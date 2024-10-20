import java.util.Scanner;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FitnessService fitnessService = new FitnessService();

        System.out.println("Welcome to the Fitness Plan Recommendation System!");

        // Simulate user roles (In a real application, this would come from user authentication)
        System.out.print("Enter your user role (e.g., Regular, Admin): ");
        String userRole = scanner.nextLine().trim();

        // Validate user role to enforce fail-safe defaults
        if (!userRole.equalsIgnoreCase("Regular") && !userRole.equalsIgnoreCase("Admin")) {
            System.out.println("Invalid user role. Access denied.");
            scanner.close();
            return;  // Fail-Safe: deny access if role is not recognized
        }

        // Collect user inputs with validation
        System.out.print("Enter your fitness goal (e.g., Weight Loss, Muscle Building, etc.): ");
        String fitnessGoal = scanner.nextLine().trim();

        System.out.print("Enter your current fitness level (Beginner, Intermediate, Advanced): ");
        String fitnessLevel = scanner.nextLine().trim();

        int age = 0;
        while (age <= 0) {
            System.out.print("Enter your age: ");
            try {
                age = Integer.parseInt(scanner.nextLine());
                if (age <= 0) {
                    System.out.println("Age must be a positive number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value for age.");
            }
        }

        System.out.print("Enter any relevant medical history (e.g., illnesses, surgeries): ");
        String medicalHistory = sanitizeInput(scanner.nextLine());

        System.out.println("\nCalculating recommendations...\n");

        // Get recommendations based on user role and display
        List<FitnessPlan> recommendedPlans = fitnessService.getRecommendedPlans(fitnessGoal, fitnessLevel, age, medicalHistory, userRole);
        if (recommendedPlans.isEmpty()) {
            System.out.println("No plans found for your input.");
        } else {
            System.out.println("Recommended Fitness Plans:");
            for (FitnessPlan plan : recommendedPlans) {
                int requiredMinutes = fitnessService.calculateRequiredExerciseTime(plan.getDuration(), fitnessLevel);
                System.out.printf("- %s (%d minutes/week) - Goal: %s\n", plan.getPlanType(), requiredMinutes, plan.getHealthGoal());
            }
            System.out.println("\nAdditional Notes: " + fitnessService.getAdditionalNotes(medicalHistory, fitnessLevel));
        }

        scanner.close();
    }

    // Method to sanitize input and prevent special characters
    private static String sanitizeInput(String input) {
        return input.replaceAll("[^a-zA-Z0-9.,\\s]", "").trim();
    }
}
