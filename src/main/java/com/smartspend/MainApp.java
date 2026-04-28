package com.smartspend;

import com.smartspend.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.init(stage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 650);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        stage.setTitle("SmartSpend");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
