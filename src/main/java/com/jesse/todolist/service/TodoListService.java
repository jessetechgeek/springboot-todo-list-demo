package com.jesse.todolist.service;

import com.jesse.todolist.entity.TodoList;
import com.jesse.todolist.entity.User;
import com.jesse.todolist.exception.ResourceNotFoundException;
import com.jesse.todolist.payload.request.TodoListRequest;
import com.jesse.todolist.payload.response.TodoListResponse;
import com.jesse.todolist.repository.TodoListRepository;
import com.jesse.todolist.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoListService {

    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;

    public TodoListService(TodoListRepository todoListRepository, UserRepository userRepository) {
        this.todoListRepository = todoListRepository;
        this.userRepository = userRepository;
    }

    public List<TodoListResponse> getAllTodoListsByUserId(Long userId) {
        return todoListRepository.findByUserId(userId)
                .stream()
                .map(TodoListResponse::new)
                .collect(Collectors.toList());
    }

    public TodoListResponse getTodoListById(Long userId, Long todoListId) {
        TodoList todoList = validateTodoListAccess(userId, todoListId);
        return new TodoListResponse(todoList);
    }

    public TodoListResponse createTodoList(Long userId, TodoListRequest todoListRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        TodoList todoList = new TodoList(todoListRequest.getName(), todoListRequest.getDescription());
        user.addTodoList(todoList);
        
        TodoList savedTodoList = todoListRepository.save(todoList);
        return new TodoListResponse(savedTodoList);
    }

    public TodoListResponse updateTodoList(Long userId, Long todoListId, TodoListRequest todoListRequest) {
        TodoList todoList = validateTodoListAccess(userId, todoListId);
        
        todoList.setName(todoListRequest.getName());
        todoList.setDescription(todoListRequest.getDescription());
        
        TodoList updatedTodoList = todoListRepository.save(todoList);
        return new TodoListResponse(updatedTodoList);
    }

    public void deleteTodoList(Long userId, Long todoListId) {
        TodoList todoList = validateTodoListAccess(userId, todoListId);
        todoListRepository.delete(todoList);
    }

    private TodoList validateTodoListAccess(Long userId, Long todoListId) {
        TodoList todoList = todoListRepository.findById(todoListId)
                .orElseThrow(() -> new ResourceNotFoundException("TodoList", "id", todoListId));
        
        if (!todoList.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied: User does not have permission for this todo list");
        }
        
        return todoList;
    }
}
