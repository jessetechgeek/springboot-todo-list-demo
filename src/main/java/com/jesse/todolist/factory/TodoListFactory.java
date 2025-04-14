package com.jesse.todolist.factory;

import com.jesse.todolist.entity.TodoItem;
import com.jesse.todolist.entity.TodoList;
import com.jesse.todolist.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Factory for creating and building TodoList domain objects.
 * This separates the creation logic from the domain objects themselves.
 */
@Component
public class TodoListFactory {

    /**
     * Creates a basic TodoList with just a name.
     *
     * @param name the name of the TodoList
     * @return a newly created TodoList
     */
    public TodoList createTodoList(String name) {
        return TodoList.Factory.createTodoList(name);
    }

    /**
     * Creates a TodoList with a name and description.
     *
     * @param name the name of the TodoList
     * @param description the description of the TodoList
     * @return a newly created TodoList
     */
    public TodoList createTodoList(String name, String description) {
        return TodoList.Factory.createTodoList(name, description);
    }

    /**
     * Creates a TodoList with items.
     *
     * @param name the name of the TodoList
     * @param description the description of the TodoList
     * @param items the initial set of TodoItems to include
     * @return a newly created TodoList with the given items
     */
    public TodoList createTodoListWithItems(String name, String description, Set<TodoItem> items) {
        return TodoList.Factory.createTodoListWithItems(name, description, items);
    }

    /**
     * Creates a TodoList and associates it with a User.
     *
     * @param name the name of the TodoList
     * @param description the description of the TodoList
     * @param user the User who owns this list
     * @return a newly created TodoList owned by the user
     */
    public TodoList createTodoListForUser(String name, String description, User user) {
        TodoList todoList = TodoList.Factory.createTodoList(name, description);
        user.addTodoList(todoList);
        return todoList;
    }
}
