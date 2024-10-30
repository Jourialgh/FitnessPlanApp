package com.fitnessplanrecommendation.fitnessplan.service;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;
import com.fitnessplanrecommendation.fitnessplan.model.FitnessPlan;
import com.fitnessplanrecommendation.fitnessplan.utils.EncryptionUtils;
import org.springframework.stereotype.Service;

@Service
public class FitnessService {
    private List<FitnessPlan> plans;
    private SecretKey secretKey;

    public FitnessService() {
        //List of fitness plans
        plans = new ArrayList<>();
        plans.add(new FitnessPlan("Cardio", 150, "Beginner", "Weight Loss"));
        plans.add(new FitnessPlan("Strength Training", 120, "Intermediate", "Muscle Building"));
        plans.add(new FitnessPlan("Flexibility", 90, "Beginner", "Improve Flexibility"));
        plans.add(new FitnessPlan("HIIT", 90, "Advanced", "Improve Cardiovascular Health"));
        plans.add(new FitnessPlan("Yoga", 120, "Beginner", "Stress Relief"));

        try {
            //Generate a new secret key for encryption/decryption
            secretKey = EncryptionUtils.generateSecretKey();
        } catch (Exception e) {
            System.out.println("Error generating encryption key: " + e.getMessage());
        }
    }

    //Method to get recommended plans based on fitness goal, fitness level, medical history, and user role
    public List<FitnessPlan> getRecommendedPlans(String fitnessGoal, String fitnessLevel, int age, String medicalHistory, String userRole) {
        List<FitnessPlan> recommendedPlans = new ArrayList<>();
        
        //Encrypt the medical history
        String encryptedMedicalHistory = null;
        try {
            encryptedMedicalHistory = EncryptionUtils.encrypt(medicalHistory, secretKey);
            System.out.println("Encrypted Medical History: " + encryptedMedicalHistory);

        } catch (Exception e) {
            System.out.println("Error encrypting medical history: " + e.getMessage());
        }

        for (FitnessPlan plan : plans) {
            if (userRole.equalsIgnoreCase("Admin") ||
                (plan.getHealthGoal().equalsIgnoreCase(fitnessGoal) &&
                 plan.getFitnessLevel().equalsIgnoreCase(fitnessLevel) &&
                 isPlanSuitableForMedicalHistory(plan, encryptedMedicalHistory))) {
                recommendedPlans.add(plan);
            }
        }
        return recommendedPlans;
    }

    //Check if the plan is suitable for the user's encrypted medical history
    private boolean isPlanSuitableForMedicalHistory(FitnessPlan plan, String encryptedMedicalHistory) {
        try {
            //Decrypt the medical history to check for specific conditions
            String decryptedMedicalHistory = EncryptionUtils.decrypt(encryptedMedicalHistory, secretKey);
            System.out.println("Decrypted Medical History: " + decryptedMedicalHistory);

            if (decryptedMedicalHistory.toLowerCase().contains("heart condition") && plan.getPlanType().equalsIgnoreCase("HIIT")) {
                return false; 
            }
        } catch (Exception e) {
            System.out.println("Error decrypting medical history: " + e.getMessage());
            return false;
        }
        return true;
    }


    //Simple method to test if enc/dec works
    public void testEncryption() {
        String originalData = "Patient has no known allergies.";
        
        try {
            //Encrypt the original data
            String encryptedData = EncryptionUtils.encrypt(originalData, secretKey);
            System.out.println("Encrypted Data: " + encryptedData);
    
            //Decrypt the data
            String decryptedData = EncryptionUtils.decrypt(encryptedData, secretKey);
            System.out.println("Decrypted Data: " + decryptedData);
    
            //Check if the decrypted data matches the original
            if (originalData.equals(decryptedData)) {
                System.out.println("Encryption and Decryption are working correctly.");
            } else {
                System.out.println("Mismatch! Encryption and Decryption are not working correctly.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred during encryption/decryption: " + e.getMessage());
        }
    }

}