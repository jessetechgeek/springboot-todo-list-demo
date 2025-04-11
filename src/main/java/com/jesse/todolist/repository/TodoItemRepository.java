package com.jesse.todolist.repository;

import com.jesse.todolist.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findByTodoListId(Long todoListId);
}
