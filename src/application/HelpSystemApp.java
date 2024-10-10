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

    private Map<String, User> userAccounts = new HashMap<>(); // Keeping track of users
    private Map<String, String> invitationCodes = new HashMap<>(); // Store invite codes
    private boolean isFirstUser = true; // Is first user
    private User currentUser; // Whoever's logged in right now

    // For handling password reset tokens and expiration times
    private Map<String, String> resetTokens = new HashMap<>(); // Token map for resets
    private Map<String, LocalDateTime> resetExpiration = new HashMap<>(); // Expiration for tokens

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CSE 360 Help System"); // Set window title

        // Initial Login/Registration Scene setup
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10)); // Add some padding so things don't look crammed
        grid.setVgap(8);
        grid.setHgap(10);

        // Username input
        Label userLabel = new Label("Username:");
        GridPane.setConstraints(userLabel, 0, 0); // Place the label at the top left
        TextField userInput = new TextField(); // Input field for the username
        GridPane.setConstraints(userInput, 1, 0); // Position the input field next to the label

        // Password input
        Label passLabel = new Label("Password:");
        GridPane.setConstraints(passLabel, 0, 1); // Same thing for password
        PasswordField passInput = new PasswordField(); // Hides the password input
        GridPane.setConstraints(passInput, 1, 1);

        // Confirm password input
        Label confirmPassLabel = new Label("Confirm Password:");
        GridPane.setConstraints(confirmPassLabel, 0, 2); // Asking to confirm password
        PasswordField confirmPasswordInput = new PasswordField(); // Another confirmation
        GridPane.setConstraints(confirmPasswordInput, 1, 2);

        // Invitation Code input
        Label inviteLabel = new Label("Invitation Code:");
        GridPane.setConstraints(inviteLabel, 0, 3); // Users need an invite code
        TextField inviteInput = new TextField(); // Input field for the code
        GridPane.setConstraints(inviteInput, 1, 3); // Place it next to the label

        // Login button setup
Button loginButton = new Button("Login");
loginButton.setOnAction(e -> {
    String username = userInput.getText();
    String password = passInput.getText();

    // Basic check for valid username/password
    if (userAccounts.containsKey(username) && userAccounts.get(username).getPassword().equals(password)) {
        currentUser = userAccounts.get(username); // Log in the user
        System.out.println("Login successful!");
        redirectToRoleHomePage(currentUser.getRole(), primaryStage); // Send them to their role's homepage
    } else {
        System.out.println("Invalid username or password."); // Handle invalid login
    }
});

// Register button setup
Button registerButton = new Button("Register");
GridPane.setConstraints(registerButton, 1, 5); // Place the register button
registerButton.setOnAction(e -> {
    String username = userInput.getText(); // Grab the username
    String password = passInput.getText(); // Grab the password

        // If this is the first user, make them an admin
        if (isFirstUser) {
            if (password.equals(confirmPasswordInput.getText())) {
                User firstUser = new User(username, password, "ADMIN"); // Make the first user an admin
                userAccounts.put(username, firstUser); // Save the user
                System.out.println("First user registered successfully as Admin!");
                isFirstUser = false; // No more "first user" after this
            } else {
                System.out.println("Passwords do not match."); // Handle password mismatch
            }
        } else {
            // Regular registration process after the first user
            String invitationCode = inviteInput.getText(); // Get the entered invite code
            if (invitationCodes.containsKey(invitationCode)) { // Check if it's valid
                if (password.equals(confirmPasswordInput.getText())) {
                    String role = invitationCodes.get(invitationCode); // Get the role tied to the code
                    User newUser = new User(username, password, role); // Create the user with that role
                    userAccounts.put(username, newUser); // Save the user
                    System.out.println("User registered successfully with role: " + role);
                } else {
                System.out.println("Passwords do not match."); // Again, handle password mismatch
                }
            } else {
            System.out.println("Invalid invitation code."); // Handle invalid invite code
            }
        }
    });


