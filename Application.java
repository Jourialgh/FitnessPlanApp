import java.util.Scanner;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FitnessService fitnessService = new FitnessService();

        System.out.println("Welcome to the Fitness Plan Recommendation System!");

        // Collect user inputs
        System.out.print("Enter your fitness goal (e.g., Weight Loss, Muscle Building, etc.): ");
        String fitnessGoal = scanner.nextLine();

        System.out.print("Enter your current fitness level (Beginner, Intermediate, Advanced): ");
        String fitnessLevel = scanner.nextLine();

        System.out.println("\nCalculating recommendations...\n");

        // Get recommendations and display
        List<FitnessPlan> recommendedPlans = fitnessService.getRecommendedPlans(fitnessGoal, fitnessLevel);
        if (recommendedPlans.isEmpty()) {
            System.out.println("No plans found for your input.");
        } else {
            System.out.println("Recommended Fitness Plans:");
            for (FitnessPlan plan : recommendedPlans) {
                System.out.printf("- %s (%d minutes/week) - Goal: %s\n", plan.getPlanType(), plan.getDuration(), plan.getHealthGoal());
            }
        }

        scanner.close();
    }
}
