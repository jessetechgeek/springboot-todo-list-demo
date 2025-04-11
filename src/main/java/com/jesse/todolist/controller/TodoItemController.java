package com.jesse.todolist.controller;

import com.jesse.todolist.payload.request.TodoItemRequest;
import com.jesse.todolist.payload.response.ApiResponse;
import com.jesse.todolist.payload.response.TodoItemResponse;
import com.jesse.todolist.security.UserPrincipal;
import com.jesse.todolist.service.TodoItemService;

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
@RequestMapping("/api/lists/{listId}/items")
@Tag(name = "Todo Items", description = "APIs for managing todo items within lists")
public class TodoItemController {

    private final TodoItemService todoItemService;

    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @GetMapping
    @Operation(
        summary = "Get all items in a list", 
        description = "Retrieves all todo items for a specific list",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<TodoItemResponse>> getAllItems(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long listId) {
        return ResponseEntity.ok(todoItemService.getAllTodoItemsByListId(currentUser.getId(), listId));
    }

    @GetMapping("/{itemId}")
    @Operation(
        summary = "Get item by ID", 
        description = "Retrieves a specific todo item by ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<TodoItemResponse> getItemById(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long listId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(todoItemService.getTodoItemById(currentUser.getId(), listId, itemId));
    }

    @PostMapping
    @Operation(
        summary = "Create new item", 
        description = "Creates a new todo item in a specific list",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<TodoItemResponse> createItem(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long listId,
            @Valid @RequestBody TodoItemRequest todoItemRequest) {
        return new ResponseEntity<>(
                todoItemService.createTodoItem(currentUser.getId(), listId, todoItemRequest),
                HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}")
    @Operation(
        summary = "Update item", 
        description = "Updates an existing todo item",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<TodoItemResponse> updateItem(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long listId,
            @PathVariable Long itemId,
            @Valid @RequestBody TodoItemRequest todoItemRequest) {
        return ResponseEntity.ok(
                todoItemService.updateTodoItem(currentUser.getId(), listId, itemId, todoItemRequest));
    }

    @DeleteMapping("/{itemId}")
    @Operation(
        summary = "Delete item", 
        description = "Deletes a todo item",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse> deleteItem(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long listId,
            @PathVariable Long itemId) {
        todoItemService.deleteTodoItem(currentUser.getId(), listId, itemId);
        return ResponseEntity.ok(new ApiResponse(true, "Todo item deleted successfully"));
    }
}
