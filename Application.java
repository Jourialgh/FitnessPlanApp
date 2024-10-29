
// In Application.java
import java.util.Scanner;

public class Application {

    private static final AuthorizationService authService = new AuthorizationService();
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Fitness Plan Recommendation System!");
        System.out.print("Do you want to Sign Up or Login? (Enter 'signup' or 'login'): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("signup")) {
            signUp(scanner);
        } else if (choice.equals("login")) {
            login(scanner);
        } else {
            System.out.println("Invalid choice. Exiting.");
        }

        scanner.close();
    }

    private static void signUp(Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter a password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Enter your role (Admin/Regular): ");
        String role = scanner.nextLine().trim();

        if (authService.saveUser(username, password, role)) {
            System.out.println("Sign-up successful. You can now log in.");
        } else {
            System.out.println("User already exists. Please try a different username.");
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine().trim();

        if (authService.authenticateUser(username, password)) {
            String role = authService.getUserRole(username);
            System.out.println("Login successful! Welcome, " + role + " user: " + username);
            if (role.equalsIgnoreCase("Admin")) {
                showAdminMenu(scanner);
            } else {
                showUserMenu(scanner, username);
            }
        } else {
            System.out.println("Login failed. Invalid username or password.");
        }
    }

    //Admin menu
    private static void showAdminMenu(Scanner scanner) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View All Users");
            System.out.println("2. Add New User");
            System.out.println("3. Remove User");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    userService.displayAllUsers();
                    break;
                case "2":
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine().trim();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine().trim();
                    System.out.print("Enter role (Admin/Regular): ");
                    String role = scanner.nextLine().trim();
                    authService.saveUser(newUsername, password, role);
                    break;
                case "3":
                    System.out.print("Enter username to remove: ");
                    String removeUsername = scanner.nextLine().trim();
                    authService.removeUser(removeUsername);
                    break;
                case "4":
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // User menu
    private static void showUserMenu(Scanner scanner, String username) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nUser Menu:");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    userService.displayUserProfile(username);
                    break;
                case "2":
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine().trim();
                    userService.updateUserProfile(username, newPassword);
                    break;
                case "3":
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}