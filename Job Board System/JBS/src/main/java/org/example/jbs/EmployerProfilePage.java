package org.example.jbs;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;


public class EmployerProfilePage extends Application {
    int userId;
    public EmployerProfilePage(int userId) {
        this.userId = userId;
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employer Profile");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label companyNameLabel = new Label("companyName:");
        TextField companyNameField = new TextField();

        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();

        Label historyLabel = new Label("History");
        TextArea historyArea = new TextArea();
        
        Button saveButton = new Button("Save Changes");
        Button cancelButton = new Button("Cancel");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nameLabel, nameField, companyNameLabel, companyNameField, locationLabel, locationField,
                historyLabel, historyArea , saveButton, cancelButton);

        saveButton.setOnAction(e -> saveProfileChanges(nameField.getText(), companyNameField.getText(), locationField.getText(),
                historyArea.getText()));

        cancelButton.setOnAction(e -> primaryStage.close());


        fetchProfileData(userId, nameField, companyNameField, locationField, historyArea );

        Scene scene = new Scene(layout, 600, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fetchProfileData(int userId, TextField nameField, TextField companyNameField, TextField locationField,
                                  TextArea historyArea) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jbs", "root", "");
            String sql = "SELECT * FROM employer WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                companyNameField.setText(rs.getString("companyName"));
                locationField.setText(rs.getString("location"));
                historyArea.setText(rs.getString("history"));

            } else {
                String insertSql = "INSERT INTO employer ( user_id ,name,companyName,  location, history) " +
                        "VALUES (?,'','', '', '')";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);


                insertStmt.executeUpdate();
                System.out.println("New profile created for Employer: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void saveProfileChanges(String name, String companyName, String location, String history) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jbs", "root", "");
            String sql = "UPDATE employer SET name = ?, companyName = ?, location = ?, history = ? WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, companyName);
            stmt.setString(3, location);
            stmt.setString(4, history);
            stmt.setInt(5, userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Congrats");
                alert.setHeaderText(null); // Optional: No header
                alert.setContentText("Profile updated successfully!");
                alert.showAndWait();
            } else {
                System.out.println("Profile update failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
