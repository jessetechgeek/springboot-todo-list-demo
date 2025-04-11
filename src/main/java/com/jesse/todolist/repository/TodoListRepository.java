package com.jesse.todolist.repository;

import com.jesse.todolist.entity.TodoList;
import com.jesse.todolist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    List<TodoList> findByUser(User user);
    List<TodoList> findByUserId(Long userId);
}
