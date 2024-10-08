package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class AdminDashboard extends Application {

    private UserService userService = new UserService();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Dashboard");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Login Page
        Label loginLabel = new Label("Login");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        Button loginButton = new Button("Login");
        Button inviteButton = new Button("Use Invitation Code");

        // Event Handling for Login
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (userService.validateUser(username, password)) {
                User user = userService.getUserByUsername(username);
                if (user.isFirstTimeSetup()) {
                    showAccountSetup(user);
                } else {
                    if (user.getRoles().size() > 1) {
                        showRoleSelection(user);
                    } else {
                        showHomePage(user.getRoles().get(0));
                    }
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid username or password.");
            }
        });

        // Event Handling for Invitation Code
        inviteButton.setOnAction(e -> {
            // Handle invitation code logic
        });

        root.getChildren().addAll(
                loginLabel, usernameField, passwordField, loginButton, inviteButton
        );

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAccountSetup(User user) {
        Stage setupStage = new Stage();
        VBox setupRoot = new VBox(10);
        setupRoot.setPadding(new Insets(10));

        Label setupLabel = new Label("Finish Setting Up Your Account");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter First Name");
        TextField middleNameField = new TextField();
        middleNameField.setPromptText("Enter Middle Name (Optional)");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter Last Name");
        TextField preferredFirstNameField = new TextField();
        preferredFirstNameField.setPromptText("Enter Preferred First Name (Optional)");
        Button completeButton = new Button("Complete Setup");

        completeButton.setOnAction(e -> {
            String email = emailField.getText();
            String firstName = firstNameField.getText();
            String middleName = middleNameField.getText();
            String lastName = lastNameField.getText();
            String preferredFirstName = preferredFirstNameField.getText();

            user.setEmail(email);
            user.setFirstName(firstName);
            user.setMiddleName(middleName);
            user.setLastName(lastName);
            user.setPreferredFirstName(preferredFirstName);
            user.setFirstTimeSetup(false);
            showAlert(Alert.AlertType.INFORMATION, "Setup Complete", "Your account has been set up. Please log in again.");

            setupStage.close(); // Close setup window
        });

        setupRoot.getChildren().addAll(
                setupLabel, emailField, firstNameField, middleNameField, lastNameField, preferredFirstNameField, completeButton
        );

        Scene setupScene = new Scene(setupRoot, 400, 400);
        setupStage.setScene(setupScene);
        setupStage.show();
    }

    private void showRoleSelection(User user) {
        Stage roleStage = new Stage();
        VBox roleRoot = new VBox(10);
        roleRoot.setPadding(new Insets(10));

        Label roleLabel = new Label("Select Role for this Session");
        ChoiceBox<String> roleChoiceBox = new ChoiceBox<>();
        roleChoiceBox.getItems().addAll(user.getRoles());
        Button proceedButton = new Button("Proceed");

        proceedButton.setOnAction(e -> {
            String selectedRole = roleChoiceBox.getValue();
            if (selectedRole != null) {
                showHomePage(selectedRole);
                roleStage.close(); // Close role selection window
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a role.");
            }
        });

        roleRoot.getChildren().addAll(roleLabel, roleChoiceBox, proceedButton);
        Scene roleScene = new Scene(roleRoot, 300, 200);
        roleStage.setScene(roleScene);
        roleStage.show();
    }

    private void showHomePage(String role) {
        // Implement the home page for the role
        System.out.println("Welcome to the " + role + " home page!"); // Placeholder for role home page
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
