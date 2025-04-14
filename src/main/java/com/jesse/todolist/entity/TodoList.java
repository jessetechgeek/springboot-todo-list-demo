package com.jesse.todolist.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * TodoList represents a collection of related TodoItems.
 * This is a DDD-style entity with proper encapsulation and validation.
 */
@Entity
@Table(name = "todo_lists")
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "todoList", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TodoItem> todoItems = new HashSet<>();

    // Protected constructor for JPA and Factory use only
    protected TodoList() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Private constructor with name
    private TodoList(String name) {
        this();
        setName(name); // Use setter for validation
    }

    // Private constructor with name and description
    private TodoList(String name, String description) {
        this(name);
        this.description = description;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    // Return an unmodifiable view of the items
    public Set<TodoItem> getTodoItems() {
        return Collections.unmodifiableSet(todoItems);
    }

    // Domain behavior and setters with validation
    public void setName(String name) {
        validateName(name);
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    // Package-private setter for user
    void setUser(User user) {
        this.user = user;
    }

    // Protected setter for ID (only used for persistence)
    protected void setId(Long id) {
        this.id = id;
    }

    // Domain methods for managing todo items
    public void addTodoItem(TodoItem todoItem) {
        if (todoItem == null) {
            throw new IllegalArgumentException("TodoItem cannot be null");
        }
        todoItems.add(todoItem);
        todoItem.setTodoList(this);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeTodoItem(TodoItem todoItem) {
        if (todoItem == null) {
            throw new IllegalArgumentException("TodoItem cannot be null");
        }
        todoItems.remove(todoItem);
        todoItem.setTodoList(null);
        this.updatedAt = LocalDateTime.now();
    }

    public int getCompletedItemsCount() {
        return (int) todoItems.stream().filter(TodoItem::isCompleted).count();
    }

    public int getTotalItemsCount() {
        return todoItems.size();
    }

    // Domain validation
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Todo list name cannot be empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Todo list name cannot exceed 255 characters");
        }
    }

    // Equals and hashCode based on identity (ID)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoList todoList = (TodoList) o;
        return id != null && Objects.equals(id, todoList.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // For debugging purposes
    @Override
    public String toString() {
        return "TodoList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", itemCount=" + todoItems.size() +
                '}';
    }

    // Factory class for creation
    public static class Factory {
        public static TodoList createTodoList(String name) {
            return new TodoList(name);
        }

        public static TodoList createTodoList(String name, String description) {
            return new TodoList(name, description);
        }

        public static TodoList createTodoListWithItems(String name, String description, Set<TodoItem> items) {
            TodoList todoList = new TodoList(name, description);
            if (items != null) {
                items.forEach(todoList::addTodoItem);
            }
            return todoList;
        }
    }

    // Pre-update callback to update the updatedAt timestamp
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
