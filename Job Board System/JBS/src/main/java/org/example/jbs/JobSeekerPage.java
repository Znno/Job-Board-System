package org.example.jbs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class JobSeekerPage extends Application {
    String username;

    public JobSeekerPage(String username) {
        this.username = username;
    }

    public static int getUserIdByUsername(String username) {
        int userId = -1;

        String sql = "SELECT userID FROM users WHERE userName = ?";

        String url = "jdbc:mysql://localhost/jbs";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("userID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Job Seeker Page");

        Label label = new Label("Welcome Job Seeker");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
        Button logoutButton = new Button("Logout");
        Button profileButton = new Button("Profile");
        Button viewJobsButton = new Button("View Jobs");

        layout.getChildren().addAll(logoutButton, profileButton, viewJobsButton);

        logoutButton.setOnAction(e -> {
            primaryStage.fireEvent(
                    new javafx.stage.WindowEvent(primaryStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
            );
        });


        int user_id = getUserIdByUsername(username);

        profileButton.setOnAction(e -> {
            Stage prfileStage = new Stage();
            new ViewProfile(user_id).start(prfileStage);
            primaryStage.hide();
            prfileStage.setOnCloseRequest(event -> primaryStage.show());
        });


        viewJobsButton.setOnAction(e -> {
            Stage stage = new Stage();
            new ViewJobList(user_id).start(stage);
            primaryStage.hide();
            stage.setOnCloseRequest(event -> primaryStage.show());
        });


    }

}