//
        // Add everything to the grid (labels, inputs, buttons)
grid.getChildren().addAll(userLabel, userInput, passLabel, passInput, confirmPassLabel, confirmPasswordInput, inviteLabel, inviteInput, loginButton, registerButton);

// Set up the scene and show it
Scene scene = new Scene(grid, 400, 250); // 400x250 window size
primaryStage.setScene(scene); // Put the scene on the stage
primaryStage.show(); // Finally, show the stage
}

// Scene for "Finish setting up your account"
private void showAccountSetup(Stage primaryStage) {
    GridPane setupGrid = new GridPane();
    setupGrid.setPadding(new Insets(10, 10, 10, 10)); // Add some padding for breathing room
    setupGrid.setVgap(8); // Vertical gap between rows
    setupGrid.setHgap(10); // Horizontal gap between columns

    // Email input field
    Label emailLabel = new Label("Email:");
    GridPane.setConstraints(emailLabel, 0, 0); // Position the label
    TextField emailInput = new TextField(); // Input field for the email
    GridPane.setConstraints(emailInput, 1, 0); // Position the input field next to the label

    // First name input
    Label firstNameLabel = new Label("First Name:");
    GridPane.setConstraints(firstNameLabel, 0, 1); // Position the label for first name
    TextField firstNameInput = new TextField(); // Input field for first name
    GridPane.setConstraints(firstNameInput, 1, 1);

    // Middle name input (optional)
    Label middleNameLabel = new Label("Middle Name:");
    GridPane.setConstraints(middleNameLabel, 0, 2);
    TextField middleNameInput = new TextField();
    GridPane.setConstraints(middleNameInput, 1, 2);

    // Last name input
    Label lastNameLabel = new Label("Last Name:");
    GridPane.setConstraints(lastNameLabel, 0, 3); // Same pattern for last name
    TextField lastNameInput = new TextField();
    GridPane.setConstraints(lastNameInput, 1, 3);

    // Preferred name input (optional)
    Label preferredNameLabel = new Label("Preferred First Name (optional):");
    GridPane.setConstraints(preferredNameLabel, 0, 4);
    TextField preferredNameInput = new TextField();
    GridPane.setConstraints(preferredNameInput, 1, 4);

    // Finish button to complete the setup
    Button finishButton = new Button("Finish Setup");
    GridPane.setConstraints(finishButton, 1, 5); // Place the button
    finishButton.setOnAction(e -> {
        // Update the current user info with the inputs
        currentUser.setEmail(emailInput.getText());
        currentUser.setFirstName(firstNameInput.getText());
        currentUser.setMiddleName(middleNameInput.getText());
        currentUser.setLastName(lastNameInput.getText());
        currentUser.setPreferredFirstName(preferredNameInput.getText());
        System.out.println("Account setup completed!");

        // Mark the setup as done and redirect to the role selection screen
        currentUser.setAccountSetupCompleted(true);
        showRoleSelection(primaryStage); // After setup, go to role selection
    });

    // Add everything to the grid
    setupGrid.getChildren().addAll(emailLabel, emailInput, firstNameLabel, firstNameInput, middleNameLabel, middleNameInput, lastNameLabel, lastNameInput, preferredNameLabel, preferredNameInput, finishButton);

    // Set up the scene and display it
    Scene setupScene = new Scene(setupGrid, 400, 300); // Slightly larger window here
    primaryStage.setScene(setupScene); // Show the setup scene
}


    // Scene for Role Selection
