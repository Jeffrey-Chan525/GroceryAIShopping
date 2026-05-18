package com.smartspend.controller;

import com.smartspend.service.PriceComparisonService;

import com.smartspend.dao.ItemDao;
import com.smartspend.dao.PriceDao;
import com.smartspend.dao.ShoppingListDao;
import com.smartspend.model.Item;
import com.smartspend.model.Price;
import com.smartspend.model.ShoppingListEntry;
import com.smartspend.util.DatabaseManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.util.ArrayList;

import java.util.List;

import java.util.LinkedHashMap;
import java.util.Map;

public class ComparisonController extends BaseController {

    // Inner model for tableview rows

    /**
     * Represents a single row in the price comparison table.
     * One row = one grocery item, with prices at each store.
     */
    public static class ComparisonRow {
        private final SimpleStringProperty item;
        private final SimpleStringProperty coles;
        private final SimpleStringProperty woolworths;
        private final SimpleStringProperty aldi;
        private final SimpleStringProperty bestStore;
        private final SimpleStringProperty reason;

        public ComparisonRow(String item, String coles, String woolworths, String aldi,
                             String bestStore, String reason) {
            this.item = new SimpleStringProperty(item);
            this.coles = new SimpleStringProperty(coles);
            this.woolworths = new SimpleStringProperty(woolworths);
            this.aldi = new SimpleStringProperty(aldi);
            this.bestStore = new SimpleStringProperty(bestStore);
            this.reason = new SimpleStringProperty(reason);
        }

        public String getItem() {
            return item.get();
        }

        public String getColes() {
            return coles.get();
        }

        public String getWoolworths() {
            return woolworths.get();
        }

        public String getAldi() {
            return aldi.get();
        }

        public String getBestStore() {
            return bestStore.get();
        }

        public String getReason() {
            return reason.get();
        }
    }

    // FXML Fields

    @FXML private Label lowestStoreLabel;
    @FXML private Label lowestBasketLabel;
    @FXML private Label bestDealLabel;
    @FXML private Label bestDealSubLabel;
    @FXML private Label splitShopLabel;
    @FXML private Label splitShopSubLabel;

    @FXML private TableView<ComparisonRow> comparisonTable;
    @FXML private TableColumn<ComparisonRow, String> itemColumn;
    @FXML private TableColumn<ComparisonRow, String> colesColumn;
    @FXML private TableColumn<ComparisonRow, String> woolworthsColumn;
    @FXML private TableColumn<ComparisonRow, String> aldiColumn;
    @FXML private TableColumn<ComparisonRow, String> bestStoreColumn;
    @FXML private TableColumn<ComparisonRow, String> reasonColumn;

    // Dependencies

    /**
     * Connect to the database and retrieve the comparison data.
     */
    private final PriceComparisonService priceComparisonService = new PriceComparisonService();

    private PriceDao priceDao;
    private ShoppingListDao shoppingListDao;
    private ItemDao itemDao;

    // Initialization

    /**
     * Set up the table and load the comparison data from the database.
     */

    @FXML
    public void initialize() {
        setupTable();

        try {
            Connection connection = DatabaseManager.getConnection();
            priceDao = new PriceDao(connection);
            shoppingListDao = new ShoppingListDao(connection);
            itemDao = new ItemDao(connection);
            loadComparisonDataFromDb();
        } catch (Exception e) {
            showError("Could not connect to database: " + e.getMessage());
            loadFallbackData;
        }
    }

    // Table setup

