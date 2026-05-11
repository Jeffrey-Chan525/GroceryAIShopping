ppackage com.smartspend.controller;

import com.smartspend.model.UserRegistrationDTO;
import com.smartspend.model.validation.EmailValidator;
import com.smartspend.model.validation.NameValidation;
import com.smartspend.model.validation.UserEntryValidator;
import com.smartspend.model.validation.UserExistsValidator;
import com.smartspend.model.validation.UserPasswordValidator;
import com.smartspend.model.validation.ValidationResult;
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

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField budgetField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    private UserEntryValidator validator;
    private UserRegistrationService userRegistrationService;

    @FXML
    private void initialize() {
        hideError();

        try {
            Connection connection = DatabaseManager.getConnection();
            validator = UserEntryValidator.link(
                    new NameValidation(),
                    new EmailValidator(),
                    new UserExistsValidator(connection),
                    new UserPasswordValidator()
            );
            userRegistrationService = new UserRegistrationService(connection);
        } catch (SQLException e) {
            showError("Database connection failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        if (validator == null || userRegistrationService == null) {
            showError("Cannot register because the local database is unavailable.");
            return;
        }

        String firstName = textOf(firstNameField);
        String lastName = textOf(lastNameField);
        String email = textOf(emailField);
        String password = passwordField == null || passwordField.getText() == null ? "" : passwordField.getText();
        String confirmPassword = confirmPasswordField == null || confirmPasswordField.getText() == null ? "" : confirmPasswordField.getText();

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                firstName,
                lastName,
                email,
                password,
                confirmPassword
        );

        ValidationResult validationResult = validator.check(userRegistrationDTO);

        if (validationResult.isValid()) {
            try {
                userRegistrationService.register(userRegistrationDTO);
                SceneManager.switchTo("dashboard-view");
            } catch (Exception e) {
                showError("Registration failed: " + e.getMessage());
            }
        } else {
            showError(validationResult.getErrorMessage());
        }
    }

    @FXML
    private void handleSignIn() {
        SceneManager.switchTo("Login");
    }

    private String textOf(TextField field) {
        return field == null || field.getText() == null ? "" : field.getText().trim();
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
        }
    }

    private void hideError() {
        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
        }
    }
}