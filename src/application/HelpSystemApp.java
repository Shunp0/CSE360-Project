package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.VBox;

public class HelpSystemApp extends Application {

    private Map<String, User> userAccounts = new HashMap<>(); // Stores users by username
    private Map<String, String> invitationCodes = new HashMap<>(); // Stores codes
    private boolean isFirstUser = true; // Track if the first user is being created
    private User currentUser; // Store current logged-in user

    // Maps for reset tokens and expiration
    private Map<String, String> resetTokens = new HashMap<>(); // Username -> One-time password for reset
    private Map<String, LocalDateTime> resetExpiration = new HashMap<>(); // Username -> Expiration time of the reset password
    

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CSE 360 Help System");

        // Initial Login/Registration Scene
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Username
        Label userLabel = new Label("Username:");
        GridPane.setConstraints(userLabel, 0, 0);
        TextField userInput = new TextField();
        GridPane.setConstraints(userInput, 1, 0);

        // Password
        Label passLabel = new Label("Password:");
        GridPane.setConstraints(passLabel, 0, 1);
        PasswordField passInput = new PasswordField();
        GridPane.setConstraints(passInput, 1, 1);

        // Confirm Password
        Label confirmPassLabel = new Label("Confirm Password:");
        GridPane.setConstraints(confirmPassLabel, 0, 2);
        PasswordField confirmPasswordInput = new PasswordField();
        GridPane.setConstraints(confirmPasswordInput, 1, 2);

        // Invitation Code
        Label inviteLabel = new Label("Invitation Code:");
        GridPane.setConstraints(inviteLabel, 0, 3);
        TextField inviteInput = new TextField();
        GridPane.setConstraints(inviteInput, 1, 3);