private void showRoleSelection(Stage primaryStage) {
    // If the user has only one role, no need to ask—just redirect
    if (currentUser.getRoles().size() == 1) {
        String selectedRole = currentUser.getRoles().get(0); // Get the single role
        System.out.println("Redirecting to role-specific page for role: " + selectedRole);
        
        // Send them directly to their role-specific page
        showRoleSpecificPage(primaryStage, selectedRole);
    } else {
        // If they have multiple roles, let them choose which one to use
        GridPane roleGrid = new GridPane();
        roleGrid.setPadding(new Insets(10, 10, 10, 10)); // Add padding around the grid
        roleGrid.setVgap(8); // Space between rows
        roleGrid.setHgap(10); // Space between columns

        // Label to instruct the user to select a role
        Label roleLabel = new Label("Select Role for this session:");
        GridPane.setConstraints(roleLabel, 0, 0); // Position the label

        // Dropdown for selecting one of their available roles
        ComboBox<String> roleSelect = new ComboBox<>();
        roleSelect.getItems().addAll(currentUser.getRoles()); // Fill it with user's roles
        GridPane.setConstraints(roleSelect, 1, 0); // Position the dropdown

        // Button to proceed after role selection
        Button proceedButton = new Button("Proceed");
        GridPane.setConstraints(proceedButton, 1, 1); // Position the button
        proceedButton.setOnAction(e -> {
            String selectedRole = roleSelect.getValue(); // Get the selected role
            System.out.println("User selected role: " + selectedRole);

            // Redirect them to the appropriate page based on their choice
            showRoleSpecificPage(primaryStage, selectedRole);
        });

        // Add everything to the grid (label, dropdown, button)
        roleGrid.getChildren().addAll(roleLabel, roleSelect, proceedButton);

        // Set up the scene for role selection and display it
        Scene roleScene = new Scene(roleGrid, 400, 200); // Set scene size
        primaryStage.setScene(roleScene); // Display the role selection scene
    }
}
    
    // Admin Dashboard to manage users
private void showAdminDashboard(Stage primaryStage) {
    GridPane adminGrid = new GridPane();
    adminGrid.setPadding(new Insets(10, 10, 10, 10)); // Standard padding
    adminGrid.setVgap(8); // Vertical spacing
    adminGrid.setHgap(10); // Horizontal spacing

    // Welcome label for the Admin Dashboard
    Label welcomeLabel = new Label("Admin Dashboard");
    GridPane.setConstraints(welcomeLabel, 0, 0); // Place at the top

    // Button to invite new users
    Button inviteUserButton = new Button("Invite New User");
    GridPane.setConstraints(inviteUserButton, 0, 1); // Position the invite button
    inviteUserButton.setOnAction(e -> showInviteUserDialog()); // Show invite user dialog when clicked

    // Button to reset user accounts
    Button resetUserButton = new Button("Reset User Account");
    GridPane.setConstraints(resetUserButton, 0, 2); // Position reset
    resetUserButton.setOnAction(e -> showResetUserDialog()); // Show reset user dialog

    // Button to delete user accounts
    Button deleteUserButton = new Button("Delete User Account");
    GridPane.setConstraints(deleteUserButton, 0, 3); // Position delete
    deleteUserButton.setOnAction(e -> showDeleteUserDialog()); // Show delete user dialog

    // Button to list all user accounts
    Button listUsersButton = new Button("List User Accounts");
    GridPane.setConstraints(listUsersButton, 0, 4); // Position list users 
    listUsersButton.setOnAction(e -> listUsers()); // List users when clicked

    // Button to manage users in more detail
    Button manageUsersButton = new Button("Manage Users");
    GridPane.setConstraints(manageUsersButton, 0, 5); // Position the manage users button
    manageUsersButton.setOnAction(e -> showManageUsersPage(primaryStage)); // Go to manage users page

    // Logout button to exit the admin session
    Button logoutButton = new Button("Logout");
    GridPane.setConstraints(logoutButton, 0, 6); // Position the logout button
    logoutButton.setOnAction(e -> {
        currentUser = null; // Clear the current user when logging out
        start(primaryStage); // Redirect back to login scene
    });

    // Add the logout button and other elements to the grid
    adminGrid.getChildren().addAll(welcomeLabel, inviteUserButton, resetUserButton, deleteUserButton, listUsersButton, manageUsersButton, logoutButton);

    // Set up the scene with the grid and display it
    Scene adminScene = new Scene(adminGrid, 400, 300); // Create the admin scene
    primaryStage.setScene(adminScene); // Set the scene
    primaryStage.setTitle("Admin Home Page"); // Title for the admin page

    // Extra VBox layout for padding
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20, 20, 20, 20)); // Extra padding

    // Logout logic that also closes the stage
    logoutButton.setOnAction(e -> {
        primaryStage.close(); // Close the current window
        start(primaryStage); // Redirect to login screen again
    });
}

    // Show dialog for inviting a new user
