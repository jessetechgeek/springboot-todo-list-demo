package com.jesse.todolist.controller;

import com.jesse.todolist.entity.User;
import com.jesse.todolist.factory.UserFactory;
import com.jesse.todolist.payload.request.LoginRequest;
import com.jesse.todolist.payload.request.SignupRequest;
import com.jesse.todolist.payload.response.ApiResponse;
import com.jesse.todolist.payload.response.JwtAuthResponse;
import com.jesse.todolist.repository.UserRepository;
import com.jesse.todolist.security.JwtTokenProvider;
import com.jesse.todolist.security.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API for login and registration")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final UserFactory userFactory;

    public AuthController(AuthenticationManager authenticationManager,
                         UserRepository userRepository,
                         JwtTokenProvider tokenProvider,
                         UserFactory userFactory) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.userFactory = userFactory;
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate a user and return a JWT token")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(authentication);
        
        return ResponseEntity.ok(new JwtAuthResponse(jwt, userPrincipal.getId(), userPrincipal.getUsername()));
    }

    @PostMapping("/signup")
    @Operation(summary = "User registration", description = "Register a new user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Using the factory to create a User
        User user = userFactory.createUser(
            signUpRequest.getUsername(),
            signUpRequest.getPassword(),
            signUpRequest.getEmail(),
            signUpRequest.getFirstName(),
            signUpRequest.getLastName()
        );

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully!"));
    }
}
