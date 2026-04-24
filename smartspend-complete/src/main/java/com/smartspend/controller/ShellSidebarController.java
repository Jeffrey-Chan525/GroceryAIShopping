package com.smartspend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

public class ShellSidebarController {

    @FXML private BorderPane rootPane;
    @FXML private ToggleButton navDashboard;
    @FXML private ToggleButton navMyItems;
    @FXML private ToggleButton navPriceCompare;
    @FXML private ToggleButton navCartBudget;
    @FXML private ToggleButton navAiAdvisor;
    @FXML private ToggleButton navPriceHistory;
    @FXML private ToggleButton navSettings;

    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();

        navDashboard.setToggleGroup(group);
        navMyItems.setToggleGroup(group);
        navPriceCompare.setToggleGroup(group);
        navCartBudget.setToggleGroup(group);
        navAiAdvisor.setToggleGroup(group);
        navPriceHistory.setToggleGroup(group);
        navSettings.setToggleGroup(group);

        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null && oldToggle != null) {
                oldToggle.setSelected(true);
            }
        });

        navDashboard.setSelected(true);
        loadDashboard();
    }

    @FXML private void loadDashboard()    { loadView("Dashboard.fxml"); }
    @FXML private void loadShoppingList() { loadView("ShoppingList.fxml"); }
    @FXML private void loadPriceCompare() { loadView("PriceCompare.fxml"); }
    @FXML private void loadCartBudget()   { loadView("CartBudget.fxml"); }
    @FXML private void loadAiAdvisor()    { loadView("AiAdvisor.fxml"); }
    @FXML private void loadPriceHistory() { loadView("PriceHistory.fxml"); }
    @FXML private void loadSettings()     { loadView("Settings.fxml"); }

    private void loadView(String fxml) {
        try {
            Node view = FXMLLoader.load(getClass().getResource("/fxml/" + fxml));
            rootPane.setCenter(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
