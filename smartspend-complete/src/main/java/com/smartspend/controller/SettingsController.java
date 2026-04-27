package com.smartspend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class SettingsController {

    @FXML private Label lblUsername;
    @FXML private Label lblEmail;
    @FXML private Label lblSaveConfirm;
    @FXML private Label lblDbStatus;

    @FXML private ComboBox<String> cmbPreferredStore;
    @FXML private TextField txtWeeklyBudget;

    @FXML private CheckBox chkSalePredictions;
    @FXML private CheckBox chkValueSuggestions;

    @FXML private Button btnSignOut;
    @FXML private Button btnSavePreferences;
    @FXML private Button btnCancelPreferences;

    @FXML private Region dbStatusDot;

    @FXML
    public void initialize() {
        lblUsername.setText("Kevin");
        lblEmail.setText("student@qut.edu.au");

        cmbPreferredStore.setValue("No preference");
        txtWeeklyBudget.setText("100.00");

        chkSalePredictions.setSelected(true);
        chkValueSuggestions.setSelected(true);

        lblDbStatus.setText("Connected");
        dbStatusDot.setStyle("-fx-background-color: #5aa469; -fx-background-radius: 5;");

        lblSaveConfirm.setVisible(false);
        lblSaveConfirm.setManaged(false);

        btnSavePreferences.setOnAction(event -> handleSavePreferences());
        btnCancelPreferences.setOnAction(event -> handleCancelPreferences());
        btnSignOut.setOnAction(event -> handleSignOut());
    }

    private void handleSavePreferences() {
        String budgetText = txtWeeklyBudget.getText();

        try {
            double budget = Double.parseDouble(budgetText);

            if (budget <= 0) {
                showMessage("Weekly budget must be greater than 0.");
                return;
            }

            showMessage("Preferences saved. Preferred store: "
                    + cmbPreferredStore.getValue()
                    + ", weekly budget: $"
                    + String.format("%.2f", budget));

        } catch (NumberFormatException e) {
            showMessage("Please enter a valid weekly budget.");
        }
    }

    private void handleCancelPreferences() {
        cmbPreferredStore.setValue("No preference");
        txtWeeklyBudget.setText("100.00");
        chkSalePredictions.setSelected(true);
        chkValueSuggestions.setSelected(true);
        showMessage("Changes cancelled.");
    }

    private void handleSignOut() {
        lblUsername.setText("Guest");
        lblEmail.setText("Not signed in");
        showMessage("Signed out.");
    }

    private void showMessage(String message) {
        lblSaveConfirm.setText(message);
        lblSaveConfirm.setVisible(true);
        lblSaveConfirm.setManaged(true);
    }
}