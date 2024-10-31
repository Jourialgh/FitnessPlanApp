package com.fitnessplanrecommendation.fitnessplan.service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//Libraries handling the hashing function
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Base64;

import org.springframework.stereotype.Service;
@Service
public class AuthorizationService {
    private static final String USER_FILE = "users.txt";

    //Hashing password method
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest hashGenerator = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = hashGenerator.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    //Save new user with the hashed password
    public boolean saveUser(String username, String password, String role) {
        if (userExists(username))
            return false;

        try (FileWriter writer = new FileWriter(USER_FILE, true)) {
            String hashedPassword = hashPassword(password);
            writer.write(username + "," + hashedPassword + "," + role + "\n");
           
            //System.out.println("Original password:"+ password);
            //System.out.println("Hashed password:"+ hashedPassword);

            return true;
        } catch (IOException | NoSuchAlgorithmException e) {
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

    //Authenticate user by reading users file and comparing hashed password
    public boolean authenticateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            String hashedPassword = hashPassword(password);

            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(username) && userDetails[1].equals(hashedPassword)) {
                    return true;
                }
            }
        } catch (IOException | NoSuchAlgorithmException e) {
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
