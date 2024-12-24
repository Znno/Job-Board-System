package org.example.jbs;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ApplyForJob
{
    int employer_id,user_id,job_id;
    public ApplyForJob(int employer_id,int user_id,int job_id)
    {
        this.employer_id=employer_id;
        this.user_id=user_id;

        System.out.println(user_id+" "+employer_id+" "+job_id);
        this.job_id=job_id;

    }
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Upload CV");
        int jobseeker_id=-1;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CV (PDF File)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                uploadCVToDatabase(selectedFile, user_id, employer_id,job_id, "pending"); // Example: user_id=1, employer_id=1
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No file selected.");
            alert.showAndWait();
            System.out.println("No file selected.");
        }


            primaryStage.fireEvent(
                    new javafx.stage.WindowEvent(primaryStage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST)
            );

    }

    private void uploadCVToDatabase(File file, int userId, int employerId,int job_id, String state) {
        String url = "jdbc:mysql://localhost/jbs";
        String user = "root";
        String password = "";
        String query2="SELECT id FROM jobseeker_profile WHERE user_id=?";
        int jobseeker_id=-1;
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt2 = conn.prepareStatement(query2)) {
            stmt2.setInt(1,userId);
            ResultSet rs=stmt2.executeQuery();
            if(rs.next())
            {
                jobseeker_id=rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String query = "INSERT INTO applicants_details (cv, jobseeker_id, employer_id, state,job_id) VALUES (?, ?, ?, ?,?)";

        if (!file.getName().endsWith(".pdf")) {
            System.err.println("Invalid file type. Please upload a PDF.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             FileInputStream fis = new FileInputStream(file)) {

            stmt.setBinaryStream(1, fis, (int) file.length());
            stmt.setInt(2, jobseeker_id);
            stmt.setInt(3, employerId);
            stmt.setString(4, state);
            stmt.setInt(5, job_id);
            System.out.println(job_id+" "+employerId+" "+job_id);


            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                // show box that say applied to job succesfully
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("CV uploaded successfully.");
                alert.showAndWait();
                System.out.println("CV uploaded successfully.");
            } else {
                System.out.println("Failed to upload CV.");
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    public static void retrievePDF(int userId) {
        String url = "jdbc:mysql://localhost/jbs";
        String user = "root";
        String password = "";
        String outputFilePath="C:\\Users\\ibrahem\\Desktop\\cv.pdf";

        String query = "SELECT cv FROM applicants_details WHERE jobseeker_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                try (InputStream input = rs.getBinaryStream("cv");
                     FileOutputStream output = new FileOutputStream(outputFilePath)) {

                    byte[] buffer = new byte[1024];
                    while (input.read(buffer) > 0) {
                        output.write(buffer);
                    }

                    System.out.println("PDF retrieved and saved to " + outputFilePath);
                }
            } else {
                System.out.println("No CV found for user ID: " + userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
