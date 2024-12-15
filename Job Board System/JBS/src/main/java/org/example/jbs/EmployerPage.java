package org.example.jbs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployerPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employer Page");

        Label label = new Label("Welcome Employer");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        // i want to create 3 bottons in the employer header page (logout, profile, viewjobs)
        // and in jobs button i want to open the job list page
        // in every job i want CRUD operations


        Button logoutButton = new Button("Logout");
        Button profileButton = new Button("Profile");
        Button viewJobsButton = new Button("View Jobs");

        layout.getChildren().addAll(logoutButton, profileButton, viewJobsButton);

        logoutButton.setOnAction(e -> {
            primaryStage.close();
        });

        profileButton.setOnAction(e -> {
            new EmployerProfilePage().start(new Stage());
        });

        viewJobsButton.setOnAction(e -> {
            new EPJobListPage().start(new Stage());
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}