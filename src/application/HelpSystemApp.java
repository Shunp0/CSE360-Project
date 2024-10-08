package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class HelpSystemApp extends Application {

    private Map<String, User> userAccounts = new HashMap<>(); // Stores users by username
    private Map<String, String> invitationCodes = new HashMap<>(); // Stores codes
    private boolean isFirstUser = true; // Track if the first user is being created
    private User currentUser; // Store current logged-in user

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
                showAccountSetup(primaryStage);
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
            showRoleSelection(primaryStage);
        });

        setupGrid.getChildren().addAll(emailLabel, emailInput, firstNameLabel, firstNameInput, middleNameLabel, middleNameInput, lastNameLabel, lastNameInput, preferredNameLabel, preferredNameInput, finishButton);
        Scene setupScene = new Scene(setupGrid, 400, 300);
        primaryStage.setScene(setupScene);
    }

    // Scene for Role Selection
    private void showRoleSelection(Stage primaryStage) {
        if (currentUser.getRoles().size() == 1) {
            System.out.println("Redirecting to role-specific page...");
            // Logic to redirect user based on the single role
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
            });

            roleGrid.getChildren().addAll(roleLabel, roleSelect, proceedButton);
            Scene roleScene = new Scene(roleGrid, 400, 200);
            primaryStage.setScene(roleScene);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
