// separation of duties design principle. a separate AuthorizationService to handle all role and permission checks.
// minimize trust surface
public class AuthorizationService {
    // Only expose minimal methods needed to validate roles
    public boolean authorizeUser(String role, String action) {
        if (role.equalsIgnoreCase("Admin")) {
            return true; // Admins can access everything
        }
        // Regular users can only perform specific actions
        if (role.equalsIgnoreCase("Regular") && action.equalsIgnoreCase("viewPlan")) {
            return true;
        }
        return false;
    }

    public boolean isValidRole(String role) {
        return role.equalsIgnoreCase("Regular") || role.equalsIgnoreCase("Admin");
    }
}