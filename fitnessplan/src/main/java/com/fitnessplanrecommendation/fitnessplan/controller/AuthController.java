package com.fitnessplanrecommendation.fitnessplan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fitnessplanrecommendation.fitnessplan.model.FitnessPlan;
import com.fitnessplanrecommendation.fitnessplan.service.AuthorizationService;
import com.fitnessplanrecommendation.fitnessplan.service.FitnessService;
import com.fitnessplanrecommendation.fitnessplan.service.UserService;

@Controller
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthorizationService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private FitnessService fitnessService;

    
    @GetMapping("/home")
    public String showHomePage(Model model) {
        String message = "Welcome to the Fitness Plan Recommendation System!";
        model.addAttribute("message", message);
        return "home";  // Refers to home.html in /templates
    }    

        

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        return "signup";  // Refers to signup.html in /templates
    }

    @PostMapping("/signup")
    public String signUp(@RequestParam String username, @RequestParam String password, @RequestParam String role, Model model) {
        String message;
        if (authService.saveUser(username, password, role)) {
            message = "Sign-up successful. You can now log in.";
            model.addAttribute("message", message);
            return "redirect:/api/login";  // Redirect to login page on sign-up success
        } else {
            message = "User already exists. Please try a different username.";
            model.addAttribute("message", message);
            return "signup";  // Reloads signup.html with an error message
        }
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "login";  // Refers to login.html in /templates
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        String message;
        if (authService.authenticateUser(username, password)) {
            String role = authService.getUserRole(username);
            message = "Login successful! Welcome, " + role + " user: " + username;
            model.addAttribute("message", message);
            return "redirect:/somewhere"; // Change this to the appropriate redirect after login success
        } else {
            message = "Login failed. Invalid username or password.";
            model.addAttribute("message", message);
            return "login";  // Reloads login.html with an error message
        }
    }

    @GetMapping("/fitness/recommend")
    public String recommendFitnessPlan(@RequestParam String goal, 
                                       @RequestParam String level, 
                                       @RequestParam int age, 
                                       @RequestParam String medicalHistory, 
                                       Model model) {
        List<FitnessPlan> plans = fitnessService.getRecommendedPlans(goal, level, age, medicalHistory, "Regular");
        model.addAttribute("plans", plans);
        return "recommend";  // Refers to recommend.html in /templates
    }
}
