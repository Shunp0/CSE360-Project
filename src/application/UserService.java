package application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private Map<String, User> users = new HashMap<>();
    private Map<String, String> invitations = new HashMap<>(); // Store invitation codes and roles

    public String register(String username, String password, String confirmPassword, String invitationCode) {
        // Check if username is already taken
        if (users.containsKey(username)) {
            return "Username already exists.";
        }

        // Check if password and confirm password match
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }

        // Verify invitation code
        if (!invitations.containsKey(invitationCode)) {
            return "Invalid invitation code.";
        }

        // Hash the password
        byte[] passwordHash = hashPassword(password);

        // Create and store the user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(passwordHash);

        // Assign roles based on invitation code
        String role = invitations.remove(invitationCode);
        newUser.addRole(role);
        users.put(username, newUser);

        return "Registration successful.";
    }

    public String login(String username, String password) {
        // Check if user exists
        User user = users.get(username);
        if (user == null) {
            return "User does not exist.";
        }

        // Verify the password
        byte[] passwordHash = hashPassword(password);
        if (!MessageDigest.isEqual(user.getPasswordHash(), passwordHash)) {
            return "Invalid password.";
        }

        return "Login successful!";
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public void inviteUser(String username, String role) {
        String invitationCode = generateInvitationCode();
        invitations.put(invitationCode, role);
        // Logic to send invitation to the user (e.g., email) can be added here
    }

    public void resetUserPassword(String username) {
        User user = users.get(username);
        if (user != null) {
            // Simulated one-time password reset logic
            user.setOneTimePassword(true);
            user.setPassword("OneTimePassword123"); // Set a temporary password
        }
    }

    public void deleteUser(String username) {
        users.remove(username);
    }

    public Map<String, User> listUsers() {
        return users; // Return a map of all users
    }

    private String generateInvitationCode() {
        return Long.toHexString(System.currentTimeMillis());
    }

    private byte[] hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
