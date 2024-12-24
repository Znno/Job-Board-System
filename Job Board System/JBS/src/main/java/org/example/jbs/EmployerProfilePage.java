package org.example.jbs;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;


public class EmployerProfilePage extends Application {
    int userId;

    public EmployerProfilePage(int userId) {
        this.userId = userId;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employer Profile");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label companyNameLabel = new Label("companyName:");
        TextField companyNameField = new TextField();

        Label historyLabel = new Label("History");
        TextArea historyArea = new TextArea();

        Button saveButton = new Button("Save Changes");

        Button cancelButton = new Button("Cancel");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nameLabel, nameField, companyNameLabel, companyNameField,
                historyLabel, historyArea, saveButton, cancelButton);

        saveButton.setOnAction(e -> {
            if (ViewProfile.isValidName(nameField.getText())) {
                saveProfileChanges(nameField.getText(), companyNameField.getText(),
                        historyArea.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid name");
                alert.setContentText("Please enter another name.");
                alert.showAndWait();
            }
        });



        cancelButton.setOnAction(e -> {
            primaryStage.fireEvent(
                    new javafx.stage.WindowEvent(primaryStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
            );
        });

        fetchProfileData(userId, nameField, companyNameField, historyArea);

        Scene scene = new Scene(layout, 600, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void fetchProfileData(int userId, TextField nameField, TextField companyNameField,
                                  TextArea historyArea) {
        try ( Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jbs", "root", "")){

            String sql = "SELECT * FROM employer WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                companyNameField.setText(rs.getString("companyName"));
                historyArea.setText(rs.getString("history"));

            } else {
                String insertSql = "INSERT INTO employer ( id ,name,companyName, history) " +
                        "VALUES (?,'','', '')";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);


                insertStmt.executeUpdate();
                System.out.println("New profile created for Employer: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    private void saveProfileChanges(String name, String companyName, String history) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jbs", "root", "");
            String sql = "UPDATE employer SET name = ?, companyName = ?, history = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, companyName);
            stmt.setString(3, history);
            stmt.setInt(4, userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Congrats");
                alert.setHeaderText(null); // Optional: No header
                alert.setContentText("Profile updated successfully!");
                alert.showAndWait();
            } else {
                System.out.println("Profile update failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
