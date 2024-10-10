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
    private void showDeleteUserDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Delete User Account");

        GridPane deleteGrid = new GridPane();
        deleteGrid.setPadding(new Insets(10, 10, 10, 10));
        deleteGrid.setVgap(8);
        deleteGrid.setHgap(10);

        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 0);

        Button deleteButton = new Button("Delete User");
        GridPane.setConstraints(deleteButton, 1, 1);
        deleteButton.setOnAction(e -> {
            String username = usernameInput.getText();
            if (userAccounts.remove(username) != null) {
                System.out.println("User " + username + " deleted successfully.");
            } else {
                System.out.println("User not found.");
            }
            dialogStage.close();
        });

        deleteGrid.getChildren().addAll(usernameLabel, usernameInput, deleteButton);

        Scene dialogScene = new Scene(deleteGrid, 300, 150);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }
 // Student Home Page
    private void showStudentHomePage(Stage primaryStage) {
        Stage studentHomeStage = new Stage();
        studentHomeStage.setTitle("Student Home Page");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label welcomeLabel = new Label("Welcome, Student!");
        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> {
            studentHomeStage.close();
            start(primaryStage); // Redirect back to login after logout
        });

        layout.getChildren().addAll(welcomeLabel, logoutButton);

        Scene scene = new Scene(layout, 300, 200);
        studentHomeStage.setScene(scene);
        studentHomeStage.show();
    }

 // Method to show the user management page
    private void showManageUsersPage(Stage primaryStage) {
        Stage manageUsersStage = new Stage();
        manageUsersStage.setTitle("Manage Users");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label manageUsersLabel = new Label("Manage Users");

        // Create a ListView to display the users
        ListView<String> userListView = new ListView<>();

        // Populate the ListView with actual user data
        for (String username : userAccounts.keySet()) {
            User user = userAccounts.get(username);
            String roles = String.join(", ", user.getRoles()); // Joining roles with a comma
            userListView.getItems().add(username + " - Roles: " + roles);
        }

        // Add buttons for managing users
        Button addUserButton = new Button("Add User");
        Button deleteUserButton = new Button("Delete Selected User");
        Button editUserRoleButton = new Button("Edit Selected User Role");

        // Action for adding a user (open a dialog for this)
        addUserButton.setOnAction(e -> {
            showAddUserDialog(); // This method will handle adding a user
        });

        // Action for deleting a selected user
        deleteUserButton.setOnAction(e -> {
            String selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String username = selectedUser.split(" - ")[0]; // Extract username
                userAccounts.remove(username); // Remove user from the map
                userListView.getItems().remove(selectedUser); // Remove from the ListView
                System.out.println("Deleted user: " + username);
            } else {
                System.out.println("No user selected for deletion.");
            }
        });

        // Action for editing user roles
        editUserRoleButton.setOnAction(e -> {
            String selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String username = selectedUser.split(" - ")[0]; // Extract username
                showEditUserRoleDialog(username); // Open a dialog to edit roles
            } else {
                System.out.println("No user selected for role edit.");
            }
        });

        layout.getChildren().addAll(manageUsersLabel, userListView, addUserButton, deleteUserButton, editUserRoleButton);

        Scene scene = new Scene(layout, 400, 300);
        manageUsersStage.setScene(scene);
        manageUsersStage.show();
    }


    // Method to handle adding a user (simplified)
    private void showAddUserDialog() {
        Stage addUserStage = new Stage();
        addUserStage.setTitle("Add New User");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label nameLabel = new Label("Enter User Name:");
        TextField nameInput = new TextField();
        Label roleLabel = new Label("Select Role:");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Student", "Instructor", "Admin");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String userName = nameInput.getText();
            String role = roleComboBox.getValue();
            if (!userName.isEmpty() && role != null) {
                // Logic to add the user to the system
                System.out.println("Added user: " + userName + " with role: " + role);
                addUserStage.close();
            } else {
                System.out.println("User name or role not selected.");
            }
        });

        layout.getChildren().addAll(nameLabel, nameInput, roleLabel, roleComboBox, submitButton);

        Scene scene = new Scene(layout, 300, 200);
        addUserStage.setScene(scene);
        addUserStage.show();
    }

    // Method to handle editing a user role (simplified)
    private void showEditUserRoleDialog(String selectedUser) {
        Stage editUserRoleStage = new Stage();
        editUserRoleStage.setTitle("Edit User Roles");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label userLabel = new Label("Editing roles for: " + selectedUser);

        // Get the current user
        User user = userAccounts.get(selectedUser.split(" - ")[0]); // Extract username

        // Create checkboxes for role selection
        CheckBox studentCheckBox = new CheckBox("Student");
        CheckBox instructorCheckBox = new CheckBox("Instructor");
        CheckBox adminCheckBox = new CheckBox("Admin");

        // Mark current roles as checked
        studentCheckBox.setSelected(user.getRoles().contains("Student"));
        instructorCheckBox.setSelected(user.getRoles().contains("Instructor"));
        adminCheckBox.setSelected(user.getRoles().contains("Admin"));

        // Button to submit role changes
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            // Collect selected roles based on checkboxes
            List<String> selectedRoles = new ArrayList<>();
            if (studentCheckBox.isSelected()) {
                selectedRoles.add("Student");
            }
            if (instructorCheckBox.isSelected()) {
                selectedRoles.add("Instructor");
            }
            if (adminCheckBox.isSelected()) {
                selectedRoles.add("Admin");
            }

            // Update user's roles
            user.setRoles(selectedRoles);
            System.out.println("Updated roles for " + selectedUser + " to: " + selectedRoles);
            editUserRoleStage.close();
        });

        layout.getChildren().addAll(userLabel, studentCheckBox, instructorCheckBox, adminCheckBox, submitButton);

        Scene scene = new Scene(layout, 300, 200);
        editUserRoleStage.setScene(scene);
        editUserRoleStage.show();
    }




    // Instructor Home Page
    private void showInstructorHomePage(Stage primaryStage) {
        Stage instructorHomeStage = new Stage();
        instructorHomeStage.setTitle("Instructor Home Page");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label welcomeLabel = new Label("Welcome, Instructor!");
        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> {
            instructorHomeStage.close();
            start(primaryStage); // Redirect back to login after logout
        });

        layout.getChildren().addAll(welcomeLabel, logoutButton);

        Scene scene = new Scene(layout, 300, 200);
        instructorHomeStage.setScene(scene);
        instructorHomeStage.show();
    }


    // Admin Home Page (More options can be added here)
    private void showAdminHomePage(Stage primaryStage) {
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
        manageUsersButton.setOnAction(e -> showManageUsersPage(primaryStage)); 
        // Button to list user accounts
        Button listUsersButton = new Button("List User Accounts");
        GridPane.setConstraints(listUsersButton, 0, 4);
        listUsersButton.setOnAction(e -> listUsers());
     // Add Logout Button
        Button logoutButton = new Button("Logout");
        GridPane.setConstraints(logoutButton, 0, 5);
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



    private void handleLogin(User user, Stage primaryStage) {
        if (user.getRoles().size() == 1) {
            // If the user has only one role, redirect to the home page for that role
            redirectToRoleHomePage(user.getRoles().get(0), primaryStage);
        } else if (user.getRoles().size() > 1) {
            // If the user has multiple roles, prompt them to select a role for the session
            showRoleSelectionDialog(user, primaryStage);
        }
    }


    // Show a dialog to let the user select their role for the session
    private void showRoleSelectionDialog(User user, Stage primaryStage) {
        Stage roleDialogStage = new Stage();
        roleDialogStage.setTitle("Select Role");

        GridPane roleGrid = new GridPane();
        roleGrid.setPadding(new Insets(10, 10, 10, 10));
        roleGrid.setVgap(8);
        roleGrid.setHgap(10);

        Label roleLabel = new Label("Select Role:");
        GridPane.setConstraints(roleLabel, 0, 0);

        ComboBox<String> roleSelectionBox = new ComboBox<>();
        roleSelectionBox.getItems().addAll(user.getRoles());
        GridPane.setConstraints(roleSelectionBox, 1, 0);

        Button submitButton = new Button("Submit");
        GridPane.setConstraints(submitButton, 1, 1);
        submitButton.setOnAction(e -> {
            String selectedRole = roleSelectionBox.getValue();
            if (selectedRole != null) {
                redirectToRoleHomePage(selectedRole, primaryStage); // Pass primaryStage here
                roleDialogStage.close(); // Close the dialog after selection
            }
        });

        roleGrid.getChildren().addAll(roleLabel, roleSelectionBox, submitButton);

        Scene roleScene = new Scene(roleGrid, 300, 150);
        roleDialogStage.setScene(roleScene);
        roleDialogStage.show();
    }


    // Redirect to the appropriate home page based on the role
    private void redirectToRoleHomePage(String role, Stage primaryStage) {
        String normalizedRole = role.toUpperCase(); // Normalize the role

        switch (normalizedRole) {
            case "STUDENT":
                showStudentHomePage(primaryStage);
                break;
            case "INSTRUCTOR":
                showInstructorHomePage(primaryStage);
                break;
            case "ADMIN":
                showAdminHomePage(primaryStage);
                break;
            default:
                System.out.println("Unknown role: " + role);
        }
    }


    // List all user accounts
    private void listUsers() {
        System.out.println("Current Users:");
        for (String username : userAccounts.keySet()) {
            System.out.println(" - " + username + " with roles: " + userAccounts.get(username).getRoles());
        }
    }

    // Method to generate a reset token (simple example)
    private String generateResetToken() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

    // Method to show the role-specific page
    private void showRoleSpecificPage(Stage primaryStage, String role) {
        GridPane roleGrid = new GridPane();
        roleGrid.setPadding(new Insets(10, 10, 10, 10));
        roleGrid.setVgap(8);
        roleGrid.setHgap(10);

        Label rolePageLabel = new Label("Welcome to the " + role + " Page!");
        GridPane.setConstraints(rolePageLabel, 0, 0);

        // Add Logout Button
        Button logoutButton = new Button("Logout");
        GridPane.setConstraints(logoutButton, 0, 1);
        logoutButton.setOnAction(e -> {
            currentUser = null; // Clear the current user
            start(primaryStage); // Redirect back to the login scene
        });

        roleGrid.getChildren().addAll(rolePageLabel, logoutButton);
        Scene roleScene = new Scene(roleGrid, 400, 200);
        primaryStage.setScene(roleScene);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

