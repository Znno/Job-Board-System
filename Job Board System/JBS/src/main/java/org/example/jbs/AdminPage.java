package org.example.jbs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Page");
        VBox layout = new VBox(10);
        Button UserButton = new Button("Users");
        Button PostsButton = new Button("Posts");
        layout.getChildren().addAll(UserButton , PostsButton);

        UserButton.setOnAction(e -> {
            new manageUser().start(new Stage());
        });
        PostsButton.setOnAction(e -> {
            new managePosts().start(new Stage());
        });

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}