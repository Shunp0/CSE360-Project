package application;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AdminFeatures {

    private Map<String, User> userAccounts = new HashMap<>(); // Stores users by username
    private Map<String, String> invitationCodes = new HashMap<>(); // Invitation code -> Role(s)
    private Map<String, String> resetTokens = new HashMap<>(); // Username -> One-time password for reset
    private Map<String, LocalDateTime> resetExpiration = new HashMap<>(); // Username -> Expiration time of the reset password

    // Invite user to join with specified roles
    public String inviteUser(String role) {
        String invitationCode = generateInvitationCode();
        invitationCodes.put(invitationCode, role);
        System.out.println("Invitation code created for role(s): " + role);
        return invitationCode;
    }

    // Reset user account with one-time password
    public String resetUserPassword(String username, LocalDateTime expirationTime) {
        if (!userAccounts.containsKey(username)) {
            System.out.println("User not found.");
            return null;
        }
        String oneTimePassword = generateOneTimePassword();
        resetTokens.put(username, oneTimePassword);
        resetExpiration.put(username, expirationTime);
        System.out.println("Password reset for user: " + username);
        System.out.println("One-time password: " + oneTimePassword + " valid until: " + expirationTime);
        return oneTimePassword;
    }

    // Delete user account
    public boolean deleteUserAccount(String username) {
        if (!userAccounts.containsKey(username)) {
            System.out.println("User not found.");
            return false;
        }
        userAccounts.remove(username);
        System.out.println("User account deleted: " + username);
        return true;
    }

    // List all user accounts with roles
    public void listUsers() {
        if (userAccounts.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (Map.Entry<String, User> entry : userAccounts.entrySet()) {
                User user = entry.getValue();
                System.out.println("Username: " + user.getUsername() + " | Name: " + user.getFullName() + " | Roles: " + user.getRoles());
            }
        }
    }

    // Add role to user
    public boolean addRoleToUser(String username, String role) {
        if (!userAccounts.containsKey(username)) {
            System.out.println("User not found.");
            return false;
        }
        User user = userAccounts.get(username);
        if (!user.getRoles().contains(role)) {
            user.addRole(role);
            System.out.println("Role added: " + role + " to user: " + username);
            return true;
        } else {
            System.out.println("User already has the role: " + role);
            return false;
        }
    }

    // Remove role from user
    public boolean removeRoleFromUser(String username, String role) {
        if (!userAccounts.containsKey(username)) {
            System.out.println("User not found.");
            return false;
        }
        User user = userAccounts.get(username);
        if (user.getRoles().contains(role)) {
            user.removeRole(role);
            System.out.println("Role removed: " + role + " from user: " + username);
            return true;
        } else {
            System.out.println("User does not have the role: " + role);
            return false;
        }
    }

    // Helper method to generate a one-time password (simple random string)
    private String generateOneTimePassword() {
        return Long.toHexString(Double.doubleToLongBits(Math.random())); // Generates a simple random string
    }

    // Helper method to generate a random invitation code (simple random string)
    private String generateInvitationCode() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }
}
