package com.jesse.todolist.service;

import com.jesse.todolist.entity.DomainEvents;
import com.jesse.todolist.entity.TodoItem;
import com.jesse.todolist.entity.TodoList;
import com.jesse.todolist.event.DomainEvent;
import com.jesse.todolist.event.DomainEventPublisher;
import com.jesse.todolist.exception.ResourceNotFoundException;
import com.jesse.todolist.factory.TodoItemFactory;
import com.jesse.todolist.payload.request.TodoItemRequest;
import com.jesse.todolist.payload.response.TodoItemResponse;
import com.jesse.todolist.repository.TodoItemRepository;
import com.jesse.todolist.repository.TodoListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;
    private final TodoListRepository todoListRepository;
    private final TodoListService todoListService;
    private final TodoItemFactory todoItemFactory;
    private final DomainEventPublisher eventPublisher;

    public TodoItemService(TodoItemRepository todoItemRepository, 
                          TodoListRepository todoListRepository,
                          TodoListService todoListService,
                          TodoItemFactory todoItemFactory,
                          DomainEventPublisher eventPublisher) {
        this.todoItemRepository = todoItemRepository;
        this.todoListRepository = todoListRepository;
        this.todoListService = todoListService;
        this.todoItemFactory = todoItemFactory;
        this.eventPublisher = eventPublisher;
    }

    public List<TodoItemResponse> getAllTodoItemsByListId(Long userId, Long todoListId) {
        // This validates access
        todoListService.getTodoListById(userId, todoListId);
        
        return todoItemRepository.findByTodoListId(todoListId)
                .stream()
                .map(TodoItemResponse::new)
                .collect(Collectors.toList());
    }

    public TodoItemResponse getTodoItemById(Long userId, Long todoListId, Long todoItemId) {
        TodoItem todoItem = validateTodoItemAccess(userId, todoListId, todoItemId);
        return new TodoItemResponse(todoItem);
    }

    public TodoItemResponse createTodoItem(Long userId, Long todoListId, TodoItemRequest todoItemRequest) {
        // Validate access to the list
        TodoList todoList = todoListRepository.findById(todoListId)
                .orElseThrow(() -> new ResourceNotFoundException("TodoList", "id", todoListId));
        
        // Validate that the list belongs to the user
        if (!todoList.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: User does not have permission for this todo list");
        }
        
        // Using the factory to create a TodoItem
        TodoItem todoItem = todoItemFactory.createTodoItemInList(
            todoItemRequest.getTitle(),
            todoItemRequest.getDescription(),
            todoItemRequest.getPriority(),
            todoItemRequest.getDueDate(),
            todoList
        );
        
        if (todoItemRequest.isCompleted()) {
            todoItem.markAsCompleted();
        }
        
        TodoItem savedTodoItem = todoItemRepository.save(todoItem);
        
        // Process any domain events that might have been raised
        processDomainEvents();
        
        return new TodoItemResponse(savedTodoItem);
    }

    public TodoItemResponse updateTodoItem(Long userId, Long todoListId, Long todoItemId, TodoItemRequest todoItemRequest) {
        TodoItem todoItem = validateTodoItemAccess(userId, todoListId, todoItemId);
        
        // Using domain methods to update the entity
        todoItem.setTitle(todoItemRequest.getTitle());
        todoItem.setDescription(todoItemRequest.getDescription());
        
        if (todoItemRequest.isCompleted()) {
            todoItem.markAsCompleted();
        } else {
            todoItem.markAsIncomplete();
        }
        
        todoItem.setDueDate(todoItemRequest.getDueDate());
        todoItem.setPriority(todoItemRequest.getPriority());
        
        TodoItem updatedTodoItem = todoItemRepository.save(todoItem);
        
        // Process and publish any domain events that occurred
        processDomainEvents();
        
        return new TodoItemResponse(updatedTodoItem);
    }

    public void deleteTodoItem(Long userId, Long todoListId, Long todoItemId) {
        TodoItem todoItem = validateTodoItemAccess(userId, todoListId, todoItemId);
        TodoList todoList = todoItem.getTodoList();
        
        // First remove the item from the parent list using domain method
        if (todoList != null) {
            todoList.removeTodoItem(todoItem);
            todoListRepository.save(todoList);
        }
        
        // Then delete the item
        todoItemRepository.delete(todoItem);
    }

    private TodoItem validateTodoItemAccess(Long userId, Long todoListId, Long todoItemId) {
        // First validate the list access
        todoListService.getTodoListById(userId, todoListId);
        
        // Then validate the item
        TodoItem todoItem = todoItemRepository.findById(todoItemId)
                .orElseThrow(() -> new ResourceNotFoundException("TodoItem", "id", todoItemId));
        
        // Validate that the item belongs to the list
        if (!todoItem.getTodoList().getId().equals(todoListId)) {
            throw new RuntimeException("TodoItem does not belong to the specified TodoList");
        }
        
        return todoItem;
    }
    
    // Process and publish domain events
    private void processDomainEvents() {
        List<DomainEvent> events = DomainEvents.getEvents();
        events.forEach(eventPublisher::publish);
        DomainEvents.clear();
    }
}
