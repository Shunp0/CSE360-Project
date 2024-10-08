package application;

import java.util.ArrayList;
import java.util.List;

public class RoleManager {
    public static final String ADMIN = "Admin";
    public static final String STUDENT = "Student";
    public static final String INSTRUCTOR = "Instructor";

    public static void addRole(User user, String role) {
        if (!user.getRoles().contains(role)) {
            user.addRole(role);
        }
    }

    public static void removeRole(User user, String role) {
        if (user.getRoles().contains(role)) {
            user.removeRole(role);
        }
    }

    public static List<String> getRoles(User user) {
        return new ArrayList<>(user.getRoles());
    }

    public static boolean hasRole(User user, String role) {
        return user.getRoles().contains(role);
    }
}
