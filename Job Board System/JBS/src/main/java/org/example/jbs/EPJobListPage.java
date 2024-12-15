package org.example.jbs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EPJobListPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Job List Page");

        VBox layout = new VBox(10);
        Button addJobButton = new Button("Add Job");
        layout.getChildren().add(addJobButton);

        addJobButton.setOnAction(e -> {
            new AddJobPage().start(new Stage());
        });

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}