package com.smartspend.controller;

import com.smartspend.dao.PreferenceDao;
import com.smartspend.model.UserPreferences;
import com.smartspend.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SettingsController extends BaseController {

    @FXML private TextField weeklyBudgetField;
    @FXML private ComboBox<String> primaryStoreCombo;
    @FXML private CheckBox salePredictionsCheck;
    @FXML private CheckBox valueSuggestionsCheck;
    @FXML private CheckBox pantryRemindersCheck;
    @FXML private Label statusLabel;

    private static final int DEFAULT_PREFERENCE_ID = 1;
    private static final int DEFAULT_USER_ID = 1;

    private PreferenceDao preferenceDao;
    private boolean backendAvailable;
    private boolean preferenceRecordExists;

    @FXML
    private void initialize() {
        setupStoreOptions();
        loadDefaults();
        connectBackendAndLoadPreferences();
    }

    private void setupStoreOptions() {
        primaryStoreCombo.setItems(FXCollections.observableArrayList(
                "Aldi",
                "Coles",
                "Woolworths",
                "No preference"
        ));

        primaryStoreCombo.setValue("Aldi");
    }

    private void connectBackendAndLoadPreferences() {
        try {
            Connection connection = DatabaseManager.getConnection();

            if (!tableExists(connection, "users")) {
                setDemoMode("Settings loaded in demo mode because the users table is missing.");
                return;
            }

            if (!tableExists(connection, "user_preferences")) {
                setDemoMode("Settings loaded in demo mode because the user_preferences table is missing.");
                return;
            }

            if (!userExists(connection, DEFAULT_USER_ID)) {
                setDemoMode("Settings loaded in demo mode because no demo user exists yet.");
                return;
            }

            preferenceDao = new PreferenceDao(connection);
            backendAvailable = true;

            UserPreferences preferences = preferenceDao.get(DEFAULT_PREFERENCE_ID);

            if (preferences == null) {
                preferenceRecordExists = false;
                loadDefaults();
                showStatus("No saved settings found. Defaults loaded.");
                return;
            }

            preferenceRecordExists = true;
            applyPreferencesToUI(preferences);
            showStatus("Settings loaded from backend.");

        } catch (Exception e) {
            setDemoMode("Settings loaded in demo mode. Backend unavailable: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveChanges() {
        try {
            double weeklyBudget = parseWeeklyBudget();

            if (weeklyBudget <= 0) {
                showStatus("Weekly budget must be greater than $0.");
                return;
            }

            String primaryStore = primaryStoreCombo.getValue();

            if (primaryStore == null || primaryStore.isBlank()) {
                primaryStore = "No preference";
            }

            UserPreferences preferences = new UserPreferences(
                    DEFAULT_PREFERENCE_ID,
                    DEFAULT_USER_ID,
                    weeklyBudget,
                    primaryStore,
                    salePredictionsCheck.isSelected(),
                    valueSuggestionsCheck.isSelected()
            );

            if (backendAvailable && preferenceDao != null) {
                if (preferenceRecordExists) {
                    preferenceDao.update(preferences);
                    showStatus("Settings updated successfully.");
                } else {
                    preferenceDao.insert(preferences);
                    preferenceRecordExists = true;
                    showStatus("Settings saved successfully.");
                }
            } else {
                showStatus("Settings saved for this session only because backend storage is unavailable.");
            }

        } catch (NumberFormatException e) {
            showStatus("Enter a valid weekly budget, for example 100 or 100.00.");
        } catch (Exception e) {
            showStatus("Could not save settings: " + e.getMessage());
        }
    }

    @FXML
    private void handleResetDefaults() {
        loadDefaults();
        showStatus("Settings reset to defaults.");
    }

    private void loadDefaults() {
        weeklyBudgetField.setText("$100.00");
        primaryStoreCombo.setValue("Aldi");
        salePredictionsCheck.setSelected(true);
        valueSuggestionsCheck.setSelected(true);
        pantryRemindersCheck.setSelected(true);
    }

    private void applyPreferencesToUI(UserPreferences preferences) {
        weeklyBudgetField.setText(String.format("$%.2f", preferences.getWeeklyBudget()));

        String primaryStore = preferences.getPrimaryStore();

        if (primaryStore == null || primaryStore.isBlank()) {
            primaryStoreCombo.setValue("No preference");
        } else if (primaryStoreCombo.getItems().contains(primaryStore)) {
            primaryStoreCombo.setValue(primaryStore);
        } else {
            primaryStoreCombo.setValue("No preference");
        }

        salePredictionsCheck.setSelected(preferences.isShowSalePredictions());
        valueSuggestionsCheck.setSelected(preferences.isShowValueSuggestions());

        // Pantry reminders are not currently stored in UserPreferences,
        // so this remains UI-only for now.
        pantryRemindersCheck.setSelected(true);
    }

    private void setDemoMode(String message) {
        backendAvailable = false;
        preferenceRecordExists = false;
        preferenceDao = null;
        loadDefaults();
        showStatus(message);
    }

    private void showStatus(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
    }

    private double parseWeeklyBudget() {
        String text = weeklyBudgetField.getText();

        if (text == null || text.isBlank()) {
            throw new NumberFormatException("Weekly budget is blank");
        }

        return Double.parseDouble(
                text.replace("$", "")
                        .replace(",", "")
                        .trim()
        );
    }

    private boolean tableExists(Connection connection, String tableName) {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            return resultSet.next();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean userExists(Connection connection, int userId) {
        String query = "SELECT user_id FROM users WHERE user_id = " + userId;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            return resultSet.next();
        } catch (Exception e) {
            return false;
        }
    }
}