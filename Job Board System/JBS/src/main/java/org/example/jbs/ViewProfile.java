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

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();

        Label bioLabel = new Label("Bio:");
        TextArea bioArea = new TextArea();

        Label skillsLabel = new Label("Skills:");
        TextArea skillsArea = new TextArea();

        Label experienceLabel = new Label("Experience:");
        TextArea experienceArea = new TextArea();

        Label educationLabel = new Label("Education:");
        TextArea educationArea = new TextArea();

        Button saveButton = new Button("Save Changes");
        Button cancelButton = new Button("Cancel");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nameLabel, nameField, emailLabel, emailField, locationLabel, locationField,
                bioLabel, bioArea, skillsLabel, skillsArea, experienceLabel, experienceArea, educationLabel, educationArea, saveButton, cancelButton);

        saveButton.setOnAction(e -> saveProfileChanges(nameField.getText(), emailField.getText(), locationField.getText(),
                bioArea.getText(), skillsArea.getText(), experienceArea.getText(), educationArea.getText()));

        cancelButton.setOnAction(e -> {
            primaryStage.fireEvent(
                    new javafx.stage.WindowEvent(primaryStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
            );
        });


        fetchProfileData(userId, nameField, emailField, locationField, bioArea, skillsArea, experienceArea, educationArea);

        Scene scene = new Scene(layout, 600, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fetchProfileData(int userId, TextField nameField, TextField emailField, TextField locationField,
                                  TextArea bioArea, TextArea skillsArea, TextArea experienceArea, TextArea educationArea) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jbs", "root", "");
            String sql = "SELECT * FROM jobseeker_profile WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                locationField.setText(rs.getString("location"));
                bioArea.setText(rs.getString("bio"));
                skillsArea.setText(rs.getString("skills"));
                experienceArea.setText(rs.getString("experience"));
                educationArea.setText(rs.getString("education"));
            } else {
                String insertSql = "INSERT INTO jobseeker_profile (user_id, name, email, location, bio, skills, experience, education) " +
                        "VALUES (?, '', '', '', '', '', '', '')";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.executeUpdate();
                System.out.println("New profile created for user: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void saveProfileChanges(String name, String email, String location, String bio, String skills,
                                    String experience, String education) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jbs", "root", "");
            String sql = "UPDATE jobseeker_profile SET name = ?, email = ?, location = ?, bio = ?, skills = ?, " +
                    "experience = ?, education = ?, date_updated = CURRENT_TIMESTAMP WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, location);
            stmt.setString(4, bio);
            stmt.setString(5, skills);
            stmt.setString(6, experience);
            stmt.setString(7, education);
            stmt.setInt(8, userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Profile updated successfully!");
            } else {
                System.out.println("Profile update failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
