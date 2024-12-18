package org.example.jbs;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewJobDetails {
    String title, description, requirements;
    int employer_id,user_id;

    public ViewJobDetails(String title, String description,String requirements,int employer_id,int user_id)
    {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.employer_id=employer_id;
        this.user_id=user_id;
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
        applyButton.setOnAction(e -> {
            // Handle the apply action here
            new ApplyForJob(employer_id,user_id).start(new Stage());

            System.out.println("Applied for the job: " + title);
        });

        jobDetailLayout.getChildren().add(applyButton);

        Scene jobDetailScene = new Scene(jobDetailLayout, 1000, 500);
        jobDetailStage.setScene(jobDetailScene);
        jobDetailStage.show();
    }












}
