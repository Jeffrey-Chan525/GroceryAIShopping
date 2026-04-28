package com.smartspend.controller;

import com.smartspend.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public abstract class BaseController {

    protected void open(ActionEvent event, String fxml, String title) {
        try {
            SceneNavigator.switchScene((Node) event.getSource(), fxml, title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML protected void goLogin(ActionEvent event) { open(event, "/fxml/login-view.fxml", "SmartSpend - Login"); }
    @FXML protected void goDashboard(ActionEvent event) { open(event, "/fxml/dashboard-view.fxml", "SmartSpend - Dashboard"); }
    @FXML protected void goShoppingList(ActionEvent event) { open(event, "/fxml/shopping-list-view.fxml", "SmartSpend - Shopping List"); }
    @FXML protected void goComparison(ActionEvent event) { open(event, "/fxml/comparison-view.fxml", "SmartSpend - Price Comparison"); }
    @FXML protected void goBudget(ActionEvent event) { open(event, "/fxml/budget-view.fxml", "SmartSpend - Budget"); }
    @FXML protected void goAiChat(ActionEvent event) { open(event, "/fxml/ai-chat-view.fxml", "SmartSpend - AI Assistant"); }
    @FXML protected void goPrices(ActionEvent event) { open(event, "/fxml/prices-view.fxml", "SmartSpend - Prices"); }
    @FXML protected void goPantry(ActionEvent event) { open(event, "/fxml/pantry-view.fxml", "SmartSpend - Pantry"); }
    @FXML protected void goSettings(ActionEvent event) { open(event, "/fxml/settings-view.fxml", "SmartSpend - Settings"); }
}
