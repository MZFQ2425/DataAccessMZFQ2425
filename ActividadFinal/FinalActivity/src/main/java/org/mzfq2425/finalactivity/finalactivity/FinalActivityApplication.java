package org.mzfq2425.finalactivity.finalactivity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;

public class FinalActivityApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FinalActivityApplication.class.getResource("login_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 605, 285);
        stage.setTitle("Login view");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //stop loggers from hibernate
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("org.hibernate");
        logger.setLevel(Level.SEVERE);
        launch();
    }
}