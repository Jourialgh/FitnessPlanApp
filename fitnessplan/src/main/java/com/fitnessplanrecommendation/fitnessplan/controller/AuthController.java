package com.fitnessplanrecommendation.fitnessplan.controller;

import com.fitnessplanrecommendation.fitnessplan.service.AuthorizationService;
import com.fitnessplanrecommendation.fitnessplan.service.FitnessService;
import com.fitnessplanrecommendation.fitnessplan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fitnessplanrecommendation.fitnessplan.model.FitnessPlan;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthorizationService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private FitnessService fitnessService;

    @PostMapping("/signup")
    public String signUp(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        if (authService.saveUser(username, password, role)) {
            return "Sign-up successful. You can now log in.";
        } else {
            return "User already exists. Please try a different username.";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        if (authService.authenticateUser(username, password)) {
            String role = authService.getUserRole(username);
            return "Login successful! Welcome, " + role + " user: " + username;
        } else {
            return "Login failed. Invalid username or password.";
        }
    }

    @GetMapping("/fitness/recommend")

    public List<FitnessPlan> recommendFitnessPlan(@RequestParam String goal, 
                                                   @RequestParam String level, 
                                                   @RequestParam int age, 
                                                   @RequestParam String medicalHistory) {
        return fitnessService.getRecommendedPlans(goal, level, age, medicalHistory, "Regular");
    }

    // Additional endpoints for admin and user functionality can be added here
}