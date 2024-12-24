package org.example.jbs;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        primaryStage.setTitle("Main Window");

        Button loginButton = new Button("Login");
        Button signUpButton = new Button("Sign Up");
        Button closeButton = new Button("Close");
        closeButton.setOnAction(
                e -> primaryStage.fireEvent(
                        new javafx.stage.WindowEvent(primaryStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
                )
        );

        loginButton.setOnAction(e -> {
            Stage loginStage = new Stage();
            new LoginForm().start(loginStage);
            primaryStage.hide();
            loginStage.setOnCloseRequest(event -> primaryStage.show());
        });

        signUpButton.setOnAction(e -> {
            Stage signUpStage = new Stage();
            new SignUpForm().start(signUpStage);
            primaryStage.hide();
            signUpStage.setOnCloseRequest(event -> primaryStage.show());
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(loginButton, signUpButton,closeButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}