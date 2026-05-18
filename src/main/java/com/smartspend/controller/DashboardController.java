package com.smartspend.controller;

import com.smartspend.api.ApiProduct;
import com.smartspend.api.OpenFoodFactsService;

import com.smartspend.dao.ItemDao;
import com.smartspend.dao.PreferenceDao;
import com.smartspend.dao.PriceDao;
import com.smartspend.dao.ShoppingListDao;

import com.smartspend.model.Item;
import com.smartspend.model.ShoppingListEntry;
import com.smartspend.model.UserPreferences;

import com.smartspend.service.ProductImportService;
import com.smartspend.service.RecommendationService;
import com.smartspend.service.PriceComparisonService;

import com.smartspend.util.DatabaseManager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for the dashboard screen
 */
public class DashboardController extends BaseController {

    // FXML fields
    @FXML
    private Label budgetLabel;
    @FXML private Label totalLabel;
    @FXML private Label briefingLabel;
    @FXML private Label statusLabel;
    @FXML private Label importedCountLabel;
    @FXML private TextField quickAddField;
    @FXML private TextField searchField;
    @FXML private ListView<ApiProduct> resultsListView;
    @FXML private ListView<String> localItemsListView;
    @FXML private Button searchButton;

    // Services
    private final OpenFoodFactsService apiService = new OpenFoodFactsService();
    private final ProductImportService productImportService = new ProductImportService();
    private final RecommendationService recommendationService = new RecommendationService();

    // DAOs
    private ItemDao itemDao;
    private PreferenceDao preferenceDao;
    private PriceDao priceDao;
    private ShoppingListDao shoppingListDao;

    // Instances
    private double userBudget = 100.0;
    private String userPrimaryStore = "Aldi";
    private final PriceComparisonService priceComparisonService = new PriceComparisonService();

    // Initialization

    /**
     * connects to the database and load real budget and basket values.
     */
    @FXML
    public void initialize() {
        try {
            Connection connection = DatabaseManager.getConnection();
            itemDao = new ItemDao(connection);
            preferenceDao = new PreferenceDao(connection);
            priceDao = new PriceDao(connection);
            shoppingListDao = new ShoppingListDao(connection);

            loadBudgetFromDb();
            loadBasketTotalFromDb();
            refreshLocalItems();

            statusLabel.setText("Ready. Search real products or add an item.");
        } catch (Exception e) {
            statusLabel.setText("Database error: " + e.getMessage());
            // Fallback
            budgetLabel.setText("$100.00");
            totalLabel.setText("$0.00");
            briefingLabel.setText("Could not connect to database.");
        }
    }

    // DB loading methods

    /**
     * Loads the user's weekly budget from preferenceDAO and updates the budget label. Or else falls back to default.
     */
    private void loadBudgetFromDb() {
        List<UserPreferences> prefs = preferenceDao.getAll();

        if (!prefs.isEmpty()) {
            UserPreferences userPrefs = prefs.get(0);
            userBudget = userPrefs.getWeeklyBudget();  // store in instance variable
            if (userPrefs.getPrimaryStore() != null) {
                userPrimaryStore = userPrefs.getPrimaryStore(); // store in instance variable
            }
            budgetLabel.setText(String.format("$%.2f", userBudget));
        } else {
            budgetLabel.setText("$100.00");
        }
    }

    /**
     * Loads the user's basket total from priceDAO and updates the total label. Or else falls back to default.
     */
    private void loadBasketTotalFromDb() {
        List<ShoppingListEntry> listItems = shoppingListDao.getAll();
        List<com.smartspend.model.Price> allPrices = priceDao.getAll();

        if (listItems.isEmpty() || allPrices.isEmpty()) {
            totalLabel.setText("$0.00");
            briefingLabel.setText("Add items to your shopping list to see your basket total.");
            return;
        }

        Map<String, Double> storeTotals =
                priceComparisonService.getStoreTotals(listItems, allPrices);

        if (storeTotals.isEmpty()) {
            totalLabel.setText("$0.00");
            briefingLabel.setText("No price data found for your shopping list items.");
            return;
        }

        String cheapestStore = priceComparisonService.findCheapestStore(storeTotals);
        double basketTotal   = storeTotals.get(cheapestStore);

        totalLabel.setText(String.format("$%.2f", basketTotal));
        briefingLabel.setText(
                recommendationService.buildFrugalBriefing(cheapestStore, basketTotal, userBudget)
        );
    }

    @FXML
    private void handleQuickAdd() {
        String text = quickAddField.getText() == null ? "" : quickAddField.getText().trim();
        if (text.isBlank()) {
            statusLabel.setText("Enter an item name before clicking Quick Add.");
            return;
        }

        try {
            Item item = new Item(0, text, "Uncategorized", "Manual Entry", "unit");
            itemDao.insert(item);
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
                String msg = throwable.getCause() == null ? throwable.getMessage() : throwable.getCause().getMessage();
                statusLabel.setText("Search failed: " + msg);
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
            itemDao.insert(item);
            refreshLocalItems();
            briefingLabel.setText("Imported " + item.getName() + " from Open Food Facts. Your AI workflow is now backed by real product data.");
            statusLabel.setText("Imported: " + item.getName());
        } catch (Exception e) {
            statusLabel.setText("Import failed: " + e.getMessage());
        }
    }

    private void refreshLocalItems() throws Exception {
        if (itemDao == null) {
            return;
        }
        List<String> items = itemDao.getAll().stream()
                .map(item -> item.getName() + "  •  " + item.getBrand() + "  •  " + item.getDefaultUnit())
                .toList();
        localItemsListView.setItems(FXCollections.observableArrayList(items));
        importedCountLabel.setText(String.valueOf(items.size()));
    }

    @FXML
    private void handleOpenSettings(ActionEvent event) {
        goSettings(event);
    }
}



