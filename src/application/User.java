package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password; // Store plaintext password for simplicity; hash in production
    private String email; // User's email address
    private String firstName; // User's first name
    private String middleName; // User's middle name (optional)
    private String lastName; // User's last name
    private String preferredFirstName; // Optional preferred first name
    private List<String> roles; // List of roles assigned to the user
    private boolean oneTimePassword; // Flag for one-time password usage
    private byte[] passwordHash; // To store hashed password
    private LocalDateTime oneTimePasswordExpiry; // Expiry time for one-time password

    // Default constructor
    public User() {
        this.username = "";
        this.password = "";
        this.email = "";
        this.firstName = "";
        this.middleName = "";
        this.lastName = "";
        this.preferredFirstName = "";
        this.roles = new ArrayList<>();
        this.oneTimePassword = false;
        this.passwordHash = null; // Initialize with null
        this.oneTimePasswordExpiry = null; // Initialize with null
    }

    // Constructor for user creation with role assignment
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password; // In a real application, this should be hashed
        this.email = "";
        this.firstName = "";
        this.middleName = "";
        this.lastName = "";
        this.preferredFirstName = "";
        this.roles = new ArrayList<>();
        this.roles.add(role); // Assign role upon creation
        this.oneTimePassword = false;
        this.passwordHash = null;
        this.oneTimePasswordExpiry = null;
    }

    // Full constructor for user creation with all details
    public User(String username, String password, String email, String firstName, String middleName, String lastName, String preferredFirstName) {
        this.username = username;
        this.password = password; // In a real application, this should be hashed
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.preferredFirstName = preferredFirstName;
        this.roles = new ArrayList<>();
        this.oneTimePassword = false;
        this.passwordHash = null;
        this.oneTimePasswordExpiry = null;
    }

    // Getters and Setters for user details
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password; // In a real application, do not expose plaintext passwords
    }

    public void setPassword(String password) {
        this.password = password; // Hash the password before saving in production
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPreferredFirstName() {
        return preferredFirstName;
    }

    public void setPreferredFirstName(String preferredFirstName) {
        this.preferredFirstName = preferredFirstName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public void removeRole(String role) {
        roles.remove(role);
    }

    public boolean isOneTimePassword() {
        return oneTimePassword;
    }

    public void setOneTimePassword(boolean oneTimePassword) {
        this.oneTimePassword = oneTimePassword;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getOneTimePasswordExpiry() {
        return oneTimePasswordExpiry;
    }

    public void setOneTimePasswordExpiry(LocalDateTime oneTimePasswordExpiry) {
        this.oneTimePasswordExpiry = oneTimePasswordExpiry;
    }

    // Method to return the full name of the user, using preferred first name if available
    public String getFullName() {
        if (preferredFirstName != null && !preferredFirstName.isEmpty()) {
            return preferredFirstName + " " + lastName;
        }
        return firstName + " " + lastName;
    }
}
