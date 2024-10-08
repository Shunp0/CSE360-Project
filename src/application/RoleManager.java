package application;

import java.util.ArrayList;
import java.util.List;

public class RoleManager {
    public enum Role { ADMIN, STUDENT, INSTRUCTOR }

    // Adds a role to the user if it doesn't already exist
    public static void addRole(User user, Role role) {
        if (!user.getRoles().contains(role.toString())) {
            user.addRole(role.toString());
        }
    }

    // Removes a role from the user if it exists
    public static void removeRole(User user, Role role) {
        if (user.getRoles().contains(role.toString())) {
            user.removeRole(role.toString());
        }
    }

    // Returns a list of roles for the user
    public static List<String> getRoles(User user) {
        return new ArrayList<>(user.getRoles());
    }

    // Check if the user has a specific role
    public static boolean hasRole(User user, Role role) {
        return user.getRoles().contains(role.toString());
    }

    // Assign multiple roles to a user
    public static void assignRoles(User user, List<Role> roles) {
        for (Role role : roles) {
            addRole(user, role);
        }
    }

    // Remove multiple roles from a user
    public static void removeRoles(User user, List<Role> roles) {
        for (Role role : roles) {
            removeRole(user, role);
        }
    }
}
