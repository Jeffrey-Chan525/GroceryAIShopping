package com.smartspend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends BaseController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void handleSignIn(ActionEvent event) {
        if (emailField.getText() == null || emailField.getText().isBlank()) {
            statusLabel.setText("Enter an email to continue.");
            return;
        }
        statusLabel.setText("Signed in as " + emailField.getText().trim() + ". Demo mode enabled.");
        goDashboard(event);
    }
}
