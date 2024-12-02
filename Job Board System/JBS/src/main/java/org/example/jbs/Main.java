package org.example.jbs;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
//        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        primaryStage.setTitle("Main Window");


        Button loginButton = new Button("Login");
        Button signUpButton = new Button("Sign Up");

        loginButton.setOnAction(e -> new LoginForm().start(new Stage()));
        signUpButton.setOnAction(e -> new SignUpForm().start(new Stage()));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(loginButton, signUpButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
