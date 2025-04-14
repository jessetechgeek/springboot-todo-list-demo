package com.jesse.todolist.repository;

import com.jesse.todolist.entity.User;
import com.jesse.todolist.entity.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    Boolean existsByUsername(String username);
    
    // Using native query to check email as a string
    @Query(value = "SELECT COUNT(*) > 0 FROM users u WHERE u.email = :email", nativeQuery = true)
    Boolean existsByEmail(@Param("email") String email);
}
