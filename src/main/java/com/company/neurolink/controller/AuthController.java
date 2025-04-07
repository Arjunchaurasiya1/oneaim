package com.company.neurolink.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.company.neurolink.exception.UserNotFoundException;
import com.company.neurolink.model.User;
import com.company.neurolink.security.JwtUtil;
import com.company.neurolink.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${some.property:default_value}")
    private String someProperty;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

   

    // Register a new customer
    @PostMapping("/register-customer")
    public ResponseEntity<String> registerCustomer(@RequestBody User user) {
        try {
            userService.registerCustomer(user);
            return ResponseEntity.ok("Customer registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Register a new admin
    @PostMapping("/register-admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody User user) {
        try {
            userService.registerAdmin(user);
            return ResponseEntity.ok("Admin registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User authenticatedUser = userService.login(user.getEmail(), user.getPassword());

        if (authenticatedUser != null) {
            String token = jwtUtil.generateToken(authenticatedUser.getEmail(), authenticatedUser.getId(), authenticatedUser.getName(), null);
            return ResponseEntity.ok(new LoginResponse("Login successful", token, authenticatedUser.getName()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
    }

    public static class LoginResponse {
        private String message;
        private String token;
        private String name;

        public LoginResponse(String message, String token, String name) {
            this.message = message;
            this.token = token;
            this.name = name;
        }

        public String getMessage() { return message; }
        public String getToken() { return token; }
        public String getName() { return name; }
    }

 
    // Delete a user (only for admins)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while deleting the user: " + ex.getMessage());
        }
    }
}