private void showInviteUserDialog() {
    Stage dialogStage = new Stage();
    dialogStage.setTitle("Generate Invitation Code"); // Set the title for the dialog

    // Create a grid layout for the invite dialog
    GridPane inviteGrid = new GridPane();
    inviteGrid.setPadding(new Insets(10, 10, 10, 10)); // Add padding
    inviteGrid.setVgap(8); // Vertical spacing
    inviteGrid.setHgap(10); // Horizontal spacing

    // Label and input for entering a role
    Label roleLabel = new Label("Role (ADMIN, STUDENT, INSTRUCTOR):");
    GridPane.setConstraints(roleLabel, 0, 0); // Place the label at the top left
    TextField roleInput = new TextField(); // Input for the role
    GridPane.setConstraints(roleInput, 1, 0); // Position the input next to the label

    // Label and output field for showing the generated invitation code
    Label inviteCodeLabel = new Label("Generated Code:");
    GridPane.setConstraints(inviteCodeLabel, 0, 1); // Position the code label
    TextField inviteCodeOutput = new TextField(); // Output field for showing the code
    inviteCodeOutput.setEditable(false); // Make it read only
    GridPane.setConstraints(inviteCodeOutput, 1, 1); // Position the output field
    
    // Button to generate the code
    Button generateButton = new Button("Generate Code");
    GridPane.setConstraints(generateButton, 1, 2); // Position the generate button
    generateButton.setOnAction(e -> {
        String role = roleInput.getText().toUpperCase(); // Get the role input and normalize it
        if (!role.equals("ADMIN") && !role.equals("STUDENT") && !role.equals("INSTRUCTOR")) {
            System.out.println("Invalid role entered."); // Handle invalid role input
                return; // Exit the method if the role is invalid
            }
        // Generate a unique invitation code
        String invitationCode = generateResetToken(); // Use the token generator method
        invitationCodes.put(invitationCode, role); // Store the code with the role
        inviteCodeOutput.setText(invitationCode); // Display the generated code
        System.out.println("Generated invitation code: " + invitationCode + " for role: " + role);
    });
    // Add all elements to the grid
    inviteGrid.getChildren().addAll(roleLabel, roleInput, inviteCodeLabel, inviteCodeOutput, generateButton);
    // Set up the scene and show the dialog
    Scene dialogScene = new Scene(inviteGrid, 400, 200); // Create a scene for the invite dialog
    dialogStage.setScene(dialogScene); // Set the scene on the dialog stage
    dialogStage.show(); // Show the invite dialog
}
    // Show dialog for resetting a user account