    /**
     * Binds each TableColumn to the matching property in the ComparisonRow class.
     */
    private void setupTable() {
        itemColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem()));
        colesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getColes()));
        woolworthsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getWoolworths()));
        aldiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAldi()));
        bestStoreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBestStore()));
        reasonColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReason()));
    }

    // Data loading from DB

    /**
     * Loads all prices and shopping list entries from the database and populates the comparison table.
     */

    private void loadComparisonDataFromDb() {

        List<Price> allPrices = priceDao.getAll();
        List<ShoppingListEntry> allItems = shoppingListDao.getAll();

        if (allPrices.isEmpty()) {
            showError("No Price data found in database. Add items via the Dashboard first.");
            loadFallbackData();
            return;
        }

        // Table rows
        List<ComparisonRow> rows = new ArrayList<>();

        // Collect unique item IDs from prices
        List<Integer> itemIds = new ArrayList<>();
        for (Price price : allPrices) {
            if (!itemIds.contains(price.getItemId())) {
                itemIds.add(price.getItemId());
            }
        }

        // Track best individual deal
        String bestDealItem = "-";
        String bestDealStore = "-";
        double bestDealPrice = Double.MAX_VALUE;

        for (int itemId : itemIds) {
            //Get item name from ItemDao
            Item item = itemDao.get(itemId);
            String itemName = (item != null) ? item.getName() : "Item #" + itemId;

            // Get prices per store for this item
            Map<String, Double> storePrices = new LinkedHashMap<>();
            for (Price price : allPrices) {
                if (price.getItemId() == itemId) {
                    storePrices.put(price.getStoreName(), price.getPrice());
                }
            }
        }

        String colesPrice = fo rmatPrice(storePrices.get("Coles"));
        String woolworthsPrice = formatPrice(storePrices.get("Woolworths"));
        String aldiPrice = formatPrice(storePrices.get("Aldi"));

        // Find cheapest store for this item
        String cheapestStore = priceComparisonService.findCheapestStore(storePrices);
        double cheapestPrice = cheapestStore != null ? storePrices.get(cheapestStore) : 0.0;
        String reason = cheapestStore != null
                ? String.format("%s has the lowest price at $%.2f.", cheapestStore, cheapestPrice)
                : "No prices found.";

        rows.add(new ComparisonRow(itemName, colesPrice, woolworthsPrice, aldiPrice, cheapestStore != null ? cheapestStore : "-", reason));

        // Track overall best individual deal
        if (cheapestPrice < bestDealPrice) {
            bestDealPrice = cheapestPrice;
            bestDealItem = itemName;
            bestDealStore = cheapestStore;
        }

        comparisonTable.setItems(FXCollections.observableArrayList(rows));

        // Calculate basket totals using shopping List
        Map<String, Double> basketTotals = priceComparisonService.getStoreTotals(allItems, allPrices);

        if (!basketTotals.isEmpty()) {
            String cheapestBasketStore = priceComparisonService.findCheapestStore(basketTotals);
            double cheapestBasketTotal = basketTotals.get(cheapestBasketStore);

            lowestStoreLabel.setText(cheapestBasketStore);
            lowestBasketLabel.setText(String.format("$%.2f for the full basket", cheapestBasketTotal));

            // split shop
            String splitSuggestion = buildSplitShopSuggestion(basketTotals, cheapestBasketStore);
            splitShopLabel.setText(splitSuggestion);
            splitShopSubLabel.setText("Buying select items at the cheaper store saves more");
        } else {
            // No shopping list items (show overall cheapest from price table)
            lowestStoreLabel.setText("Add items to your shopping list for basket totals");
            lowestBasketLabel.setText("Using all available prices instead");
        }

        // Best individual deal
        bestDealLabel.setText(bestDealItem + " at " + bestDealStore);
        bestDealSubLabel.setText(String.format("Cheapest individual item at $%.2f", bestDealPrice));

        if (statusLabel != null) {
            statusLabel.setText("Loaded " + rows.size() + " item(s) from database");
        }
    }

    // Helpers

    /**
     * Formats a price as a currency string, or returns "N/A" if the price is available.
     *
     * @param price the price value, or null if the store does not have the item.
     * @return formatted price string, or "N/A" if the price is not available.
     */

    private String formatPrice(Double price) {
        return (price != null) ? String.format("$%.2f", price) : "N/A";
    }

    /**
     * Builds a simple split shop suggestion based on the basket totals.
     * Recommends the cheapest store
     *
     * @param basketTotals
     * @param cheapestStore
     * @return suggestion string
     */

    private String buildSplitShopSuggestion(Map<String, Double> basketTotals, String cheapestStore) {
        String secondStore = null;
        double secondLowest = Double.MAX_VALUE;

        for (Map.Entry<String, Double> entry : basketTotals.entrySet()) {
            if (!entry.getKey().equals(cheapestStore) && entry.getValue() < secondLowest) {
                secondLowest = entry.getValue();
                secondStore = entry.getKey();
            }
        }

        if (secondStore != null) {
            double saving = secondLowest - basketTotals.get(cheapestStore);
            return String.format("%s + %s (save ~$%.2f vs %s alone)", cheapestStore, secondStore, saving, secondStore);
        }
        return cheapestStore;
    }
        /**
         * Shows an error message in the status label.
         *
         * @param message the error message to display
         */
        private void showError(String message) {
            if (statusLabel != null) {
                statusLabel.setText(message);
            }
            System.err.println("ComparisonController: " + message);
        }

        /**
         * Loads hardcoded fallback data when the database is unavailable.
         * This ensures the UI is never blank during a demo.
         */
        private void loadFallbackData() {
            List<ComparisonRow> fallback = List.of(
                    new ComparisonRow("Whole milk 2L",      "$3.20", "$3.10", "$2.80", "Aldi",        "Aldi has the lowest price at $2.80."),
                    new ComparisonRow("Chicken breast 500g","$6.49", "$7.00", "$7.20", "Coles",       "Coles has the lowest price at $6.49."),
                    new ComparisonRow("Pasta 500g",         "$1.80", "$1.60", "$1.20", "Aldi",        "Aldi has the lowest price at $1.20."),
                    new ComparisonRow("Greek yoghurt 1kg",  "$4.50", "$3.90", "$3.90", "Woolworths",  "Woolworths has the lowest price at $3.90.")
            );
            comparisonTable.setItems(FXCollections.observableArrayList(fallback));
            lowestStoreLabel.setText("Aldi");
            lowestBasketLabel.setText("$15.10 for the full basket (demo data)");
            bestDealLabel.setText("Pasta at Aldi");
            bestDealSubLabel.setText("Cheapest individual item at $1.20");
            splitShopLabel.setText("Aldi + Coles");
            splitShopSubLabel.setText("Aldi is cheapest overall, Coles wins on chicken");
        }
    }