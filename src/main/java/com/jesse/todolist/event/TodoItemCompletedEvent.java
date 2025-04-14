package com.jesse.todolist.event;

import com.jesse.todolist.entity.TodoItem;

/**
 * Domain event for when a todo item is marked as completed.
 */
public class TodoItemCompletedEvent extends DomainEvent {
    
    private final Long todoItemId;
    private final String title;
    private final Long todoListId;
    private final Long userId;
    
    public TodoItemCompletedEvent(TodoItem todoItem) {
        this.todoItemId = todoItem.getId();
        this.title = todoItem.getTitle();
        this.todoListId = todoItem.getTodoList() != null ? todoItem.getTodoList().getId() : null;
        this.userId = todoItem.getTodoList() != null && todoItem.getTodoList().getUser() != null ? 
                todoItem.getTodoList().getUser().getId() : null;
    }
    
    public Long getTodoItemId() {
        return todoItemId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Long getTodoListId() {
        return todoListId;
    }
    
    public Long getUserId() {
        return userId;
    }
}
