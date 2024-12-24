package org.example.jbs;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.regex.Pattern;

public class    SignUpForm extends Application {
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    boolean simpleRegexValidation(String emailAddress) {
        String regexPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return patternMatches(emailAddress, regexPattern);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sign Up Form");

        // Create a grid layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Username field
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        // Password field
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        // Email field
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        // ComboBox for role selection
        Label roleLabel = new Label("Role:");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("employer", "jobSeeker");
        roleComboBox.setPromptText("Select Role");

        // Sign-up button and status label
        Button signUpButton = new Button("Sign Up");
        Label statusLabel = new Label();
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(
                e -> stage.fireEvent(
                        new javafx.stage.WindowEvent(stage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
                )
        );

        // Sign-up button action
        signUpButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            password=DoubleHashing.doubleHash(password);
            String email = emailField.getText();
            String role = roleComboBox.getValue();
            username = username.trim();
            email = email.trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role == null) {
                statusLabel.setText("All fields are required!");
            } else if (!simpleRegexValidation(email)) {
                statusLabel.setText("Invalid email address");
            } else if (registerUser(username, password, email, role)) {
                statusLabel.setText("Sign-up successful!");
            } else {
                statusLabel.setText("Sign-up failed.");
            }
        });

        // Add components to the grid
        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(roleLabel, 0, 3);
        grid.add(roleComboBox, 1, 3);
        grid.add(signUpButton, 0, 4);
        grid.add(cancelButton, 0, 5);
        grid.add(statusLabel, 1, 4);

        // Set the scene
        Scene scene = new Scene(grid, 350, 300);
        stage.setScene(scene);
        stage.show();
    }

    // Updated registerUser method to include email and role
    private boolean registerUser(String username, String password, String email, String role) {
        String url = "jdbc:mysql://localhost/jbs";
        String dbUser = "root";
        String dbPass = "";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            // Insert into users table
            String query = "INSERT INTO users (userName, password, email, userType) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, role);
            stmt.executeUpdate();
            conn.setAutoCommit(false);
            // Retrieve the generated user ID
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                // Insert into employer or jobseeker_profile table based on role
                if ("employer".equals(role)) {
                    String query2 = "INSERT INTO employer (user_id, name, companyName, history) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt2 = conn.prepareStatement(query2);
                    stmt2.setInt(1, userId);
                    stmt2.setString(2, username);
                    stmt2.setString(3, "");
                    stmt2.setString(4, "");
                    stmt2.executeUpdate();
                } else if ("jobSeeker".equals(role)) {
                    String query3 = "INSERT INTO jobseeker_profile (user_id, name, location, experience, education) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt3 = conn.prepareStatement(query3);
                    stmt3.setInt(1, userId);
                    stmt3.setString(2, username);
                    stmt3.executeUpdate();
                }
                conn.commit();
                return true;
            } else {
                throw new SQLException("Failed to retrieve user ID.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            // Show a pop-up message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error !!!");
            alert.setContentText("Username or email already exists.");
            alert.showAndWait();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}