package com.smartspend.controller;

import com.smartspend.api.ApiProduct;
import com.smartspend.api.OpenFoodFactsService;
import com.smartspend.dao.ItemDao;
import com.smartspend.model.Item;
import com.smartspend.service.ProductImportService;
import com.smartspend.util.DatabaseManager;
import javafx.application.Platform;
import com.smartspend.util.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DashboardController {
    @FXML private Label budgetLabel;
    @FXML private Label totalLabel;
    @FXML private Label briefingLabel;
    @FXML private Label statusLabel;
    @FXML private TextField quickAddField;
    @FXML private TextField searchField;
    @FXML private ListView<ApiProduct> resultsListView;
    @FXML private ListView<String> localItemsListView;
    @FXML private Button searchButton;

    private final OpenFoodFactsService apiService = new OpenFoodFactsService();
    private final ProductImportService productImportService = new ProductImportService();
    private ItemDao itemDao;

    @FXML
    public void initialize() {
        budgetLabel.setText("Weekly Budget: $100.00");
        totalLabel.setText("Current Basket Total: $72.40");
        briefingLabel.setText("Your basket is cheapest at Aldi this week. Import a real product to test the API flow.");
        statusLabel.setText("Ready. Search Open Food Facts or add a local item.");

        try {
            Connection connection = DatabaseManager.getConnection();
            itemDao = new ItemDao(connection);
            refreshLocalItems();
        } catch (Exception e) {
            statusLabel.setText("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleQuickAdd() {
        String text = quickAddField.getText() == null ? "" : quickAddField.getText().trim();
        if (text.isBlank()) {
            statusLabel.setText("Enter an item name before clicking Add.");
            return;
        }

        try {
            Item item = new Item(0, text, "Uncategorized", "Manual Entry", "unit");
            itemDao.insertItem(item);
            quickAddField.clear();
            refreshLocalItems();
            statusLabel.setText("Added local item: " + item.getName());
        } catch (Exception e) {
            statusLabel.setText("Could not add item: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String term = searchField.getText() == null ? "" : searchField.getText().trim();
        if (term.isBlank()) {
            statusLabel.setText("Enter a product name before searching.");
            return;
        }

        searchButton.setDisable(true);
        statusLabel.setText("Searching Open Food Facts for '" + term + "'...");

        CompletableFuture.supplyAsync(() -> {
            try {
                return apiService.searchProducts(term);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).whenComplete((products, throwable) -> Platform.runLater(() -> {
            searchButton.setDisable(false);
            if (throwable != null) {
                statusLabel.setText("Search failed: " + throwable.getCause().getMessage());
                return;
            }

            resultsListView.setItems(FXCollections.observableArrayList(products));
            statusLabel.setText(products.isEmpty()
                    ? "No results found for '" + term + "'."
                    : "Found " + products.size() + " product(s). Select one and import it.");
        }));
    }

    @FXML
    private void handleImportSelected() {
        ApiProduct selected = resultsListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a product from the results first.");
            return;
        }

        try {
            Item item = productImportService.mapApiProductToItem(selected);
            itemDao.insertItem(item);
            refreshLocalItems();
            briefingLabel.setText("Imported " + item.getName() + " from Open Food Facts. This proves the real-data flow works.");
            statusLabel.setText("Imported: " + item.getName());
        } catch (Exception e) {
            statusLabel.setText("Import failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleSettings() {
        try {
            SceneNavigator.switchScene(searchButton, "/fxml/settings-view.fxml", "SmartSpend - Settings");
        } catch (Exception e) {
            statusLabel.setText("Could not open Settings: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewShoppingList() {
        try {
            SceneNavigator.switchScene(searchButton, "/fxml/list-view.fxml", "SmartSpend - Shopping List");
        } catch (Exception e) {
            statusLabel.setText("Could not open Shopping List: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewPriceTrends() {
        try {
            SceneNavigator.switchScene(searchButton, "/fxml/trends-view.fxml", "SmartSpend - Price Trends");
        } catch (Exception e) {
            statusLabel.setText("Could not open Price Trends: " + e.getMessage());
        }
    }

    private void refreshLocalItems() throws Exception {
        if (itemDao == null) {
            return;
        }
        List<String> items = itemDao.getAllItems().stream()
                .map(item -> item.getName() + " | " + item.getBrand() + " | " + item.getDefaultUnit())
                .toList();
        localItemsListView.setItems(FXCollections.observableArrayList(items));
    }

}
