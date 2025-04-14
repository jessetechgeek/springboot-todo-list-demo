package com.jesse.todolist.factory;

import com.jesse.todolist.entity.TodoItem;
import com.jesse.todolist.entity.TodoList;
import com.jesse.todolist.entity.TodoPriority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Factory for creating and building TodoItem domain objects.
 * This separates the creation logic from the domain objects themselves.
 */
@Component
public class TodoItemFactory {

    /**
     * Creates a basic TodoItem with just a title.
     *
     * @param title the title of the TodoItem
     * @return a newly created TodoItem
     */
    public TodoItem createTodoItem(String title) {
        return TodoItem.Factory.createTodoItem(title);
    }

    /**
     * Creates a TodoItem with a title and description.
     *
     * @param title the title of the TodoItem
     * @param description the description of the TodoItem
     * @return a newly created TodoItem
     */
    public TodoItem createTodoItem(String title, String description) {
        return TodoItem.Factory.createTodoItem(title, description);
    }

    /**
     * Creates a TodoItem with all detailed attributes.
     *
     * @param title the title of the TodoItem
     * @param description the description of the TodoItem
     * @param priority the priority level of the TodoItem
     * @param dueDate the due date of the TodoItem
     * @return a newly created TodoItem
     */
    public TodoItem createTodoItem(String title, String description, TodoPriority priority, LocalDateTime dueDate) {
        return TodoItem.Factory.createTodoItem(title, description, priority, dueDate);
    }

    /**
     * Creates a TodoItem and associates it with a TodoList.
     *
     * @param title the title of the TodoItem
     * @param description the description of the TodoItem
     * @param priority the priority level of the TodoItem
     * @param dueDate the due date of the TodoItem
     * @param todoList the TodoList to associate with this item
     * @return a newly created TodoItem
     */
    public TodoItem createTodoItemInList(String title, String description, 
                                         TodoPriority priority, LocalDateTime dueDate,
                                         TodoList todoList) {
        TodoItem todoItem = TodoItem.Factory.createTodoItem(title, description, priority, dueDate);
        todoList.addTodoItem(todoItem);
        return todoItem;
    }
}
