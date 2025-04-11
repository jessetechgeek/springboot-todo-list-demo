package com.jesse.todolist.payload.request;

import com.jesse.todolist.entity.TodoPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class TodoItemRequest {
    
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    private boolean completed;
    
    private LocalDateTime dueDate;
    
    private TodoPriority priority;
    
    public TodoItemRequest() {
        this.priority = TodoPriority.MEDIUM;
        this.completed = false;
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
}
