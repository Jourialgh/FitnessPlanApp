package com.fitnessplanrecommendation.model;

public class User {
    private String username;
    private String password;
    private String role;
    private String medicalHistory;

    // Constructors
    public User(String username, String password, String role, String medicalHistory) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.medicalHistory = medicalHistory;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

}
