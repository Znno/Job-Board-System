package org.example.jbs;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class EPupdateJob extends Application {

    private int jobId;
    public EPupdateJob(int id){
        jobId=id;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Label titleLabel = new Label("Job Title:");
        TextField titleField = new TextField();

        Label descriptionLabel = new Label("Job Description:");
        TextField descriptionField = new TextField();

        Label requirementsLabel = new Label("Job Requirements:");
        TextField requirementsField = new TextField();

        Button updateButton = new Button("Update");
        Button cancelButton = new Button("Cancel");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, titleField, descriptionLabel, descriptionField, requirementsLabel, requirementsField, updateButton, cancelButton);

        // Update button action
        updateButton.setOnAction(e -> saveJobChanges(titleField.getText(), descriptionField.getText(), requirementsField.getText()));

        // Cancel button action
        cancelButton.setOnAction(e -> {
            primaryStage.fireEvent(
                    new javafx.stage.WindowEvent(primaryStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
            );
        });
        // Fetch job data based on jobId
        fetchJobData(jobId, titleField, descriptionField, requirementsField);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to fetch job data based on job ID
    private void fetchJobData(int jobId, TextField titleField, TextField descriptionField, TextField requirementsField) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jbs", "root", "");
            String sql = "SELECT title, description, requirements FROM jobs WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                titleField.setText(rs.getString("title"));
                descriptionField.setText(rs.getString("description"));
                requirementsField.setText(rs.getString("requirements"));
            } else {
                System.out.println("Job not found with job ID: " + jobId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to save the updated job details
    private void saveJobChanges(String title, String description, String requirements) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jbs", "root", "");
            String sql = "UPDATE jobs SET title = ?, description = ?, requirements = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, requirements);
            stmt.setInt(4, jobId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Job details updated successfully!");
                alert.showAndWait();
            } else {
                System.out.println("Job update failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to set the job ID (this could be passed when opening the page)


    public static void main(String[] args) {
        launch(args);
    }
}
