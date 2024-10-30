package com.fitnessplanrecommendation.fitnessplan.service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    //Display all users
    public void displayAllUsers() {
        System.out.println("All Registered Users:");
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                System.out.println("Username: " + parts[0] + ", Role: " + parts[2]);
            }
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
    }

    //Display a user's profile (for regular users)
    public void displayUserProfile(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    System.out.println("Profile - Username: " + parts[0] + ", Role: " + parts[2]);
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading profile: " + e.getMessage());
        }
    }

    //Update user's password
    public void updateUserProfile(String username, String newPassword) {
        AuthorizationService authService = new AuthorizationService();
        authService.removeUser(username);
        authService.saveUser(username, newPassword, "Regular"); 
        System.out.println("Profile updated successfully.");
    }
}
