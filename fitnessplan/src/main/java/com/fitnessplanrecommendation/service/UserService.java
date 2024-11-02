package com.fitnessplanrecommendation.service;

import com.fitnessplanrecommendation.model.User;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserService {

    private AuthorizationService authorizationService = new AuthorizationService();

    private static final String USER_FILE = "users.txt";
    private static final String ALGORITHM = "AES";
    private SecretKey secretKey;

    public UserService() {
        try {
            this.secretKey = generateSecretKey();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing encryption key", e);
        }
    }

    // Display all users
    public List<User> displayAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                users.add(new User(parts[0], parts[1], parts[2], ""));
            }
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
        return users;
    }

    public boolean deleteUser(String username) {
        List<User> users = displayAllUsers();
        boolean userFound = users.removeIf(user -> user.getUsername().equals(username));
        if (userFound) {
            saveAllUsers(users);
        }
        return userFound;
    }

    public boolean updateUserRole(String username, String newRole) {
        List<User> users = displayAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setRole(newRole);
                saveAllUsers(users);
                return true;
            }
        }
        return false;
    }


    // Hashing password method
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest hashGenerator = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = hashGenerator.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    // Save new user with the hashed password
    public boolean saveUser(String username, String password, String role, String medicalHistory) throws Exception {
        if (userExists(username))
            return false;

        String hashedPassword = authorizationService.hashPassword(password);
        String encryptedHistory = encrypt(medicalHistory, secretKey);
        return authorizationService.saveUser(username, hashedPassword, role);
    }

    // Authenticate user by reading users file and comparing hashed password
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

    // Get user role
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
        return "Regular"; // Default to Regular if role not found
    }

    // Check if user already exists
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

    // Generate a new AES secret key
    public static SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128);
        return keyGen.generateKey();
    }

    // Encrypt data using AES
    public static String encrypt(String data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt data using AES
    public static String decrypt(String encryptedData, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    private void saveAllUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : users) {
                writer.write(String.format("%s,%s,%s%n", 
                    user.getUsername(), 
                    user.getPassword(), 
                    user.getRole()));
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}