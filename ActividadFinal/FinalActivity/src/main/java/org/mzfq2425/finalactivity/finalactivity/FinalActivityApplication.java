package org.mzfq2425.finalactivity.finalactivity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;
import java.util.logging.Level;

public class FinalActivityApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Locale.setDefault(Locale.ENGLISH); //force dialog options in english

        if(doesDatabaseExist()) {
            FXMLLoader fxmlLoader = new FXMLLoader(FinalActivityApplication.class.getResource("login_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 605, 285);
            stage.setTitle("Login view");
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) {
        //stop loggers from hibernate
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("org.hibernate");
        logger.setLevel(Level.SEVERE);
        launch();
    }

    // function check if database exists
    public static boolean doesDatabaseExist() {
        Connection connection = null;

        try {
            String dbUrl = "jdbc:postgresql://localhost:5432/FinalActivity";
            connection = DriverManager.getConnection(dbUrl, "postgres", "postgres");
            return true;
        } catch (Exception e) {
            System.err.println("Failed to connect tto database: " + e.getMessage());
            return false;
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
                System.err.println("Failed to recover connection: " + e.getMessage());
            }
        }
    }
}