package com.jesse.todolist.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TodoListRequest {
    
    @NotBlank(message = "List name cannot be blank")
    @Size(min = 1, max = 100, message = "List name must be between 1 and 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    public TodoListRequest() {
    }
    
    public TodoListRequest(String name, String description) {
        this.name = name;
        this.description = description;
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
}
