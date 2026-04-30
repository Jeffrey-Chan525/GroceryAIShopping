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
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private Connection connection;
    public LoginController() {

        try{
            connection = DatabaseManager.getConnection();
        } catch (SQLException e){
            System.err.println("SQL Error: " + e.getMessage());
        }

    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(email, password);

        errorLabel.setText("");

        // need to replace with real auth check/database set up
        // remove later
        UserEntryValidator validator = UserEntryValidator.link(new LoginValidator(connection));
        ValidationResult validationResult = validator.check(userRegistrationDTO);
        if (validationResult.isValid()) {
            SceneManager.switchTo("dashboard-view");
        } else {
            errorLabel.setText(validationResult.getErrorMessage());
            errorLabel.setVisible(true);
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