private void showResetUserDialog() {
    Stage dialogStage = new Stage();
    dialogStage.setTitle("Reset User Account"); // Set the title for the dialog
    // Create a grid for the reset dialog
    GridPane resetGrid = new GridPane();
    resetGrid.setPadding(new Insets(10, 10, 10, 10)); // Padding around the grid
    resetGrid.setVgap(8); // Space between rows
    resetGrid.setHgap(10); // Space between columns
    
    // Label and input field for the username
    Label usernameLabel = new Label("Username:");
    GridPane.setConstraints(usernameLabel, 0, 0); // Position the label
    TextField usernameInput = new TextField(); // Input field for the username
    GridPane.setConstraints(usernameInput, 1, 0); // Position the input next to the label
    
    // Button to reset the user's password
    Button resetButton = new Button("Reset Password");
    GridPane.setConstraints(resetButton, 1, 1); // Position the reset button
    resetButton.setOnAction(e -> {
        String username = usernameInput.getText(); // Get the entered username
        User user = userAccounts.get(username); // Check if the user exists
            if (user != null) {
            // Generate a reset token and set expiration for 10 minutes
                String resetToken = generateResetToken(); // Generate the token
                resetTokens.put(username, resetToken); // Store the token for the user
                resetExpiration.put(username, LocalDateTime.now().plusMinutes(10)); // Token valid for 10 minutes
                System.out.println("Reset token generated for " + username + ": " + resetToken); // Display the token
            } else {
            // If the user isn't found, print an error message
            System.out.println("User not found.");
        }

        dialogStage.close(); // Close the dialog when done
    });
    // Add all the components to the grid
    resetGrid.getChildren().addAll(usernameLabel, usernameInput, resetButton);
    // Set up the scene for the reset dialog
    Scene dialogScene = new Scene(resetGrid, 300, 150); // Set size for the dialog
    dialogStage.setScene(dialogScene); // Display the scene
    dialogStage.show(); // Show the dialog
}
    // Show dialog for deleting a user account
private void showDeleteUserDialog() {
    Stage dialogStage = new Stage();
    dialogStage.setTitle("Delete User Account"); // Set the title 

    // Set up a grid layout for the delete dialog
    GridPane deleteGrid = new GridPane();
    deleteGrid.setPadding(new Insets(10, 10, 10, 10)); // Padding
    deleteGrid.setVgap(8); // Vertical space
    deleteGrid.setHgap(10); // Horizontal space

    // Label and input field for the username
    Label usernameLabel = new Label("Username:");
    GridPane.setConstraints(usernameLabel, 0, 0); // Place label at the top left
    TextField usernameInput = new TextField(); // Input for the username
    GridPane.setConstraints(usernameInput, 1, 0); // Place input next to the label

    // Button to delete the user
    Button deleteButton = new Button("Delete User");
    GridPane.setConstraints(deleteButton, 1, 1); // Place the delete button
    deleteButton.setOnAction(e -> {
        String username = usernameInput.getText(); // Get the username entered

        // Try to remove the user, print success or failure
        if (userAccounts.remove(username) != null) {
            System.out.println("User " + username + " deleted successfully."); // User found and deleted
        } else {
            System.out.println("User not found."); // No user with that username
        }

        dialogStage.close(); // Close the dialog when done
    });

    // Add all pieces to the grid
    deleteGrid.getChildren().addAll(usernameLabel, usernameInput, deleteButton);

    // Set up and show the dialog scene
    Scene dialogScene = new Scene(deleteGrid, 300, 150); // Set size of the dialog
    dialogStage.setScene(dialogScene); // Display the scene
    dialogStage.show(); // Show the dialog
}

// Student Home Page
private void showStudentHomePage(Stage primaryStage) {
    Stage studentHomeStage = new Stage();
    studentHomeStage.setTitle("Student Home Page"); // Title for the student page

    // Set up the layout for the student home page
    VBox layout = new VBox(10); // Vertical box with spacing
    layout.setPadding(new Insets(20, 20, 20, 20)); // Padding around the edges

    // Welcome label and logout button
    Label welcomeLabel = new Label("Welcome, Student!");
    Button logoutButton = new Button("Log Out"); // Logout button
    logoutButton.setOnAction(e -> {
        studentHomeStage.close(); // Close the student page
        start(primaryStage); // Redirect back to login after logout
    });

    // Add the label and button to the layout
    layout.getChildren().addAll(welcomeLabel, logoutButton);

    // Set up and show the scene
    Scene scene = new Scene(layout, 300, 200); // Set size for the student home page
    studentHomeStage.setScene(scene); // Set the scene on the stage
    studentHomeStage.show(); // Show the student home page
}
 // Method to show the user management page