        // Login Button
        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 4);
        loginButton.setOnAction(e -> {
            String username = userInput.getText();
            String password = passInput.getText();
            // Call login logic here
            User user = userAccounts.get(username);
            if (user != null && user.getPassword().equals(password)) {
                System.out.println("Login successful!");
                currentUser = user;

                if (currentUser.getRoles().contains("ADMIN")) {
                    showAdminDashboard(primaryStage); // Show Admin Dashboard
                } else {
                    // Check if account setup is completed
                    if (!currentUser.isAccountSetupCompleted()) {
                        showAccountSetup(primaryStage); // Show account setup only if not completed
                    } else {
                        // Redirect to the user's home page if account setup is completed
                    	handleLogin(currentUser, primaryStage); // Assuming at least one role exists
                    }
                }
            } else {
                System.out.println("Invalid username or password.");
            }

        });

        // Register Button
        Button registerButton = new Button("Register");
        GridPane.setConstraints(registerButton, 1, 5);
        registerButton.setOnAction(e -> {
            String username = userInput.getText();
            String password = passInput.getText();

            // Check if this is the first user
            if (isFirstUser) {
                if (password.equals(confirmPasswordInput.getText())) {
                    User firstUser = new User(username, password, "ADMIN"); // Assign Admin role
                    userAccounts.put(username, firstUser); // Store the new user
                    System.out.println("First user registered successfully as Admin!");
                    isFirstUser = false; // Set flag to false after first registration
                } else {
                    System.out.println("Passwords do not match.");
                }
            } else {
                // Regular registration process
                String invitationCode = inviteInput.getText();
                if (invitationCodes.containsKey(invitationCode)) {
                    if (password.equals(confirmPasswordInput.getText())) {
                        String role = invitationCodes.get(invitationCode); // Get the role associated with the code
                        User newUser = new User(username, password, role);
                        userAccounts.put(username, newUser); // Store the new user
                        System.out.println("User registered successfully with role: " + role);
                    } else {
                        System.out.println("Passwords do not match.");
                    }
                } else {
                    System.out.println("Invalid invitation code.");
                }
            }
        });

        // Add everything to the grid
        grid.getChildren().addAll(userLabel, userInput, passLabel, passInput, confirmPassLabel, confirmPasswordInput, inviteLabel, inviteInput, loginButton, registerButton);

        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Scene for "Finish setting up your account"
    private void showAccountSetup(Stage primaryStage) {
        GridPane setupGrid = new GridPane();
        setupGrid.setPadding(new Insets(10, 10, 10, 10));
        setupGrid.setVgap(8);
        setupGrid.setHgap(10);

        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 0, 0);
        TextField emailInput = new TextField();
        GridPane.setConstraints(emailInput, 1, 0);

        Label firstNameLabel = new Label("First Name:");
        GridPane.setConstraints(firstNameLabel, 0, 1);
        TextField firstNameInput = new TextField();
        GridPane.setConstraints(firstNameInput, 1, 1);

        Label middleNameLabel = new Label("Middle Name:");
        GridPane.setConstraints(middleNameLabel, 0, 2);
        TextField middleNameInput = new TextField();
        GridPane.setConstraints(middleNameInput, 1, 2);

        Label lastNameLabel = new Label("Last Name:");
        GridPane.setConstraints(lastNameLabel, 0, 3);
        TextField lastNameInput = new TextField();
        GridPane.setConstraints(lastNameInput, 1, 3);

        Label preferredNameLabel = new Label("Preferred First Name (optional):");
        GridPane.setConstraints(preferredNameLabel, 0, 4);
        TextField preferredNameInput = new TextField();
        GridPane.setConstraints(preferredNameInput, 1, 4);

        Button finishButton = new Button("Finish Setup");
        GridPane.setConstraints(finishButton, 1, 5);
        finishButton.setOnAction(e -> {
            currentUser.setEmail(emailInput.getText());
            currentUser.setFirstName(firstNameInput.getText());
            currentUser.setMiddleName(middleNameInput.getText());
            currentUser.setLastName(lastNameInput.getText());
            currentUser.setPreferredFirstName(preferredNameInput.getText());
            System.out.println("Account setup completed!");

            // After setup, check roles and redirect accordingly
            currentUser.setAccountSetupCompleted(true);
            showRoleSelection(primaryStage);
        });

        setupGrid.getChildren().addAll(emailLabel, emailInput, firstNameLabel, firstNameInput, middleNameLabel, middleNameInput, lastNameLabel, lastNameInput, preferredNameLabel, preferredNameInput, finishButton);
        Scene setupScene = new Scene(setupGrid, 400, 300);
        primaryStage.setScene(setupScene);
    }

    // Scene for Role Selection
    private void showRoleSelection(Stage primaryStage) {
        if (currentUser.getRoles().size() == 1) {
            String selectedRole = currentUser.getRoles().get(0);
            System.out.println("Redirecting to role-specific page for role: " + selectedRole);
            // Logic to redirect user based on the single role
            showRoleSpecificPage(primaryStage, selectedRole);
        } else {
            GridPane roleGrid = new GridPane();
            roleGrid.setPadding(new Insets(10, 10, 10, 10));
            roleGrid.setVgap(8);
            roleGrid.setHgap(10);

            Label roleLabel = new Label("Select Role for this session:");
            GridPane.setConstraints(roleLabel, 0, 0);
            ComboBox<String> roleSelect = new ComboBox<>();
            roleSelect.getItems().addAll(currentUser.getRoles());
            GridPane.setConstraints(roleSelect, 1, 0);

            Button proceedButton = new Button("Proceed");
            GridPane.setConstraints(proceedButton, 1, 1);
            proceedButton.setOnAction(e -> {
                String selectedRole = roleSelect.getValue();
                System.out.println("User selected role: " + selectedRole);
                // Redirect to role-specific page
                showRoleSpecificPage(primaryStage, selectedRole);
            });

            roleGrid.getChildren().addAll(roleLabel, roleSelect, proceedButton);
            Scene roleScene = new Scene(roleGrid, 400, 200);
            primaryStage.setScene(roleScene);
        }
    }

    // Admin Dashboard to manage users
    private void showAdminDashboard(Stage primaryStage) {
        GridPane adminGrid = new GridPane();
        adminGrid.setPadding(new Insets(10, 10, 10, 10));
        adminGrid.setVgap(8);
        adminGrid.setHgap(10);

        Label welcomeLabel = new Label("Admin Dashboard");
        GridPane.setConstraints(welcomeLabel, 0, 0);

        // Button to invite new users
        Button inviteUserButton = new Button("Invite New User");
        GridPane.setConstraints(inviteUserButton, 0, 1);
        inviteUserButton.setOnAction(e -> showInviteUserDialog());

        // Button to reset user account
        Button resetUserButton = new Button("Reset User Account");
        GridPane.setConstraints(resetUserButton, 0, 2);
        resetUserButton.setOnAction(e -> showResetUserDialog());

        // Button to delete user account
        Button deleteUserButton = new Button("Delete User Account");
        GridPane.setConstraints(deleteUserButton, 0, 3);
        deleteUserButton.setOnAction(e -> showDeleteUserDialog());
        
        Button manageUsersButton = new Button("Manage Users");
        GridPane.setConstraints(manageUsersButton, 0, 5);
        manageUsersButton.setOnAction(e -> showManageUsersPage(primaryStage)); 
        // Button to list user accounts
        Button listUsersButton = new Button("List User Accounts");
        GridPane.setConstraints(listUsersButton, 0, 4);
        listUsersButton.setOnAction(e -> listUsers());
     // Add Logout Button
        Button logoutButton = new Button("Logout");
        GridPane.setConstraints(logoutButton, 0, 6);
        logoutButton.setOnAction(e -> {
            currentUser = null; // Clear the current user
            start(primaryStage); // Redirect back to the login scene
        });

        adminGrid.getChildren().add(logoutButton); // Add the logout button to the grid


        adminGrid.getChildren().addAll(welcomeLabel, inviteUserButton, resetUserButton, deleteUserButton, listUsersButton,manageUsersButton);

        Scene adminScene = new Scene(adminGrid, 400, 300);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Home Page");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        logoutButton.setOnAction(e -> {
            primaryStage.close();
            start(primaryStage); // Redirect back to login after logout
        });
    }

    // Show dialog for inviting a new user
 // Show dialog for inviting a new user
    private void showInviteUserDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Generate Invitation Code");

        GridPane inviteGrid = new GridPane();
        inviteGrid.setPadding(new Insets(10, 10, 10, 10));
        inviteGrid.setVgap(8);
        inviteGrid.setHgap(10);

        Label roleLabel = new Label("Role (ADMIN, STUDENT, INSTRUCTOR):");
        GridPane.setConstraints(roleLabel, 0, 0);
        TextField roleInput = new TextField();
        GridPane.setConstraints(roleInput, 1, 0);

        Label inviteCodeLabel = new Label("Generated Code:");
        GridPane.setConstraints(inviteCodeLabel, 0, 1);
        TextField inviteCodeOutput = new TextField();
        inviteCodeOutput.setEditable(false); // Prevents editing
        GridPane.setConstraints(inviteCodeOutput, 1, 1);

        Button generateButton = new Button("Generate Code");
        GridPane.setConstraints(generateButton, 1, 2);
        generateButton.setOnAction(e -> {
            String role = roleInput.getText().toUpperCase();
            if (!role.equals("ADMIN") && !role.equals("STUDENT") && !role.equals("INSTRUCTOR")) {
                System.out.println("Invalid role entered.");
                return;
            }

            // Generate a unique invitation code
            String invitationCode = generateResetToken();
            invitationCodes.put(invitationCode, role); // Store the code with the associated role
            inviteCodeOutput.setText(invitationCode); // Display the generated code
            System.out.println("Generated invitation code: " + invitationCode + " for role: " + role);
        });

        inviteGrid.getChildren().addAll(roleLabel, roleInput, inviteCodeLabel, inviteCodeOutput, generateButton);

        Scene dialogScene = new Scene(inviteGrid, 400, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }


    // Show dialog for resetting a user account
    private void showResetUserDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Reset User Account");

        GridPane resetGrid = new GridPane();
        resetGrid.setPadding(new Insets(10, 10, 10, 10));
        resetGrid.setVgap(8);
        resetGrid.setHgap(10);

        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 0);

        Button resetButton = new Button("Reset Password");
        GridPane.setConstraints(resetButton, 1, 1);
        resetButton.setOnAction(e -> {
            String username = usernameInput.getText();
            User user = userAccounts.get(username);
            if (user != null) {
                String resetToken = generateResetToken(); // Create a token
                resetTokens.put(username, resetToken);
                resetExpiration.put(username, LocalDateTime.now().plusMinutes(10)); // Token expires in 10 minutes
                System.out.println("Reset token generated for " + username + ": " + resetToken);
            } else {
                System.out.println("User not found.");
            }
            dialogStage.close();
        });

        resetGrid.getChildren().addAll(usernameLabel, usernameInput, resetButton);

        Scene dialogScene = new Scene(resetGrid, 300, 150);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

