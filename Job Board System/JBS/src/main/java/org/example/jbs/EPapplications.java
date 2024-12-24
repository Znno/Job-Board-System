package org.example.jbs;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class EPapplications extends Application {
    int employer_id;
    public EPapplications(int id){
        employer_id=id;
    }
    private TableView<Applicant> tableView = new TableView<>();
    private ObservableList<Applicant> applicantsList = FXCollections.observableArrayList();

    private static final String DB_URL = "jdbc:mysql://localhost:3306/jbs";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        TableColumn<Applicant, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());

        TableColumn<Applicant, String> nameColumn = new TableColumn<>("jobSeeker_id");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<Applicant, String> employer_idColumn = new TableColumn<>("employer_id");
        employer_idColumn.setCellValueFactory(data -> data.getValue().employer_id_Property());

        TableColumn<Applicant, String> stateColumn = new TableColumn<>("state");
        stateColumn.setCellValueFactory(data -> data.getValue().stateProperty());

        tableView.getColumns().addAll(idColumn, nameColumn, employer_idColumn, stateColumn);

        // Buttons
        Button acceptButton = new Button("Accept");
        acceptButton.setOnAction(e -> updateApplicantstate("Accepted"));

        Button rejectButton = new Button("Reject");
        rejectButton.setOnAction(e -> updateApplicantstate("Rejected"));
        Button cancel=new Button("Cancel");
        cancel.setOnAction(e -> {
            stage.fireEvent(
                    new javafx.stage.WindowEvent(stage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
            );
        });

        // Layout
        VBox layout = new VBox(10, tableView, acceptButton, rejectButton,cancel);
        layout.setPadding(new Insets(10));

        // Scene
        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Applicants Management");
        stage.show();

        // Load data
        loadApplicants(employer_id);
    }

    private void loadApplicants(int employerId) {
        applicantsList.clear();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Filter applications by employer_id
            String query = "SELECT * FROM applicants_details WHERE employer_id = ? AND state = 'Pending'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, employerId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("jobSeeker_id");
                String employer_id = resultSet.getString("employer_id");
                String state = resultSet.getString("state");

                applicantsList.add(new Applicant(id, name, employer_id, state));
            }

            tableView.setItems(applicantsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void updateApplicantstate(String state) {
        Applicant selectedApplicant = tableView.getSelectionModel().getSelectedItem();
        if (selectedApplicant == null) {
            showAlert("No Applicant Selected", "Please select an applicant to " + state.toLowerCase() + ".");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateQuery = "UPDATE applicants_details SET state = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1, state);
            statement.setInt(2, selectedApplicant.getId());
            statement.executeUpdate();

            showAlert("Success", "Applicant " + state.toLowerCase() + " successfully.");
            loadApplicants(employer_id); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
