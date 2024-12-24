package org.example.jbs;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class managePosts extends Application {

    private TableView<Job> table;
    private ObservableList<Job> jobList;



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Manage Posts");

        TableColumn<Job, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Job, Integer> employerIdColumn = new TableColumn<>("Employer ID");
        employerIdColumn.setMinWidth(100);
        employerIdColumn.setCellValueFactory(new PropertyValueFactory<>("employerId"));

        TableColumn<Job, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(200);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Job, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(300);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Job, String> requirementsColumn = new TableColumn<>("Requirements");
        requirementsColumn.setMinWidth(300);
        requirementsColumn.setCellValueFactory(new PropertyValueFactory<>("requirements"));

        TableColumn<Job, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setMinWidth(200);
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(e -> {
                    Job job = getTableView().getItems().get(getIndex());
                    editJob(job);
                });

                deleteButton.setOnAction(e -> {
                    Job job = getTableView().getItems().get(getIndex());
                    deleteJob(job.getId());
                    loadJobs();
                    table.refresh(); // Refresh the table to reload the buttons
                });

                HBox pane = new HBox(editButton, deleteButton);
                pane.setSpacing(10);
                setGraphic(pane);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : getGraphic());
            }
        });

        table = new TableView<>();
        table.getColumns().addAll(idColumn, employerIdColumn, titleColumn, descriptionColumn, requirementsColumn, actionColumn);

        jobList = FXCollections.observableArrayList();
        table.setItems(jobList);

        loadJobs();

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(table);

        Scene scene = new Scene(layout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadJobs() {
        jobList.clear();
        String url = "jdbc:mysql://localhost/jbs";
        String dbUser = "root";
        String dbPass = "";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String query = "SELECT * FROM jobs";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                jobList.add(new Job(rs.getInt("id"), rs.getInt("employer_id"), rs.getString("title"), rs.getString("description"), rs.getString("requirements") , rs.getString("location")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Re-add the action column to ensure buttons are available
        TableColumn<Job, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setMinWidth(200);
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(e -> {
                    Job job = getTableView().getItems().get(getIndex());
                    editJob(job);
                });

                deleteButton.setOnAction(e -> {
                    Job job = getTableView().getItems().get(getIndex());
                    deleteJob(job.getId());
                    loadJobs();
                    table.refresh(); // Refresh the table to reload the buttons
                });

                HBox pane = new HBox(editButton, deleteButton);
                pane.setSpacing(10);
                setGraphic(pane);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : getGraphic());
            }
        });

        table.getColumns().removeIf(column -> column.getText().equals("Actions"));
        table.getColumns().add(actionColumn);
    }

    private void editJob(Job job) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Job");

        TextField titleField = new TextField(job.getTitle());
        TextField descriptionField = new TextField(job.getDescription());
        TextField requirementsField = new TextField(job.getRequirements());
        Button saveButton = new Button("Save");

        saveButton.setOnAction(e -> {
            job.setTitle(titleField.getText());
            job.setDescription(descriptionField.getText());
            job.setRequirements(requirementsField.getText());
            updateJob(job);
            loadJobs();
            //editStage.close();
                editStage.fireEvent(
                        new javafx.stage.WindowEvent(editStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
                );
            });


        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(new Label("Title:"), titleField, new Label("Description:"), descriptionField, new Label("Requirements:"), requirementsField, saveButton);

        Scene scene = new Scene(layout, 300, 200);
        editStage.setScene(scene);
        editStage.show();
    }

    private void updateJob(Job job) {
        String url = "jdbc:mysql://localhost/jbs";
        String dbUser = "root";
        String dbPass = "";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String query = "UPDATE jobs SET title = ?, description = ?, requirements = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, job.getTitle());
            stmt.setString(2, job.getDescription());
            stmt.setString(3, job.getRequirements());
            stmt.setInt(4, job.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteJob(int id) {
        String url = "jdbc:mysql://localhost/jbs";
        String dbUser = "root";
        String dbPass = "";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String query = "DELETE FROM jobs WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}