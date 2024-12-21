package org.example.jbs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class EmployerPage extends Application {
    String username;
    public EmployerPage(String username)
    {
        this.username=username;
    }
    public static int getUserIdByUsername(String username) {
        int userId = -1;

        String sql = "SELECT userID FROM users WHERE userName = ?";
        String sql2= "SELECT id FROM employer WHERE user_id = ?";

        String url = "jdbc:mysql://localhost/jbs";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            PreparedStatement stmt2 =conn.prepareStatement(sql2);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("userID");
                stmt2.setInt(1,userId);
                ResultSet rs2=stmt2.executeQuery();
                if(rs2.next()){
                    userId=rs2.getInt("id");
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employer Page");

        Label label = new Label("Welcome Employer");
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
            primaryStage.close();
        });

        profileButton.setOnAction(e -> {
            new EmployerProfilePage(getUserIdByUsername(username)).start(new Stage());
        });

        viewJobsButton.setOnAction(e -> {
            new EPJobListPage(getUserIdByUsername(username),username).start(new Stage());
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}