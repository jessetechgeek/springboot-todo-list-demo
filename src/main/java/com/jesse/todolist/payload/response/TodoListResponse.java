package com.jesse.todolist.payload.response;

import com.jesse.todolist.entity.TodoList;

import java.time.LocalDateTime;

public class TodoListResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int itemCount;
    
    public TodoListResponse() {
    }
    
    public TodoListResponse(TodoList todoList) {
        this.id = todoList.getId();
        this.name = todoList.getName();
        this.description = todoList.getDescription();
        this.createdAt = todoList.getCreatedAt();
        this.updatedAt = todoList.getUpdatedAt();
        this.itemCount = todoList.getTodoItems().size();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public int getItemCount() {
        return itemCount;
    }
    
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
