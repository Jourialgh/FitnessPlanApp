package com.fitnessplanrecommendation.controller;

import com.fitnessplanrecommendation.model.User;
import com.fitnessplanrecommendation.service.AuthorizationService;
import com.fitnessplanrecommendation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserService userService;

    // Sign Up
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        if (authorizationService.saveUser(user.getUsername(), user.getPassword(), user.getRole())) {
            return ResponseEntity.ok("User registered successfully");
        }
        return ResponseEntity.badRequest().body("User already exists or an error occurred.");
    }

    // Log In
    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody User user) {
        if (authorizationService.authenticateUser(user.getUsername(), user.getPassword())) {
            return ResponseEntity.ok(authorizationService.getUserRole(user.getUsername()));
        }
        return ResponseEntity.status(401).body("Invalid username or password.");
    }

    // Admin: Get All Users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.displayAllUsers());
    }

    // Admin: Delete User
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        if (userService.deleteUser(username)) {
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    // Admin: Update User Role
    @PutMapping("/update-role")
    public ResponseEntity<String> updateUserRole(@RequestParam String username, @RequestParam String role) {
        if (userService.updateUserRole(username, role)) {
            return ResponseEntity.ok("User role updated successfully");
        }
        return ResponseEntity.badRequest().body("User not found");
    }
}
