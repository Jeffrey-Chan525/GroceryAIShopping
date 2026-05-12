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

        addToGroup(navDashboard, group);
        addToGroup(navMyItems, group);
        addToGroup(navPriceCompare, group);
        addToGroup(navCartBudget, group);
        addToGroup(navAiAdvisor, group);
        addToGroup(navPriceHistory, group);
        addToGroup(navSettings, group);

        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null && oldToggle != null) {
                oldToggle.setSelected(true);
            }
        });

        if (navDashboard != null) {
            navDashboard.setSelected(true);
        }

        loadDashboard();
    }

    private void addToGroup(ToggleButton button, ToggleGroup group) {
        if (button != null) {
            button.setToggleGroup(group);
        }
    }

    @FXML private void loadDashboard()    { loadView("dashboard-view.fxml"); }
    @FXML private void loadShoppingList() { loadView("shopping-list-view.fxml"); }
    @FXML private void loadPriceCompare() { loadView("comparison-view.fxml"); }
    @FXML private void loadCartBudget()   { loadView("budget-view.fxml"); }
    @FXML private void loadAiAdvisor()    { loadView("ai-chat-view.fxml"); }
    @FXML private void loadPriceHistory() { loadView("prices-view.fxml"); }
    @FXML private void loadSettings()     { loadView("settings-view.fxml"); }

    private void loadView(String fxml) {
        if (rootPane == null) {
            System.err.println("Cannot load " + fxml + ": rootPane is not connected.");
            return;
        }

        try {
            Node view = FXMLLoader.load(getClass().getResource("/fxml/" + fxml));
            rootPane.setCenter(view);
        } catch (Exception e) {
            System.err.println("Failed to load /fxml/" + fxml + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
