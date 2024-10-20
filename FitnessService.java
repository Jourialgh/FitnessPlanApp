import java.util.ArrayList;
import java.util.List;

public class FitnessService {
    private List<FitnessPlan> plans;

    public FitnessService() {
        // Initialize the list of fitness plans
        plans = new ArrayList<>();
        plans.add(new FitnessPlan("Cardio", 150, "Beginner", "Weight Loss"));
        plans.add(new FitnessPlan("Strength Training", 120, "Intermediate", "Muscle Building"));
        plans.add(new FitnessPlan("Flexibility", 90, "Beginner", "Improve Flexibility"));
        plans.add(new FitnessPlan("HIIT", 90, "Advanced", "Improve Cardiovascular Health"));
        plans.add(new FitnessPlan("Yoga", 120, "Beginner", "Stress Relief"));
    }

    // Method to get recommended plans based on fitness goal, fitness level, medical history, and user role
    public List<FitnessPlan> getRecommendedPlans(String fitnessGoal, String fitnessLevel, int age, String medicalHistory, String userRole) {
        List<FitnessPlan> recommendedPlans = new ArrayList<>();
        for (FitnessPlan plan : plans) {
            // Role-based access: Admins have access to all plans, Regular users have limited access
            if (userRole.equalsIgnoreCase("Admin") ||
                (plan.getHealthGoal().equalsIgnoreCase(fitnessGoal) &&
                 plan.getFitnessLevel().equalsIgnoreCase(fitnessLevel) &&
                 isPlanSuitableForMedicalHistory(plan, medicalHistory))) {
                recommendedPlans.add(plan);
            }
        }
        return recommendedPlans;
    }

    // Check if the plan is suitable for the user's medical history
    private boolean isPlanSuitableForMedicalHistory(FitnessPlan plan, String medicalHistory) {
        // Implement custom checks for medical conditions
        if (medicalHistory.toLowerCase().contains("heart condition") && plan.getPlanType().equalsIgnoreCase("HIIT")) {
            return false; // Avoid HIIT for users with heart conditions
        }
        return true;
    }

    // Method to calculate the required exercise time based on the user's fitness level
    public int calculateRequiredExerciseTime(int baseDuration, String fitnessLevel) {
        int additionalMinutes = 0;
        switch (fitnessLevel.toLowerCase()) {
            case "beginner":
                additionalMinutes = 30;
                break;
            case "intermediate":
                additionalMinutes = 20;
                break;
            case "advanced":
                additionalMinutes = 10;
                break;
            default:
                break;
        }
        return baseDuration + additionalMinutes;
    }

    // Method to provide additional notes based on the user's medical history and fitness level
    public String getAdditionalNotes(String medicalHistory, String fitnessLevel) {
        StringBuilder notes = new StringBuilder();
        if (medicalHistory.toLowerCase().contains("joint pain") && fitnessLevel.equalsIgnoreCase("Beginner")) {
            notes.append("Consider low-impact exercises like Yoga or Flexibility training to reduce stress on joints.");
        }
        if (medicalHistory.toLowerCase().contains("surgery")) {
            notes.append("Consult with your healthcare provider before starting a new fitness plan.");
        }
        return notes.toString();
    }
}

