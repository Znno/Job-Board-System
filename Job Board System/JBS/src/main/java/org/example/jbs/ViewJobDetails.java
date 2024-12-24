package org.example.jbs;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewJobDetails {
    String title, description, requirements;
    int employer_id,user_id;
    int job_id;

    public ViewJobDetails(String title, String description,String requirements,int employer_id,int user_id,int job_id) {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.employer_id=employer_id;
        this.user_id=user_id;
        this.job_id=job_id;
    }

    public void start(Stage jobDetailStage) {
        jobDetailStage.setTitle("Job Details");
        VBox jobDetailLayout = new VBox(10);
        jobDetailLayout.setPadding(new Insets(15));
        jobDetailLayout.getChildren().addAll(
                new Label("Title: " + title),
                new Label("Description: " + description),
                new Label("Requirements: " + requirements)
        );

        Button applyButton = new Button("Apply");
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(
                e -> jobDetailStage.fireEvent(
                        new javafx.stage.WindowEvent(jobDetailStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
                )
        );
        applyButton.setOnAction(e -> {
            Stage stage=new Stage();
            new ApplyForJob(employer_id,user_id,job_id).start(stage);

            System.out.println("Applied for the job: " + title);
        });

        jobDetailLayout.getChildren().add(applyButton);
        jobDetailLayout.getChildren().add(cancelButton);
        Scene jobDetailScene = new Scene(jobDetailLayout, 1000, 500);
        jobDetailStage.setScene(jobDetailScene);
        jobDetailStage.show();
    }












}
