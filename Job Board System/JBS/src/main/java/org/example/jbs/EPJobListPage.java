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

public class EPJobListPage extends Application {
    int employer_id;
    String username;
    public EPJobListPage(int id,String username) {
        employer_id = id;
        this.username=username;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Job List Page");

        Button addJobButton = new Button("Add jobs");
        ListView<HBox> jobListView = new ListView<>();

        loadJobsFromDatabase(jobListView, primaryStage);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        Button cancel = new Button("Cancel");
        cancel.setOnAction(
                e -> primaryStage.fireEvent(
                        new javafx.stage.WindowEvent(primaryStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
                )
        );
        layout.getChildren().addAll(new Label("Job List"), jobListView, addJobButton,cancel);

        addJobButton.setOnAction(e -> {
            Stage stage = new Stage();
            new AddJobPage(employer_id ,primaryStage, username ).start(stage);
            primaryStage.hide();
            stage.setOnCloseRequest(event -> primaryStage.show());


        });
        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadJobsFromDatabase(ListView<HBox> jobListView, Stage primaryStage) {
        String url = "jdbc:mysql://localhost/jbs";
        String user = "root";
        String password = "";
        String query = "SELECT id, title, description, requirements, location FROM jobs WHERE employer_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employer_id);
            ResultSet rs = stmt.executeQuery();

            jobListView.getItems().clear();

            while (rs.next()) {
                int jobId = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String requirements = rs.getString("requirements");
                String location = rs.getString("location");

                Label jobTitleLabel = new Label("Title: "+title);
                Label jobDescriptionLabel = new Label("Description: " + description);
                Label jobRequirementsLabel = new Label("Requirements: " + requirements);
                Label jobLocationLabel = new Label("Location: " + location);

                Button applicationsButton = new Button("Applications");
                Button updateButton = new Button("Update");
                Button deleteButton = new Button("Delete");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                applicationsButton.setOnAction(e -> {
                    System.out.println("Applications job: " + title);
                    Button cancel = new Button("Cancel");
                    try {
                        Stage stage = new Stage();
                        new EPapplications(employer_id).start(stage);
                        primaryStage.hide();
                        stage.setOnCloseRequest(event -> primaryStage.show());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                });

                updateButton.setOnAction(e -> {
                    System.out.println("Update job: " + jobId);

                    primaryStage.fireEvent(
                            new javafx.stage.WindowEvent(primaryStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
                    );

                    try {
                        Stage stage = new Stage();
                        new EPupdateJob(jobId).start(stage);
                        primaryStage.hide();
                        stage.setOnCloseRequest(event -> {
                            primaryStage.show();
                            loadJobsFromDatabase(jobListView, primaryStage); // Refresh the job list
                        });
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });

                deleteButton.setOnAction(e -> {
                    deleteJob(jobId, jobListView, primaryStage);
                });

                HBox jobEntry = new HBox(10);

                jobEntry.getChildren().addAll(jobTitleLabel, jobDescriptionLabel, jobRequirementsLabel,jobLocationLabel, spacer, applicationsButton, updateButton, deleteButton);
                jobListView.getItems().add(jobEntry);
            }

        } catch (Exception e) {
            e.printStackTrace();
            jobListView.getItems().add(new HBox(new Label("Error loading jobs.")));
        }
    }

    private void deleteJob(int jobId, ListView<HBox> jobListView, Stage primaryStage) {
        String url = "jdbc:mysql://localhost/jbs";
        String user = "root";
        String password = "";
        String deleteQuery = "DELETE FROM jobs WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setInt(1, jobId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Job deleted successfully!");
                loadJobsFromDatabase(jobListView, primaryStage);
            } else {
                System.out.println("Failed to delete job.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
