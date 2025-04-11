package com.jesse.todolist.payload.response;

import com.jesse.todolist.entity.TodoItem;
import com.jesse.todolist.entity.TodoPriority;

import java.time.LocalDateTime;

public class TodoItemResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;
    private TodoPriority priority;
    private Long todoListId;
    
    public TodoItemResponse() {
    }
    
    public TodoItemResponse(TodoItem todoItem) {
        this.id = todoItem.getId();
        this.title = todoItem.getTitle();
        this.description = todoItem.getDescription();
        this.completed = todoItem.isCompleted();
        this.createdAt = todoItem.getCreatedAt();
        this.updatedAt = todoItem.getUpdatedAt();
        this.dueDate = todoItem.getDueDate();
        this.priority = todoItem.getPriority();
        if (todoItem.getTodoList() != null) {
            this.todoListId = todoItem.getTodoList().getId();
        }
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    public TodoPriority getPriority() {
        return priority;
    }
    
    public void setPriority(TodoPriority priority) {
        this.priority = priority;
    }
    
    public Long getTodoListId() {
        return todoListId;
    }
    
    public void setTodoListId(Long todoListId) {
        this.todoListId = todoListId;
    }
}
