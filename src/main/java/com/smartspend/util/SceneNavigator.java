package com.smartspend.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {

    private SceneNavigator() {
    }

    public static void switchScene(Node node, String fxmlPath, String title) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        double width = stage.getWidth();
        double height = stage.getHeight();
        FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load(), width, height);
        scene.getStylesheets().add(SceneNavigator.class.getResource("/css/style.css").toExternalForm());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
