package com.fitnessplanrecommendation.service;

import javax.crypto.SecretKey;

import com.fitnessplanrecommendation.model.FitnessPlan;

import java.util.ArrayList;
import java.util.List;

public class FitnessService {
    private List<FitnessPlan> plans;

    public FitnessService() {
        plans = new ArrayList<>();
        plans.add(new FitnessPlan("Cardio", 150, "Beginner", "Weight Loss"));
        plans.add(new FitnessPlan("Strength Training", 120, "Intermediate", "Muscle Building"));
        plans.add(new FitnessPlan("Flexibility", 90, "Beginner", "Improve Flexibility"));
        plans.add(new FitnessPlan("HIIT", 90, "Advanced", "Improve Cardiovascular Health"));
        plans.add(new FitnessPlan("Yoga", 120, "Beginner", "Stress Relief"));
    }

    public List<FitnessPlan> getRecommendedPlans(String fitnessGoal, String fitnessLevel) {
        List<FitnessPlan> recommendedPlans = new ArrayList<>();
        for (FitnessPlan plan : plans) {
            if (plan.getHealthGoal().equalsIgnoreCase(fitnessGoal) &&
                    plan.getFitnessLevel().equalsIgnoreCase(fitnessLevel)) {
                recommendedPlans.add(plan);
            }
        }
        return recommendedPlans;
    }
}