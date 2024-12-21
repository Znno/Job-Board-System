package org.example.jbs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.example.jbs.EmployerPage.getUserIdByUsername;


public class AddJobPage extends Application {
    int employerId;
    String username;
    public AddJobPage(int id, Stage test,String username) {
        employerId = id;
        this.username=username;
            test.fireEvent(
                    new javafx.stage.WindowEvent(test, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
            );

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Add Job Page");
        System.out.println("Employer ID: " + employerId);

        Label titleLabel = new Label("Job Title:");
        TextField titleField = new TextField();

        Label descriptionLabel = new Label("Job Description:");
        TextField descriptionField = new TextField();

        Label requirementsLabel = new Label("Job Requirements:");
        TextField requirementsField = new TextField();

        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            String jobTitle = titleField.getText();
            String jobDescription = descriptionField.getText();
            String jobRequirements = requirementsField.getText();

            // Check if any fields are blank
            if (jobTitle.isEmpty() || jobDescription.isEmpty() || jobRequirements.isEmpty()) {
                // Show an alert if any field is empty
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Form Incomplete");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all fields before submitting.");
                alert.showAndWait();
            } else {
                String url = "jdbc:mysql://localhost/jbs";
                String user = "root";
                String password = "";

                // Check if employer_id exists

                    String query = "INSERT INTO jobs (employer_id, title, description, requirements) " +
                            "VALUES (?, ?, ?, ?)";

                    try (Connection conn = DriverManager.getConnection(url, user, password);
                         PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, employerId);
                        stmt.setString(2, jobTitle);
                        stmt.setString(3, jobDescription);
                        stmt.setString(4, jobRequirements);

                        int rowsInserted = stmt.executeUpdate();

                        if (rowsInserted > 0) {
                            System.out.println("Job added successfully!");
                        } else {
                            System.out.println("Failed to add job.");
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                primaryStage.close();
                    Stage jobListStage = new Stage();
                new EPJobListPage(getUserIdByUsername(username),username).start(jobListStage);
                primaryStage.hide();
                jobListStage.setOnCloseRequest(event -> primaryStage.show());

            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, titleField, descriptionLabel, descriptionField, requirementsLabel, requirementsField, submitButton);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to check if employer_id exists in the employer table


    public static void main(String[] args) {
        launch(args);
    }
}
