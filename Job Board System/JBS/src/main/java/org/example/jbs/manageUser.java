package org.example.jbs;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class manageUser extends Application {

    private TableView<User> table;
    private ObservableList<User> users;

    private Connection connect() {
        String url = "jdbc:mysql://localhost/jbs";
        String user = "root";
        String password = "";
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createUser(String username, String password, String userType) {
        String sql = "INSERT INTO users (username, password, userType) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, userType);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUser(String oldUsername, String newUsername, String newPassword, String newUserType) {
        String sql = "UPDATE users SET username = ?, password = ?, userType = ? WHERE username = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, newPassword);
            stmt.setString(3, newUserType);
            stmt.setString(4, oldUsername);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean deleteUser(String username) {
        String getUserIdSql = "SELECT userID, userType FROM users WHERE username = ?";
        String deleteApplicationsByUserSql = "DELETE FROM applicants_details WHERE jobseeker_id = ?";
        String deleteApplicationsByEmployerSql = "DELETE FROM applicants_details WHERE employer_id = ?";
        String deleteJobsSql = "DELETE FROM jobs WHERE employer_id = ?";
        String deleteUserSql = "DELETE FROM users WHERE username = ?";
        String deleteEmployerSql = "DELETE FROM employer WHERE user_id = ?";
        String deleteJobSeekerSql = "DELETE FROM jobseeker_profile WHERE user_id = ?";

        try (Connection conn = connect();
             PreparedStatement getUserIdStmt = conn.prepareStatement(getUserIdSql);
             PreparedStatement deleteApplicationsByUserStmt = conn.prepareStatement(deleteApplicationsByUserSql);
             PreparedStatement deleteApplicationsByEmployerStmt = conn.prepareStatement(deleteApplicationsByEmployerSql);
             PreparedStatement deleteJobsStmt = conn.prepareStatement(deleteJobsSql);
             PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserSql);
             PreparedStatement deleteEmployerStmt = conn.prepareStatement(deleteEmployerSql);
             PreparedStatement deleteJobSeekerStmt = conn.prepareStatement(deleteJobSeekerSql)) {

            // Start transaction
            conn.setAutoCommit(false);

            // Get the user ID and role
            getUserIdStmt.setString(1, username);
            ResultSet rsUser = getUserIdStmt.executeQuery();
            if (rsUser.next()) {
                int userId = rsUser.getInt("userID");
                String userType = rsUser.getString("userType");

                if ("employer".equals(userType)) {
                    // Delete applications related to the employer
                    String getEmployerId = "SELECT id FROM employer WHERE name = ?";
                    PreparedStatement getEmployerIdStmt = conn.prepareStatement(getEmployerId);
                    getEmployerIdStmt.setString(1, username);
                    ResultSet rs = getEmployerIdStmt.executeQuery();
                    if (rs.next()) {
                        int employerId = rs.getInt("id");
                        deleteApplicationsByEmployerStmt.setInt(1, employerId);
                        deleteApplicationsByEmployerStmt.executeUpdate();
                        // Delete jobs related to the employer
                        deleteJobsStmt.setInt(1, employerId);
                        deleteJobsStmt.executeUpdate();
                    }

                    // Delete the employer from the employer table
                    deleteEmployerStmt.setInt(1, userId);
                    deleteEmployerStmt.executeUpdate();
                } else if ("jobSeeker".equals(userType)) {
                    // Delete applications related to the jobseeker
                    String getJobSeekerId = "SELECT id FROM jobseeker_profile WHERE name = ?";
                    PreparedStatement getJobSeekerIdStmt = conn.prepareStatement(getJobSeekerId);
                    getJobSeekerIdStmt.setString(1, username);
                    ResultSet rs = getJobSeekerIdStmt.executeQuery();

                    if (rs.next()) {
                        int jobSeekerId = rs.getInt("id");
                        deleteApplicationsByUserStmt.setInt(1, jobSeekerId);
                        deleteApplicationsByUserStmt.executeUpdate();
                    }

                    // Delete the jobseeker from the jobseeker table
                    deleteJobSeekerStmt.setInt(1, userId);
                    deleteJobSeekerStmt.executeUpdate();
                }

                // Delete the user from the users table
                deleteUserStmt.setString(1, username);
                int rowsAffected = deleteUserStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User deleted successfully.");
                } else {
                    System.out.println("User not found.");
                }

                // Commit transaction
                conn.commit();
                return rowsAffected > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try (Connection conn = connect()) {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
        return false;
    }
    private ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getString("username"), rs.getString("password"), rs.getString("userType")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    private void refreshTable() {
        users = getAllUsers();
        table.setItems(users);
    }
    private boolean updateUserStatus(String username, boolean isActive) {
        String sql = "UPDATE users SET isActive = ? WHERE username = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, isActive);
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User status updated successfully.");
            } else {
                System.out.println("User not found.");
            }
            return rowsAffected>0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void start(Stage primaryStage) {
        table = new TableView<>(); // Initialize the table here

        primaryStage.setTitle("Manage User");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        Button deleteButton = new Button("Delete");
        Button activateButton = new Button("Activate");
        Button deactivateButton = new Button("Deactivate");
        Button viewAllButton = new Button("View All");
        Label statusLabel = new Label();

        deleteButton.setOnAction(e -> {
            if (deleteUser(userField.getText())) {
                statusLabel.setText("User deleted.");
                refreshTable();
            } else {
                statusLabel.setText("User not found.");
            }
        });

        activateButton.setOnAction(e -> {
            if (updateUserStatus(userField.getText(), true)) {
                statusLabel.setText("User activated.");
                refreshTable();
            } else {
                statusLabel.setText("User not found.");
            }
        });

        deactivateButton.setOnAction(e -> {
            if (updateUserStatus(userField.getText(), false)) {
                statusLabel.setText("User deactivated.");
                refreshTable();
            } else {
                statusLabel.setText("User not found.");
            }
        });

        viewAllButton.setOnAction(e -> {
            users = getAllUsers();

            TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

            TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
            passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

            TableColumn<User, String> userTypeColumn = new TableColumn<>("User Type");
            userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));

            TableColumn<User, Void> actionColumn = new TableColumn<>("Action");
            actionColumn.setCellFactory(param -> new TableCell<>() {
                private final Button editButton = new Button("Edit");

                {
                    editButton.setOnAction(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        openEditTab(user);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }
            });

            table.setItems(users);
            table.getColumns().setAll(usernameColumn, passwordColumn, userTypeColumn, actionColumn);

            VBox vbox = new VBox(table);
            Scene scene = new Scene(vbox);
            Stage stage = new Stage();
            primaryStage.hide();
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(event -> primaryStage.show());

        });

        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(deleteButton, 0, 1);
        grid.add(activateButton, 1, 1);
        grid.add(deactivateButton, 2, 1);
        grid.add(viewAllButton, 0, 2);
        grid.add(statusLabel, 1, 2);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void openEditTab(User user) {
        Stage stage = new Stage();
        stage.setTitle("Edit User");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label newUserLabel = new Label("New Username:");
        TextField newUserField = new TextField(user.getUsername());

        Label passLabel = new Label("Password:");
        TextField passField = new TextField(user.getPassword());

        Label typeLabel = new Label("User Type:");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("employer", "jobSeeker");
        typeComboBox.setValue(user.getUserType());

        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Label statusLabel = new Label();

        updateButton.setOnAction(e -> {
            updateUser(user.getUsername(), newUserField.getText(), passField.getText(), typeComboBox.getValue());
            user.setUsername(newUserField.getText()); // Update the username in the User object
            statusLabel.setText("User updated.");
            refreshTable();
        });

        deleteButton.setOnAction(e -> {
            deleteUser(user.getUsername());
            statusLabel.setText("User deleted.");
            refreshTable();
        });

        grid.add(newUserLabel, 0, 0);
        grid.add(newUserField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(typeLabel, 0, 2);
        grid.add(typeComboBox, 1, 2);
        grid.add(updateButton, 0, 3);
        grid.add(deleteButton, 1, 3);
        grid.add(statusLabel, 0, 4, 2, 1);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static class User {
        private String username;
        private String password;
        private String userType;

        public User(String username, String password, String userType) {
            this.username = username;
            this.password = password;
            this.userType = userType;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
    }
}