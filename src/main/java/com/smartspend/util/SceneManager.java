package com.smartspend.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage primaryStage;

    public static void init(Stage stage) {
        primaryStage = stage;
    }

    public static void switchTo(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                SceneManager.class.getResource("/fxml/" + fxmlName + ".fxml")
            );
            Scene scene = new Scene(loader.load(), 1000, 650);
            scene.getStylesheets().add(
                SceneManager.class.getResource("/css/style.css").toExternalForm()
            );
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stage getStage() {
        return primaryStage;
    }
}
