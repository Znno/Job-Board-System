package org.example.jbs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddJobPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Add Job Page");

        Label titleLabel = new Label("Job Title:");
        TextField titleField = new TextField();

        Label descriptionLabel = new Label("Job Description:");
        TextField descriptionField = new TextField();

        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            String jobTitle = titleField.getText();
            String jobDescription = descriptionField.getText();
            // Add logic to handle job submission
            System.out.println("Job Title: " + jobTitle);
            System.out.println("Job Description: " + jobDescription);
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, titleField, descriptionLabel, descriptionField, submitButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
