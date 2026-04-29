package com.smartspend.controller;

import com.smartspend.model.UserRegistrationDTO;
import com.smartspend.model.validation.*;
import com.smartspend.service.UserRegistrationService;
import com.smartspend.util.DatabaseManager;
import com.smartspend.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class RegisterController {


    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField budgetField;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public Label errorLabel;


    UserEntryValidator validator;
    private final UserRegistrationService userRegistrationService;
    public RegisterController() throws SQLException {
        Connection connection= DatabaseManager.getConnection();

        validator = UserEntryValidator.link(
                new NameValidation(),
                new EmailValidator(),
                new UserExistsValidator(connection),
                new UserPasswordValidator()
        );
        userRegistrationService = new UserRegistrationService(connection);
    }

    @FXML
    private void handleRegister() {
        //creating the data transfer object
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText();
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(firstName, lastName, email, password, confirmPassword);

        ValidationResult validationResult = validator.check(userRegistrationDTO);
        // validating the user input
        if (validationResult.isValid()) {
            // if all validation checks pass
            // then insert the data into the database
            userRegistrationService.register(userRegistrationDTO);
            SceneManager.switchTo("dashboard-view");
        } else{
            errorLabel.setText(validationResult.getErrorMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void handleSignIn() {
        SceneManager.switchTo("Login");
    }
}
