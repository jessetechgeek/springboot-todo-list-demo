package com.jesse.todolist.payload.response;

import com.jesse.todolist.entity.User;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int todoListCount;
    
    public UserResponse() {
    }
    
    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = (user.getEmail() != null) ? user.getEmail().getValue() : null;
        
        if (user.getName() != null) {
            this.firstName = user.getName().getFirstName();
            this.lastName = user.getName().getLastName();
            this.fullName = user.getName().getFullName();
        } else {
            this.fullName = user.getUsername();
        }
        
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.todoListCount = user.getTodoLists().size();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public int getTodoListCount() {
        return todoListCount;
    }

    public void setTodoListCount(int todoListCount) {
        this.todoListCount = todoListCount;
    }
}
