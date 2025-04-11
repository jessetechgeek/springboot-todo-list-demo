package com.jesse.todolist.controller;

import com.jesse.todolist.payload.request.TodoListRequest;
import com.jesse.todolist.payload.response.ApiResponse;
import com.jesse.todolist.payload.response.TodoListResponse;
import com.jesse.todolist.security.UserPrincipal;
import com.jesse.todolist.service.TodoListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
@Tag(name = "Todo Lists", description = "APIs for managing todo lists")
public class TodoListController {

    private final TodoListService todoListService;

    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping
    @Operation(
        summary = "Get all lists", 
        description = "Retrieves all todo lists for the authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<TodoListResponse>> getAllLists(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(todoListService.getAllTodoListsByUserId(currentUser.getId()));
    }

    @GetMapping("/{listId}")
    @Operation(
        summary = "Get list by ID", 
        description = "Retrieves a specific todo list by ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<TodoListResponse> getListById(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long listId) {
        return ResponseEntity.ok(todoListService.getTodoListById(currentUser.getId(), listId));
    }

    @PostMapping
    @Operation(
        summary = "Create new list", 
        description = "Creates a new todo list for the authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<TodoListResponse> createList(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody TodoListRequest todoListRequest) {
        return new ResponseEntity<>(
                todoListService.createTodoList(currentUser.getId(), todoListRequest),
                HttpStatus.CREATED);
    }

    @PutMapping("/{listId}")
    @Operation(
        summary = "Update list", 
        description = "Updates an existing todo list",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<TodoListResponse> updateList(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long listId,
            @Valid @RequestBody TodoListRequest todoListRequest) {
        return ResponseEntity.ok(
                todoListService.updateTodoList(currentUser.getId(), listId, todoListRequest));
    }

    @DeleteMapping("/{listId}")
    @Operation(
        summary = "Delete list", 
        description = "Deletes a todo list and all its items",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse> deleteList(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long listId) {
        todoListService.deleteTodoList(currentUser.getId(), listId);
        return ResponseEntity.ok(new ApiResponse(true, "Todo list deleted successfully"));
    }
}
