package org.example.jbs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JobSeekerPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Job Seeker Page");

        Label label = new Label("Welcome Job Seeker");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        // i want to create 3 bottons in the jobseeker header page (logout, profile, view jobs)

        Button logoutButton = new Button("Logout");
        Button profileButton = new Button("Profile");
        Button viewJobsButton = new Button("View Jobs");

        layout.getChildren().addAll(logoutButton, profileButton, viewJobsButton);

        logoutButton.setOnAction(e -> {
            primaryStage.close();
        });

        profileButton.setOnAction(e -> {
            new JSProfilePage().start(new Stage());
        });

        viewJobsButton.setOnAction(e -> {
            new JSJobListPage().start(new Stage());
        });


    }

}