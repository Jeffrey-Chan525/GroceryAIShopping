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
    private boolean backendAvailable = false;
    private boolean preferenceRecordExists = false;

    @FXML
    public void initialize() {
        setupPrimaryStoreCombo();
        loadPreferencesFromBackend();
    }

    private void setupPrimaryStoreCombo() {
        primaryStoreCombo.setItems(FXCollections.observableArrayList(
                "Aldi",
                "Coles",
                "Woolworths",
                "No preference"
        ));

        primaryStoreCombo.setValue("Aldi");
        primaryStoreCombo.setButtonCell(createComboCell());
        primaryStoreCombo.setCellFactory(list -> createComboCell());
    }

    private void loadPreferencesFromBackend() {
        try {
            Connection connection = DatabaseManager.getConnection();

            if (!tableExists(connection, "user_preferences")) {
                backendAvailable = false;
                preferenceDao = null;
                loadDefaultPreferences();
                statusLabel.setText("Settings loaded in demo mode because user_preferences table is missing.");
                return;
            }

            if (!tableExists(connection, "users") || !userExists(connection, DEFAULT_USER_ID)) {
                backendAvailable = false;
                preferenceDao = null;
                loadDefaultPreferences();
                statusLabel.setText("Settings loaded in demo mode because no local user record exists.");
                return;
            }

            preferenceDao = new PreferenceDao(connection);
            backendAvailable = true;

            UserPreferences preferences = preferenceDao.get(DEFAULT_PREFERENCE_ID);

            if (preferences == null) {
                preferenceRecordExists = false;
                loadDefaultPreferences();
                statusLabel.setText("No saved preferences found yet. Defaults loaded.");
                return;
            }

            preferenceRecordExists = true;
            applyPreferencesToUI(preferences);
            statusLabel.setText("Preferences loaded from backend.");

        } catch (Exception e) {
            backendAvailable = false;
            preferenceDao = null;
            loadDefaultPreferences();
            statusLabel.setText("Settings loaded in demo mode. Backend unavailable: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveChanges() {
        try {
            double weeklyBudget = parseMoney(weeklyBudgetField.getText());

            if (weeklyBudget <= 0) {
                statusLabel.setText("Weekly budget must be greater than $0.");
                return;
            }

            String primaryStore = primaryStoreCombo.getValue() == null
                    ? "No preference"
                    : primaryStoreCombo.getValue();

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
                    statusLabel.setText("Preferences updated in backend. Pantry reminders saved in UI only.");
                } else {
                    preferenceDao.insert(preferences);
                    preferenceRecordExists = true;
                    statusLabel.setText("Preferences saved to backend. Pantry reminders saved in UI only.");
                }
            } else {
                statusLabel.setText("Preferences saved in UI only because backend preferences table is unavailable.");
            }

        } catch (NumberFormatException e) {
            statusLabel.setText("Enter a valid weekly budget, for example $100.00.");
        }
    }

    @FXML
    private void handleResetDefaults() {
        loadDefaultPreferences();
        statusLabel.setText("Preferences reset to defaults.");
    }

    private void loadDefaultPreferences() {
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
        } else {
            primaryStoreCombo.setValue(primaryStore);
        }

        salePredictionsCheck.setSelected(preferences.isShowSalePredictions());
        valueSuggestionsCheck.setSelected(preferences.isShowValueSuggestions());

        // Pantry reminders are not currently in the UserPreferences backend model,
        // so this remains UI-only for now.
        pantryRemindersCheck.setSelected(true);
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
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT user_id FROM users WHERE user_id = " + userId)) {
            return resultSet.next();
        } catch (Exception e) {
            return false;
        }
    }

    private double parseMoney(String text) {
        if (text == null) {
            throw new NumberFormatException("No value entered");
        }

        return Double.parseDouble(
                text.replace("$", "")
                        .replace(",", "")
                        .trim()
        );
    }

    private javafx.scene.control.ListCell<String> createComboCell() {
        return new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }

                setStyle("-fx-text-fill: #183f2e; -fx-font-weight: 700;");
            }
        };
    }
}