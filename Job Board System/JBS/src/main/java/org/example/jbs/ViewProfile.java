package org.example.jbs;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class ViewProfile extends Application {
    int userId;

    public ViewProfile(int userId) {
        this.userId = userId;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Profile View and Update");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();

        Label experienceLabel = new Label("Experience:");
        TextArea experienceArea = new TextArea();

        Label educationLabel = new Label("Education:");
        TextArea educationArea = new TextArea();

        Button saveButton = new Button("Save Changes");
        Button cancelButton = new Button("Cancel");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nameLabel, nameField, locationLabel, locationField,
                 experienceLabel, experienceArea, educationLabel, educationArea, saveButton, cancelButton);

        saveButton.setOnAction(e -> {
            if (isValidName(nameField.getText())) {
                saveProfileChanges(nameField.getText(), locationField.getText(),experienceArea.getText(), educationArea.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid name");
                alert.setContentText("Please enter another name.");
                alert.showAndWait();
            }
        });

        cancelButton.setOnAction(e -> {
            primaryStage.fireEvent(
                    new javafx.stage.WindowEvent(primaryStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
            );
        });

        fetchProfileData(userId, nameField, locationField, experienceArea, educationArea);

        Scene scene = new Scene(layout, 600, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fetchProfileData(int userId, TextField nameField, TextField locationField,
                                   TextArea experienceArea, TextArea educationArea) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jbs", "root", "");
            String sql = "SELECT * FROM jobseeker_profile WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                locationField.setText(rs.getString("location"));
                experienceArea.setText(rs.getString("experience"));
                educationArea.setText(rs.getString("education"));
            } else {
                String insertSql = "INSERT INTO jobseeker_profile (user_id, name, location, experience, education) " +
                        "VALUES (?, '', '', '', '')";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.executeUpdate();
                System.out.println("New profile created for user: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveProfileChanges(String name, String location,
                                    String experience, String education) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jbs", "root", "");
            String sql = "UPDATE jobseeker_profile SET name = ?, location = ?, " +
                    "experience = ?, education = ?, date_updated = CURRENT_TIMESTAMP WHERE user_id = ?";
            String sql2 = "UPDATE users SET userName = ? WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);

            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setString(3, experience);
            stmt.setString(4, education);
            stmt.setInt(5, userId);
            stmt2.setString(1, name);
            stmt2.setInt(2, userId);
            conn.setAutoCommit(false);
            int rowsUpdated = stmt.executeUpdate();
            int rowsUpdated2 = stmt2.executeUpdate();
            if (rowsUpdated > 0&&rowsUpdated2>0) {
                System.out.println("Profile updated successfully!");
            } else {
                System.out.println("Profile update failed!");
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidName(String name) {

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jbs", "root", "")){
            name.trim();
            if(name.isEmpty())
            {
                return false;
            }
            String sql = "SELECT * FROM users WHERE userName = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}