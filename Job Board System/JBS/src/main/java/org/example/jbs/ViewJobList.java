package org.example.jbs;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class ViewJobList extends Application {
    int user_id;

    public ViewJobList(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public void start(Stage jobStage) {
        jobStage.setTitle("Available Jobs");

        ListView<HBox> jobListView = new ListView<>();

        loadJobsFromDatabase(jobListView, jobStage);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.getChildren().addAll(new Label("Job List"), jobListView);

        Scene scene = new Scene(layout, 400, 300);
        jobStage.setScene(scene);
        jobStage.show();
    }


    private void loadJobsFromDatabase(ListView<HBox> jobListView, Stage jobstage) {
        String url = "jdbc:mysql://localhost/jbs";
        String user = "root";
        String password = "";

        String query = "SELECT title, description, requirements, employer_id,id,Location FROM jobs";


        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int employer_id = rs.getInt("employer_id");
                String title = rs.getString("title");
                String requirements = rs.getString("requirements");
                String description = rs.getString("description");
                int job_id = rs.getInt("id");
                String query1 = "SELECT id FROM jobseeker_profile WHERE user_id=?";
                PreparedStatement stmt1 = conn.prepareStatement(query1);
                stmt1.setInt(1, user_id);
                ResultSet rs1 = stmt1.executeQuery();
                int jobseeker_id = 0;
                if (rs1.next()) {
                    jobseeker_id = rs1.getInt("id");
                }

                String query2 = "SELECT state FROM applicants_details WHERE jobseeker_id=? AND job_id=?";

                PreparedStatement stmt2 = conn.prepareStatement(query2);
                stmt2.setInt(1, jobseeker_id);
                stmt2.setInt(2, job_id);
                ResultSet rs2 = stmt2.executeQuery();
                String state = "Not Applied";
                if (rs2.next()) {
                    state = rs2.getString("state");
                }
                Label jobLabel = new Label(title);
                Label stateLabel = new Label(state);
                Button viewButton = new Button("View");
                viewButton.setOnAction(e -> {
                    String temp = "Not Applied";
                    temp = stateLabel.getText();
                    if (temp.equals("Not Applied")) {
                        System.out.println(user_id);
                        Stage jobDetailStage = new Stage();
                        new ViewJobDetails(title, description, requirements, employer_id, user_id, job_id).start(jobDetailStage);
                        jobstage.hide();
                        jobDetailStage.setOnCloseRequest(event -> jobstage.show());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Already Applied");
                        alert.setHeaderText(null);
                        alert.setContentText("You have already applied to this job.");
                        alert.showAndWait();

                    }

                });


                HBox jobEntry = new HBox(10);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                jobEntry.getChildren().addAll(jobLabel, stateLabel, spacer, viewButton);
                jobListView.getItems().add(jobEntry);
            }

        } catch (Exception e) {
            e.printStackTrace();
            jobListView.getItems().add(new HBox(new Label("Error loading jobs.")));
        }
    }
}