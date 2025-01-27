package com.comicland.comicland;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file and set it into the scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HelloView.fxml"));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);

        // Set up the stage
        primaryStage.setTitle("ComicLand System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
