package com.fitnessplanrecommendation.fitnessplan.service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Service;
@Service
public class AuthorizationService {
    private static final String USER_FILE = "users.txt";

    //Save new user to the users file
    public boolean saveUser(String username, String password, String role) {
        if (userExists(username))
            return false;

        try (FileWriter writer = new FileWriter(USER_FILE, true)) {
            writer.write(username + "," + password + "," + role + "\n");
            return true;
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    //Remove user from the file
    public boolean removeUser(String username) {
        File inputFile = new File(USER_FILE);
        File tempFile = new File("temp_users.txt");

        boolean userRemoved = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ",")) {
                    userRemoved = true;
                    continue;
                }
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error removing user: " + e.getMessage());
            return false;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        if (userRemoved) {
            System.out.println("User removed successfully.");
        } else {
            System.out.println("User not found.");
        }
        return userRemoved;
    }

    //Authenticate user by reading users file
    public boolean authenticateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(username) && userDetails[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return false;
    }

    //Get user role
    public String getUserRole(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(username)) {
                    return userDetails[2]; 
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return "Regular"; //Default to Regular if role not found
    }

    //Check if user already exists
    private boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return false;
    }
}
