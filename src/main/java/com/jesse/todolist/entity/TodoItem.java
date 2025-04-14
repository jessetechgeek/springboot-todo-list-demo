package com.jesse.todolist.entity;

import com.jesse.todolist.event.TodoItemCompletedEvent;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * TodoItem represents a task within a TodoList.
 * This is a DDD-style entity with proper encapsulation and validation.
 */
@Entity
@Table(name = "todo_items")
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private boolean completed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private TodoPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_list_id")
    private TodoList todoList;

    // Private constructor for JPA and Factory use only
    protected TodoItem() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.completed = false;
        this.priority = TodoPriority.MEDIUM;
    }

    // Private constructor with title
    private TodoItem(String title) {
        this();
        setTitle(title); // Use setter for validation
    }

    // Private constructor with title and description
    private TodoItem(String title, String description) {
        this(title);
        this.description = description;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public TodoPriority getPriority() {
        return priority;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    // Domain behavior and setters with validation
    public void setTitle(String title) {
        validateTitle(title);
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsCompleted() {
        // Only publish event if it's a state change
        boolean wasAlreadyCompleted = this.completed;
        this.completed = true;
        this.updatedAt = LocalDateTime.now();
        
        // Event will be published by calling code, we just indicate the event should be raised
        if (!wasAlreadyCompleted) {
            // This will be used to trigger the event publisher in the service layer
            DomainEvents.raise(new TodoItemCompletedEvent(this));
        }
    }

    public void markAsIncomplete() {
        this.completed = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPriority(TodoPriority priority) {
        if (priority == null) {
            this.priority = TodoPriority.MEDIUM; // Default
        } else {
            this.priority = priority;
        }
        this.updatedAt = LocalDateTime.now();
    }

    // Package-private setter for TodoList
    void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }

    // Protected setter for ID (only used for persistence)
    protected void setId(Long id) {
        this.id = id;
    }

    // Domain validation
    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Todo item title cannot be empty");
        }
        if (title.length() > 255) {
            throw new IllegalArgumentException("Todo item title cannot exceed 255 characters");
        }
    }

    // Equals and hashCode based on identity (ID)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return id != null && Objects.equals(id, todoItem.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // For debugging purposes
    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                ", priority=" + priority +
                '}';
    }

    // Factory methods for creation
    public static class Factory {
        public static TodoItem createTodoItem(String title) {
            return new TodoItem(title);
        }

        public static TodoItem createTodoItem(String title, String description) {
            return new TodoItem(title, description);
        }

        public static TodoItem createTodoItem(String title, String description, TodoPriority priority, LocalDateTime dueDate) {
            TodoItem item = new TodoItem(title, description);
            item.setPriority(priority);
            item.setDueDate(dueDate);
            return item;
        }
    }

    // Pre-update callback to update the updatedAt timestamp
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
