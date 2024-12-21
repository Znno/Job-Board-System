package org.example.jbs;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginForm extends Application {

    public String get_type(String username) {
        String url = "jdbc:mysql://localhost/jbs";
        String sql = "SELECT userType FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(url, "root", "")) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("userType");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    String user;
    public String get_username()
    {
        return user;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Login Form");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Login");
        Label statusLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            username=username.trim();
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("All fields are required!");
            }
            else if (validateLogin(username, password)==1) {

                statusLabel.setText("Login successful!");
            }
            else if(validateLogin(username, password)==0){
                statusLabel.setText("User is not activated.");
            }
            else if(validateLogin(username, password)==-1){ {
                statusLabel.setText("Invalid credentials.");
            }
            }
            else if(validateLogin(username, password)==-2){
                statusLabel.setText("Error occurred.");
            }
            String type = get_type(username);

            if(type.equals("jobSeeker")) {
                new JobSeekerPage(username).start(new Stage());
            } else if(type.equals("employer")) {
                new EmployerPage(username).start(new Stage());
            } else if(type.equals("admin")) {
                new AdminPage().start(new Stage());
            }

        });

        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(loginButton, 0, 2);
        grid.add(statusLabel, 1, 2);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
    /*
        * This method validates the login credentials
        * returns 1 if the credentials are valid and the user is activated
        * returns 0 if the credentials are valid but the user is not activated
        * returns -1 if the credentials are invalid
        * returns -2 if there is an error
     */
    private int validateLogin(String username, String password) {
        String url = "jdbc:mysql://localhost/jbs";
        String dbUser = "root";
        String dbPass = "";


        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            user=username;
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                if(rs.getBoolean("isActive")){
                    return 1;
                }
                else{
                    return 0;
                }
            }
            else{
                return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }
}
