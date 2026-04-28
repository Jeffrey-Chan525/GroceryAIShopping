package com.smartspend.controller;

import com.smartspend.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    @FXML
    private void handleLogin() {
        errorLabel.setText("");

        String email    = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter your email and password.");
            return;
        }

        // need to replace with real auth check/database set up
        boolean ok = true; // remove later

        if (ok) {
            SceneManager.switchTo("MainShell");
        } else {
            errorLabel.setText("Incorrect email or password. Please try again.");
        }
    }

    @FXML
    private void handleSignUp() {
        SceneManager.switchTo("Register");
    }

    @FXML
    private void handleForgotPassword() {
        // TODO: implement forgot password flow
        errorLabel.setText("Password reset is not yet available.");
    }
}
