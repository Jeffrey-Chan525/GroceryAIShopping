package com.smartspend.controller;

import com.smartspend.model.UserRegistrationDTO;
import com.smartspend.model.validation.LoginValidator;
import com.smartspend.model.validation.UserEntryValidator;
import com.smartspend.model.validation.ValidationResult;
import com.smartspend.util.DatabaseManager;
import com.smartspend.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    // Login.fxml uses errorLabel. login-view.fxml uses statusLabel.
    @FXML private Label errorLabel;
    @FXML private Label statusLabel;
    @FXML private Button loginButton;

    private Connection connection;
    private UserEntryValidator validator;

    @FXML
    private void initialize() {
        hideMessage();

        try {
            connection = DatabaseManager.getConnection();
            validator = UserEntryValidator.link(new LoginValidator(connection));
            showMessage("Ready to sign in.", false);
        } catch (SQLException e) {
            validator = null;
            showMessage("Database connection failed: " + e.getMessage(), true);
            System.err.println("SQL Error in LoginController: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        String email = emailField == null || emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passwordField == null || passwordField.getText() == null ? "" : passwordField.getText();

        hideMessage();

        if (email.isBlank()) {
            showMessage("Please enter your email.", true);
            return;
        }

        if (password.isBlank()) {
            showMessage("Please enter your password.", true);
            return;
        }

        if (validator == null) {
            showMessage("Cannot sign in because the local database is unavailable.", true);
            return;
        }

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(email, password);
        ValidationResult validationResult = validator.check(userRegistrationDTO);

        if (validationResult.isValid()) {
            SceneManager.switchTo("dashboard-view");
        } else {
            showMessage(validationResult.getErrorMessage(), true);
        }
    }

    // login-view.fxml uses this handler name.
    @FXML
    private void handleSignIn() {
        handleLogin();
    }

    @FXML
    private void handleSignUp() {
        SceneManager.switchTo("Register");
    }

    @FXML
    private void handleForgotPassword() {
        showMessage("Password reset is not yet available.", true);
    }

    private void showMessage(String message, boolean isError) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
        }

        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
            statusLabel.setManaged(true);
        }
    }

    private void hideMessage() {
        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
        }

        if (statusLabel != null) {
            statusLabel.setText("");
            statusLabel.setVisible(false);
            statusLabel.setManaged(false);
        }
    }
}
