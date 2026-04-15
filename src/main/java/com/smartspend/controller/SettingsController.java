package com.smartspend.controller;

import com.smartspend.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class SettingsController {
    @FXML
    private Button backButton;

    @FXML
    private void handleBack() throws IOException {
        SceneNavigator.switchScene(backButton, "/fxml/dashboard-view.fxml", "SmartSpend");
    }
}
