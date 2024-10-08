package application;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> userAccounts = new ArrayList<>();
    private int adminCount = 0;

    public String inviteUser(String username, String role) {
        User user = new User(username, "", "", "", "", "", ""); // Create user with an empty password
        user.addRole(role);
        userAccounts.add(user);
        return "Invitation code for " + username + " with role " + role + " created."; // Simulate invitation code
    }

    public void resetUserPassword(String username) {
        // Simulate sending a one-time password (this would be done via email in a real app)
        System.out.println("Password reset for user: " + username + ". A one-time password has been sent.");
    }

    public void deleteUser(String username) {
        userAccounts.removeIf(user -> user.getUsername().equals(username));
        System.out.println("User deleted: " + username);
    }

    public List<User> listUsers() {
        return userAccounts;
    }

    public void createAdminAccount(String username, String password) {
        User adminUser = new User(username, password, "", "", "", "", "");
        adminUser.addRole("Admin");
        userAccounts.add(adminUser);
        adminCount++;
    }

    public boolean checkIfFirstAdmin() {
        return adminCount == 0; // True if no admins exist
    }

    public boolean validateUser(String username, String password) {
        for (User user : userAccounts) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public User getUserByUsername(String username) {
        for (User user : userAccounts) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
