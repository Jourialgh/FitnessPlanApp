package com.fitnessplanrecommendation.service;

import org.springframework.stereotype.Service;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class AuthorizationService {
    private static final String USER_FILE = "users.txt";

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest hashGenerator = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = hashGenerator.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    public boolean saveUser(String username, String password, String role) {
        if (userExists(username)) return false;
        try (FileWriter writer = new FileWriter(USER_FILE, true)) {
            String hashedPassword = hashPassword(password);
            writer.write(username + "," + hashedPassword + "," + role + "\n");
            return true;
        } catch (IOException | NoSuchAlgorithmException e) {
            return false;
        }
    }

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
            System.out.println("Error during authentication: " + e.getMessage());
        }
        return false;
    }

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
            System.out.println("Error reading user role: " + e.getMessage());
        }
        return "Regular";
    }

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
            System.out.println("Error checking user existence: " + e.getMessage());
        }
        return false;
    }
}
