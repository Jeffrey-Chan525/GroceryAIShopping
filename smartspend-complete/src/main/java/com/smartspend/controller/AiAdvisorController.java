package com.smartspend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AiAdvisorController {

    @FXML private TextField questionField;
    @FXML private Label answerLabel;

    @FXML
    private void handleAsk() {
        String question = questionField.getText();

        if (question == null || question.isBlank()) {
            answerLabel.setText("Please enter a question first.");
            return;
        }

        answerLabel.setText("Suggestion: Compare Aldi, Coles, and Woolworths totals before shopping. Choose the store with the lowest basket total.");
        questionField.clear();
    }

    @FXML
    private void handleCheapestBasket() {
        answerLabel.setText("Cheapest basket recommendation: Aldi is currently the cheapest overall option.");
    }

    @FXML
    private void handlePricePerUnit() {
        answerLabel.setText("Price per unit helps compare value across different package sizes, such as price per 100g or per litre.");
    }

    @FXML
    private void handleAlternatives() {
        answerLabel.setText("Try switching branded products to cheaper alternatives when the price per unit is lower.");
    }
}
