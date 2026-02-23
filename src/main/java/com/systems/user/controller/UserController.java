package com.systems.user.controller;

import com.systems.user.dto.CreateUserRequest;
import com.systems.user.dto.RegisterRequest;
import com.systems.user.dto.UpdateUserRequest;
import com.systems.user.dto.UserDto;
import com.systems.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody RegisterRequest request) {
        log.info("Public registration request for username: {}", request.username());
        return userService.registerUser(request);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers() {
        log.info("Request to get all users");
        return userService.getAllUsers();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Request to get user by ID: {}", id);
        return userService.getUserById(id);
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserDto getUserByUsername(@PathVariable String username) {
        log.info("Request to get user: {}", username);
        return userService.getUserByUsername(username);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid CreateUserRequest request) {
        log.info("Request to create user: {}", request.username());
        return userService.createUser(request);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
        log.info("Request to update user ID: {}", id);
        return userService.updateUser(id, request);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("Request to delete user ID: {}", id);
        userService.deleteUser(id);
    }
}
