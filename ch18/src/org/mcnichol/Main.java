package org.mcnichol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    @Override
    public void start(final Stage stage) {
        layoutStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void layoutStage(Stage stage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("resources/styles.fxml"));
        } catch (IOException e) {
            System.out.println("Unable to load styles.fxml");
            System.out.println("Please fix issues and re-run program.  System exiting");
            System.out.println(e.getMessage());

            System.exit(-1);
        }

        stage.setTitle("Ch 18 - File Count");
        stage.setScene(new Scene(root));
    }
}