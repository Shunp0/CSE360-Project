package application;

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
    private boolean firstTimeSetup; // Flag to check if the user needs to finish setup

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
        this.firstTimeSetup = true; // Default to true for new users
    }
    
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.roles = new ArrayList<>();
        this.roles.add(role); // Add initial role
    }


    // Constructor with parameters
    public User(String username, String password, String email, String firstName, String middleName, String lastName, String preferredFirstName) {
        this.username = username;
        this.password = password; // In a real application, this should be hashed
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.preferredFirstName = preferredFirstName;
        this.roles = new ArrayList<>();
        this.firstTimeSetup = true; // Default to true for new users
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // In a real application, this should be hashed
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

    public boolean isFirstTimeSetup() {
        return firstTimeSetup;
    }

    public void setFirstTimeSetup(boolean firstTimeSetup) {
        this.firstTimeSetup = firstTimeSetup;
    }

    public String getFullName() {
        return (preferredFirstName != null && !preferredFirstName.isEmpty() ? preferredFirstName : firstName) + " " +
               (middleName != null && !middleName.isEmpty() ? middleName + " " : "") + lastName;
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }
}
