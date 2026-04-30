package com.smartspend.controller;

import com.smartspend.service.BudgetService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class BudgetController extends BaseController {

    @FXML private Label weeklyLimitValueLabel;
    @FXML private Label remainingValueLabel;
    @FXML private Label budgetStatusLabel;
    @FXML private ProgressBar budgetProgressBar;

    @FXML private TextField budgetNameField;
    @FXML private TextField targetLimitField;
    @FXML private ComboBox<String> preferredStoreCombo;

    private final BudgetService budgetService = new BudgetService();

    private double currentBasketTotal = 72.40;

    @FXML
    public void initialize() {
        preferredStoreCombo.setItems(FXCollections.observableArrayList(
                "Aldi",
                "Coles",
                "Woolworths",
                "No preference"
        ));
        preferredStoreCombo.setValue("Aldi");

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

            updateBudgetDisplay(budget);

            String budgetName = budgetNameField.getText() == null || budgetNameField.getText().isBlank()
                    ? "Unnamed Budget"
                    : budgetNameField.getText().trim();

            String preferredStore = preferredStoreCombo.getValue() == null
                    ? "No preference"
                    : preferredStoreCombo.getValue();

            budgetStatusLabel.setText(
                    budgetName + " saved. Preferred store: " + preferredStore + ". "
                            + buildBudgetMessage(budget, currentBasketTotal)
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
        currentBasketTotal = 72.40;
        updateBudgetDisplay(100.00);
        budgetStatusLabel.setText("Budget settings reset.");
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
}