// Show dialog for deleting a user account
roleGrid.getChildren().addAll(roleLabel, roleSelectionBox, submitButton);

Scene roleScene = new Scene(roleGrid, 300, 150);
roleDialogStage.setScene(roleScene);
roleDialogStage.show();
}

// Redirect to home page based on role
private void redirectToRoleHomePage(String role, Stage primaryStage) {
    String normalizedRole = role.toUpperCase(); // Normalize role string

    switch (normalizedRole) {
        case "STUDENT":
            showStudentHomePage(primaryStage); // Show student page
            break;
        case "INSTRUCTOR":
            showInstructorHomePage(primaryStage); // Show instructor page
            break;
        case "ADMIN":
            showAdminHomePage(primaryStage); // Show admin page
            break;
        default:
            System.out.println("Unknown role: " + role); // If all else fails
    }
}

// List all user accounts
private void listUsers() {
    System.out.println("Current Users:"); // Print user accounts
    for (String username : userAccounts.keySet()) {
        System.out.println(" - " + username + " with roles: " + userAccounts.get(username).getRoles());
    }
}

// Generate a reset token (random hex)
private String generateResetToken() {
    return Long.toHexString(Double.doubleToLongBits(Math.random()));
}

// Show the role's specific page
private void showRoleSpecificPage(Stage primaryStage, String role) {
    GridPane roleGrid = new GridPane();
    roleGrid.setPadding(new Insets(10, 10, 10, 10)); // Set padding
    roleGrid.setVgap(8);
    roleGrid.setHgap(10);

    Label rolePageLabel = new Label("Welcome to the " + role + " Page!"); // Show role page label
    GridPane.setConstraints(rolePageLabel, 0, 0);

    // Logout Button
    Button logoutButton = new Button("Logout");
    GridPane.setConstraints(logoutButton, 0, 1);
    logoutButton.setOnAction(e -> {
        currentUser = null; // Clear current user on logout
        start(primaryStage); // Go back to login
    });

    roleGrid.getChildren().addAll(rolePageLabel, logoutButton); // Add elements to grid
    Scene roleScene = new Scene(roleGrid, 400, 200);
    primaryStage.setScene(roleScene); // Display the scene
}

public static void main(String[] args) {
    launch(args); // Start!
}


