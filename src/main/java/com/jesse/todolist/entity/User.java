package com.jesse.todolist.entity;

import com.jesse.todolist.entity.vo.Email;
import com.jesse.todolist.entity.vo.FullName;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * User represents an application user who can own multiple TodoLists.
 * This is a DDD-style entity with proper encapsulation and validation.
 */
@Entity
@Table(name = "users")
public class User {

    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MIN_PASSWORD_LENGTH = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    @Convert(converter = com.jesse.todolist.entity.converter.EmailConverter.class)
    private Email email;

    @Embedded
    private FullName name;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TodoList> todoLists = new HashSet<>();

    // Protected constructor for JPA and Factory use only
    protected User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Private constructor with required fields
    private User(String username, String password, Email email) {
        this();
        setUsername(username); // Use setters for validation
        setPassword(password);
        this.email = email;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Email getEmail() {
        return email;
    }

    public FullName getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Return an unmodifiable view of todo lists
    public Set<TodoList> getTodoLists() {
        return Collections.unmodifiableSet(todoLists);
    }

    // Domain behavior and setters with validation
    public void setUsername(String username) {
        validateUsername(username);
        this.username = username;
        this.updatedAt = LocalDateTime.now();
    }

    // Only update password if it's not already encoded
    public void setPassword(String password) {
        validatePassword(password);
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }

    public void setEmail(Email email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public void setName(FullName name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public void setName(String firstName, String lastName) {
        this.name = new FullName(firstName, lastName);
        this.updatedAt = LocalDateTime.now();
    }

    // Protected setter for ID (only used for persistence)
    protected void setId(Long id) {
        this.id = id;
    }

    // Domain methods for managing todo lists
    public void addTodoList(TodoList todoList) {
        if (todoList == null) {
            throw new IllegalArgumentException("TodoList cannot be null");
        }
        todoLists.add(todoList);
        todoList.setUser(this);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeTodoList(TodoList todoList) {
        if (todoList == null) {
            throw new IllegalArgumentException("TodoList cannot be null");
        }
        todoLists.remove(todoList);
        todoList.setUser(null);
        this.updatedAt = LocalDateTime.now();
    }

    public String getFullName() {
        return name != null ? name.getFullName() : username;
    }

    // Domain validation
    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() < MIN_USERNAME_LENGTH) {
            throw new IllegalArgumentException("Username must be at least " + MIN_USERNAME_LENGTH + " characters");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
    }

    // Equals and hashCode based on identity (ID)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // For debugging purposes
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // Factory class for creation
    public static class Factory {
        public static User createUser(String username, String password, String emailAddress) {
            Email email = new Email(emailAddress);
            return new User(username, password, email);
        }

        public static User createUser(String username, String password, String emailAddress, String firstName, String lastName) {
            User user = createUser(username, password, emailAddress);
            user.setName(firstName, lastName);
            return user;
        }
    }

    // Pre-update callback to update the updatedAt timestamp
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
