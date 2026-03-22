package com.smartspend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label budgetLabel;
    @FXML private Label totalLabel;
    @FXML private Label briefingLabel;

    @FXML
    public void initialize() {
        budgetLabel.setText("Weekly Budget: $100.00");
        totalLabel.setText("Current Basket Total: $72.40");
        briefingLabel.setText("Your basket is cheapest at Aldi this week.");
    }
}