private void showManageUsersPage(Stage primaryStage) {
    Stage manageUsersStage = new Stage();
    manageUsersStage.setTitle("Manage Users"); // Set title for the window

    VBox layout = new VBox(10); // Vertical layout with spacing
    layout.setPadding(new Insets(20, 20, 20, 20)); // Padding for breathing room

    Label manageUsersLabel = new Label("Manage Users"); // Label for the top of the page

    // Create a ListView to display the users
    ListView<String> userListView = new ListView<>();

    // Populate the ListView with user data
    for (String username : userAccounts.keySet()) {
        User user = userAccounts.get(username);
        String roles = String.join(", ", user.getRoles()); // Join roles with a comma
        userListView.getItems().add(username + " - Roles: " + roles); // Add to the ListView
    }

    // Buttons for managing users
    Button addUserButton = new Button("Add User");
    Button deleteUserButton = new Button("Delete Selected User");
    Button editUserRoleButton = new Button("Edit Selected User Role");

    // Action for adding a user
    addUserButton.setOnAction(e -> {
        showAddUserDialog(); // Open dialog to add a user
    });

    // Action for deleting a selected user
    deleteUserButton.setOnAction(e -> {
        String selectedUser = userListView.getSelectionModel().getSelectedItem(); // Get selected user
        if (selectedUser != null) {
            String username = selectedUser.split(" - ")[0]; // Extract the username
            userAccounts.remove(username); // Remove user
            userListView.getItems().remove(selectedUser); // Remove from ListView
            System.out.println("Deleted user: " + username);
        } else {
            System.out.println("No user selected for deletion."); // Handle no selection
        }
    });

    // Action for editing user roles
    editUserRoleButton.setOnAction(e -> {
        String selectedUser = userListView.getSelectionModel().getSelectedItem(); // Get selected user
        if (selectedUser != null) {
            String username = selectedUser.split(" - ")[0]; // Username
            showEditUserRoleDialog(username); // Open dialog to edit user roles
        } else {
            System.out.println("No user selected for role edit."); // Handle no selection
        }
    });
    // Add everything to the layout
    layout.getChildren().addAll(manageUsersLabel, userListView, addUserButton, deleteUserButton, editUserRoleButton);
    // Set up the scene and display it
    Scene scene = new Scene(layout, 400, 300); // Set size
    manageUsersStage.setScene(scene); // Set the scene on the stage
    manageUsersStage.show(); // Show stage
}

    // Method to handle adding a user (simplified)
    Label userLabel = new Label("Editing roles for: " + selectedUser); // Display user being edited

    // Get the current user object
    User user = userAccounts.get(selectedUser.split(" - ")[0]); // Extract username from the string

    // Create checkboxes for role selection
    CheckBox studentCheckBox = new CheckBox("Student");
    CheckBox instructorCheckBox = new CheckBox("Instructor");
    CheckBox adminCheckBox = new CheckBox("Admin");

    // Mark checkboxes based on current user roles
    studentCheckBox.setSelected(user.getRoles().contains("Student"));
    instructorCheckBox.setSelected(user.getRoles().contains("Instructor"));
    adminCheckBox.setSelected(user.getRoles().contains("Admin"));

    // Button to submit the role changes
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

    // Update the user's roles with the new selection
    user.setRoles(selectedRoles);
    System.out.println("Updated roles for " + selectedUser + " to: " + selectedRoles);
    editUserRoleStage.close(); // Close the role editing dialog
});

// Add elements to the layout
    layout.getChildren().addAll(userLabel, studentCheckBox, instructorCheckBox, adminCheckBox, submitButton);

