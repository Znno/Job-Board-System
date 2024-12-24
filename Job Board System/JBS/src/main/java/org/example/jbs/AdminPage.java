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
        Button logout = new Button("Logout");
        logout.setOnAction(e -> {
            primaryStage.fireEvent(
                    new javafx.stage.WindowEvent(primaryStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
            );
        });
        layout.getChildren().addAll(UserButton , PostsButton,logout);

        UserButton.setOnAction(e -> {
            Stage stage = new Stage();
            new manageUser().start(stage);
            primaryStage.hide();
            stage.setOnCloseRequest(event -> primaryStage.show());
        });
        PostsButton.setOnAction(e -> {
            Stage stage = new Stage();
            new managePosts().start(stage);
            primaryStage.hide();
            stage.setOnCloseRequest(event -> primaryStage.show());
        });

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}