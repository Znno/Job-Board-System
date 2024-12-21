package org.example.jbs;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewJobList extends Application {
    int user_id;
public ViewJobList(int user_id)
{
    this.user_id=user_id;
}

    @Override
    public void start(Stage jobStage) {
        jobStage.setTitle("Available Jobs");

        ListView<HBox> jobListView = new ListView<>();

        loadJobsFromDatabase(jobListView,jobStage);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.getChildren().addAll(new Label("Job List"), jobListView);

        Scene scene = new Scene(layout, 400, 300);
        jobStage.setScene(scene);
        jobStage.show();
    }

    private void loadJobsFromDatabase(ListView<HBox> jobListView,Stage jobstage) {
        String url = "jdbc:mysql://localhost/jbs";
        String user = "root";
        String password = "";
        String query = "SELECT title, description, requirements, employer_id FROM jobs";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int employer_id = rs.getInt("employer_id");
                String title = rs.getString("title");
                String requirements = rs.getString("requirements");
                String description = rs.getString("description");

                Label jobLabel = new Label(title);
                Button viewButton = new Button("View");

                viewButton.setOnAction(e -> {
                    Stage jobDetailStage = new Stage();
                    new ViewJobDetails(title, description, requirements, employer_id, user_id).start(jobDetailStage);
                    jobstage.hide();
                    jobDetailStage.setOnCloseRequest(event -> jobstage.show());

                });

                HBox jobEntry = new HBox(10);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                jobEntry.getChildren().addAll(jobLabel, spacer, viewButton);
                jobListView.getItems().add(jobEntry);
            }

        } catch (Exception e) {
            e.printStackTrace();
            jobListView.getItems().add(new HBox(new Label("Error loading jobs.")));
        }
    }
}