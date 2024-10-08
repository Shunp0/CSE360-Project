package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class AdminDashboard extends Application {

    // Mock UserService to simulate interaction with user management backend
    private UserService userService = new UserService();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Dashboard");

        // Layout for the dashboard
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Section: Invite User
        Label inviteUserLabel = new Label("Invite New User");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        ChoiceBox<String> roleChoiceBox = new ChoiceBox<>();
        roleChoiceBox.getItems().addAll("Admin", "Instructor", "Student");
        Button inviteUserButton = new Button("Generate Invitation Code");

        // Event: Generate invitation code
        inviteUserButton.setOnAction(e -> {
            String username = usernameField.getText();
            String role = roleChoiceBox.getValue();
            if (username.isEmpty() || role == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Username and role must be provided.");
            } else {
                String invitationCode = userService.inviteUser(username, role);
                showAlert(Alert.AlertType.INFORMATION, "Invitation Code Generated", "Code: " + invitationCode);
            }
        });

        // Section: Reset User Password
        Label resetPasswordLabel = new Label("Reset User Password");
        TextField resetUsernameField = new TextField();
        resetUsernameField.setPromptText("Enter Username");
        Button resetPasswordButton = new Button("Reset Password");

        // Event: Reset password
        resetPasswordButton.setOnAction(e -> {
            String username = resetUsernameField.getText();
            if (username.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Username must be provided.");
            } else {
                userService.resetUserPassword(username);
                showAlert(Alert.AlertType.INFORMATION, "Password Reset", "A one-time password has been sent to the user.");
            }
        });

        // Section: Delete User
        Label deleteUserLabel = new Label("Delete User");
        TextField deleteUsernameField = new TextField();
        deleteUsernameField.setPromptText("Enter Username");
        Button deleteUserButton = new Button("Delete User");

        // Event: Delete user
        deleteUserButton.setOnAction(e -> {
            String username = deleteUsernameField.getText();
            if (username.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Username must be provided.");
            } else {
                boolean confirmDelete = confirmDelete();
                if (confirmDelete) {
                    userService.deleteUser(username);
                    showAlert(Alert.AlertType.INFORMATION, "User Deleted", "User " + username + " has been deleted.");
                }
            }
        });

        // Section: List Users
        Label listUsersLabel = new Label("List of Users");
        Button listUsersButton = new Button("List Users");
        TextArea userListArea = new TextArea();
        userListArea.setEditable(false);

        // Event: List users
        listUsersButton.setOnAction(e -> {
            List<User> users = userService.listUsers();
            StringBuilder userListText = new StringBuilder("Username\tRoles\n");
            for (User user : users) {
                userListText.append(user.getUsername()).append("\t").append(user.getRoles()).append("\n");
            }
            userListArea.setText(userListText.toString());
        });

        // Add components to layout
        root.getChildren().addAll(
                inviteUserLabel, usernameField, roleChoiceBox, inviteUserButton,
                resetPasswordLabel, resetUsernameField, resetPasswordButton,
                deleteUserLabel, deleteUsernameField, deleteUserButton,
                listUsersLabel, listUsersButton, userListArea
        );

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to confirm deletion
    private boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirm Deletion");
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
