package org.example.jbs;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ApplyForJob
{
    int employer_id,user_id;
    public ApplyForJob(int employer_id,int user_id)
    {
        this.employer_id=employer_id;
        this.user_id=user_id;

    }
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Upload CV");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CV (PDF File)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                uploadCVToDatabase(selectedFile, user_id, employer_id, "pending"); // Example: user_id=1, employer_id=1
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No file selected.");
        }

        primaryStage.close();
    }

    private void uploadCVToDatabase(File file, int userId, int employerId, String state) {
        String url = "jdbc:mysql://localhost/jbs";
        String user = "root";
        String password = "";

        String query = "INSERT INTO applicants_details (cv, user_id, employer_id, state) VALUES (?, ?, ?, ?)";

        if (!file.getName().endsWith(".pdf")) {
            System.err.println("Invalid file type. Please upload a PDF.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             FileInputStream fis = new FileInputStream(file)) {

            stmt.setBinaryStream(1, fis, (int) file.length());
            stmt.setInt(2, userId);
            stmt.setInt(3, employerId);
            stmt.setString(4, state);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
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

        String query = "SELECT cv FROM applicants_details WHERE user_id = ?";

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