// Set up and show the scene for editing roles
    Scene scene = new Scene(layout, 300, 200); // Set size for the role editing window
    editUserRoleStage.setScene(scene); // Set the scene
    editUserRoleStage.show(); // Show the stage



    // Method to handle adding a user (simplified)
    Label userLabel = new Label("Editing roles for: " + selectedUser); // Display user being edited

    // Get the current user object
    User user = userAccounts.get(selectedUser.split(" - ")[0]); // Extract username from the string

    // Create checkboxes for role selection
    CheckBox studentCheckBox = new CheckBox("Student");
    CheckBox instructorCheckBox = new CheckBox("Instructor");
    CheckBox adminCheckBox = new CheckBox("Admin");

// Mark checkboxes based on current user roles
studentCheckBox.setSelected(user.getRoles().contains("Student"));
instructorCheckBox.setSelected(user.getRoles().contains("Instructor"));
adminCheckBox.setSelected(user.getRoles().contains("Admin"));

// Button to submit the role changes
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

    // Update the user's roles with the new selection
    user.setRoles(selectedRoles);
    System.out.println("Updated roles for " + selectedUser + " to: " + selectedRoles);
    editUserRoleStage.close(); // Close the role editing dialog
});

// Add elements to the layout
layout.getChildren().addAll(userLabel, studentCheckBox, instructorCheckBox, adminCheckBox, submitButton);

// Set up and show the scene for editing roles
Scene scene = new Scene(layout, 300, 200); // Set size for the role editing window
editUserRoleStage.setScene(scene); // Set the scene
editUserRoleStage.show(); // Show the stage

