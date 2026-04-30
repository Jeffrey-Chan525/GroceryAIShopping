package com.smartspend.controller;

import com.smartspend.service.BudgetService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.util.LinkedHashMap;
import java.util.Map;

public class BudgetController extends BaseController {

    @FXML private Label weeklyLimitValueLabel;
    @FXML private Label remainingValueLabel;
    @FXML private Label budgetStatusLabel;
    @FXML private ProgressBar budgetProgressBar;

    @FXML private TextField budgetNameField;
    @FXML private TextField targetLimitField;
    @FXML private ComboBox<String> preferredStoreCombo;

    private final BudgetService budgetService = new BudgetService();

    private final Map<String, Double> storeBasketTotals = new LinkedHashMap<>();

    private double currentBasketTotal = 72.40;

    @FXML
    public void initialize() {
        setupStoreTotals();

        preferredStoreCombo.setItems(FXCollections.observableArrayList(
                "Aldi",
                "Coles",
                "Woolworths",
                "No preference"
        ));

        // Makes the selected ComboBox value visible in the high-fidelity UI styling.
        preferredStoreCombo.setButtonCell(createStoreCell());
        preferredStoreCombo.setCellFactory(listView -> createStoreCell());

        preferredStoreCombo.setValue("Aldi");
        currentBasketTotal = getBasketTotalForStore("Aldi");

        preferredStoreCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentBasketTotal = getBasketTotalForStore(newValue);

            try {
                double budget = parseMoney(targetLimitField.getText());
                updateBudgetDisplay(budget);
            } catch (NumberFormatException e) {
                updateBudgetDisplay(100.00);
            }
        });

        updateBudgetDisplay(100.00);
    }

    @FXML
    private void handleSaveBudget() {
        try {
            double budget = parseMoney(targetLimitField.getText());

            if (budget <= 0) {
                budgetStatusLabel.setText("Budget must be greater than $0.");
                return;
            }

            String budgetName = budgetNameField.getText() == null || budgetNameField.getText().isBlank()
                    ? "Unnamed Budget"
                    : budgetNameField.getText().trim();

            String preferredStore = preferredStoreCombo.getValue() == null
                    ? "No preference"
                    : preferredStoreCombo.getValue();

            currentBasketTotal = getBasketTotalForStore(preferredStore);
            updateBudgetDisplay(budget);

            budgetStatusLabel.setText(
                    budgetName + " saved. Preferred store: " + preferredStore
                            + ". Estimated basket total: $" + String.format("%.2f", currentBasketTotal)
                            + ". " + buildBudgetMessage(budget, currentBasketTotal)
            );

        } catch (NumberFormatException e) {
            budgetStatusLabel.setText("Enter a valid budget amount, for example $100.00.");
        }
    }

    @FXML
    private void handleResetBudget() {
        budgetNameField.setText("Weekly Student Shop");
        targetLimitField.setText("$100.00");
        preferredStoreCombo.setValue("Aldi");

        currentBasketTotal = getBasketTotalForStore("Aldi");
        updateBudgetDisplay(100.00);

        budgetStatusLabel.setText("Budget settings reset.");
    }

    private void setupStoreTotals() {
        storeBasketTotals.put("Aldi", 72.40);
        storeBasketTotals.put("Coles", 84.50);
        storeBasketTotals.put("Woolworths", 81.20);
        storeBasketTotals.put("No preference", 76.90);
    }

    private double getBasketTotalForStore(String store) {
        if (store == null) {
            return storeBasketTotals.get("No preference");
        }

        return storeBasketTotals.getOrDefault(store, storeBasketTotals.get("No preference"));
    }

    private void updateBudgetDisplay(double budget) {
        double remaining = budgetService.getRemainingBudget(budget, currentBasketTotal);
        boolean overBudget = budgetService.overBudget(budget, currentBasketTotal);

        weeklyLimitValueLabel.setText(String.format("$%.2f", budget));
        remainingValueLabel.setText(String.format("$%.2f", remaining));

        double progress = budget <= 0 ? 0 : currentBasketTotal / budget;
        budgetProgressBar.setProgress(Math.min(progress, 1.0));

        budgetStatusLabel.setText(buildBudgetMessage(budget, currentBasketTotal));

        if (overBudget) {
            remainingValueLabel.setStyle("-fx-text-fill: #b00020;");
        } else {
            remainingValueLabel.setStyle("");
        }
    }

    private String buildBudgetMessage(double budget, double basketTotal) {
        double remaining = budgetService.getRemainingBudget(budget, basketTotal);

        if (budgetService.overBudget(budget, basketTotal)) {
            return String.format("You are over budget by $%.2f.", Math.abs(remaining));
        }

        return String.format("You are under budget by $%.2f.", remaining);
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

    private ListCell<String> createStoreCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }

                setStyle("-fx-text-fill: #0b3d2e; -fx-font-weight: 600;");
            }
        };
    }
}