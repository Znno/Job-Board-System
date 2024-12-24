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


        layout.getChildren().addAll (profileButton, viewJobsButton,logoutButton);

        logoutButton.setOnAction(e -> {

                primaryStage.fireEvent(
                        new javafx.stage.WindowEvent(primaryStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
                );
            });

        profileButton.setOnAction(e -> {
            Stage stage=new Stage();
            new EmployerProfilePage(getUserIdByUsername(username)).start(stage);
            primaryStage.hide();
            stage.setOnCloseRequest(event -> primaryStage.show());
        });

        viewJobsButton.setOnAction(e -> {
            Stage stage=new Stage();
            new EPJobListPage(getUserIdByUsername(username),username).start(stage);
            primaryStage.hide();
            stage.setOnCloseRequest(event -> primaryStage.show());
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}