// Instructor Home Page----
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


    // Admin Home Page
    private void showAdminHomePage(Stage primaryStage) {
        GridPane adminGrid = new GridPane();
        adminGrid.setPadding(new Insets(10, 10, 10, 10)); // Padding
        adminGrid.setVgap(8); // Vertical
        adminGrid.setHgap(10); // Horizontal

        Label welcomeLabel = new Label("Admin Dashboard");
        GridPane.setConstraints(welcomeLabel, 0, 0);

        // Button to invite new users
        Button inviteUserButton = new Button("Invite New User");
        GridPane.setConstraints(inviteUserButton, 0, 1);
        inviteUserButton.setOnAction(e -> showInviteUserDialog()); // Open invite user dialog

        // Button to reset user accounts
        Button resetUserButton = new Button("Reset User Account");
        GridPane.setConstraints(resetUserButton, 0, 2);
        resetUserButton.setOnAction(e -> showResetUserDialog()); // Open reset user

        // Button to delete user accounts
        Button deleteUserButton = new Button("Delete User Account");
        GridPane.setConstraints(deleteUserButton, 0, 3);
        deleteUserButton.setOnAction(e -> showDeleteUserDialog()); // Open delete user

        // Button to manage users
        Button manageUsersButton = new Button("Manage Users");
        manageUsersButton.setOnAction(e -> showManageUsersPage(primaryStage)); // Open manage users page
        
    // Button to list all user accounts
    Button listUsersButton = new Button("List User Accounts");
    GridPane.setConstraints(listUsersButton, 0, 4);
    listUsersButton.setOnAction(e -> listUsers()); // List all users

    // Logout button to exit the admin session
    Button logoutButton = new Button("Logout");
    GridPane.setConstraints(logoutButton, 0, 5);
    logoutButton.setOnAction(e -> {
        currentUser = null; // Clear the current user
        start(primaryStage); // Redirect back to the login page
});

// Add buttons and labels to the grid----
adminGrid.getChildren().addAll(welcomeLabel, inviteUserButton, resetUserButton, deleteUserButton, listUsersButton, manageUsersButton, logoutButton);

// Set up and display the admin page
Scene adminScene = new Scene(adminGrid, 400, 300);
primaryStage.setScene(adminScene);
primaryStage.setTitle("Admin Home Page");

VBox layout = new VBox(10); // Layout with spacing
layout.setPadding(new Insets(20, 20, 20, 20)); // Add padding

// Logout action to close window and return to login
logoutButton.setOnAction(e -> {
    primaryStage.close();
    start(primaryStage); // Back to login
});


    private void handleLogin(User user, Stage primaryStage) {
    if (user.getRoles().size() == 1) {
        // Single role: redirect to the appropriate home page
        redirectToRoleHomePage(user.getRoles().get(0), primaryStage);
    } else if (user.getRoles().size() > 1) {
        // Multiple roles: prompt the user to select a role
        showRoleSelectionDialog(user, primaryStage);
    }
}

    private void showRoleSelectionDialog(User user, Stage primaryStage) {
        Stage roleDialogStage = new Stage();
        roleDialogStage.setTitle("Select Role");

        GridPane roleGrid = new GridPane();
        roleGrid.setPadding(new Insets(10, 10, 10, 10)); // Add padding
        roleGrid.setVgap(8); // Vertical space
        roleGrid.setHgap(10); // Horizontal space

        // Label for role selection
        Label roleLabel = new Label("Select Role:");
        GridPane.setConstraints(roleLabel, 0, 0);

        // Dropdown for role selection
    ComboBox<String> roleSelectionBox = new ComboBox<>();
    roleSelectionBox.getItems().addAll(user.getRoles()); // Add user roles to the dropdown
    GridPane.setConstraints(roleSelectionBox, 1, 0);

    // Submit button for confirming role selection
    Button submitButton = new Button("Submit");
    GridPane.setConstraints(submitButton, 1, 1);
    submitButton.setOnAction(e -> {
        String selectedRole = roleSelectionBox.getValue(); // Get the selected role
            if (selectedRole != null) {
                redirectToRoleHomePage(selectedRole, primaryStage); // Redirect based on role
                roleDialogStage.close(); // Close the dialog
            }
    });

    // Add elements to the grid
    roleGrid.getChildren().addAll(roleLabel, roleSelectionBox, submitButton);

    // Set up and show the role selection dialog
    Scene roleScene = new Scene(roleGrid, 300, 150); // Set dialog size
    roleDialogStage.setScene(roleScene);
    roleDialogStage.show();
}

// Redirect to the appropriate home page based on the role
private void redirectToRoleHomePage(String role, Stage primaryStage) {
    String normalizedRole = role.toUpperCase(); // Normalize role string

    switch (normalizedRole) {
        case "STUDENT":
            showStudentHomePage(primaryStage); // Redirect to student home
            break;
        case "INSTRUCTOR":
            showInstructorHomePage(primaryStage); // Redirect to instructor home
            break;
    }
}

    // List all user accounts
private void listUsers() {
    System.out.println("Current Users:");
    for (String username : userAccounts.keySet()) {
        System.out.println(" - " + username + " with roles: " + userAccounts.get(username).getRoles());
    }
}

// Generate a reset token
private String generateResetToken() {
    return Long.toHexString(Double.doubleToLongBits(Math.random())); // Random hex
}

// Show the role's page
private void showRoleSpecificPage(Stage primaryStage, String role) {
    GridPane roleGrid = new GridPane();
    roleGrid.setPadding(new Insets(10, 10, 10, 10)); // Add padding
    roleGrid.setVgap(8); // Vertical spacing
    roleGrid.setHgap(10); // Horizontal spacing

    // Label to welcome the user based on their role
    Label rolePageLabel = new Label("Welcome to the " + role + " Page!");
    GridPane.setConstraints(rolePageLabel, 0, 0);

    // Logout button
    Button logoutButton = new Button("Logout");
    GridPane.setConstraints(logoutButton, 0, 1);
    logoutButton.setOnAction(e -> {
        currentUser = null; // Clear the current user
        start(primaryStage); // Redirect back to login page
    });

    // Add elements to the grid
    roleGrid.getChildren().addAll(rolePageLabel, logoutButton);

    // Set up and show the role's page
    Scene roleScene = new Scene(roleGrid, 400, 200);
    primaryStage.setScene(roleScene);
}

public static void main(String[] args) {
    launch(args); // Start
}


