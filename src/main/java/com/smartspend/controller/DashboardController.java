package com.smartspend.controller;

import com.smartspend.api.ApiProduct;
import com.smartspend.api.OpenFoodFactsService;
import com.smartspend.dao.ItemDao;
import com.smartspend.model.Item;
import com.smartspend.service.ProductImportService;
import com.smartspend.service.RecommendationService;
import com.smartspend.util.DatabaseManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DashboardController extends BaseController {

    @FXML private Label budgetLabel;
    @FXML private Label totalLabel;
    @FXML private Label briefingLabel;
    @FXML private Label statusLabel;
    @FXML private Label importedCountLabel;
    @FXML private TextField quickAddField;
    @FXML private TextField searchField;
    @FXML private ListView<ApiProduct> resultsListView;
    @FXML private ListView<String> localItemsListView;
    @FXML private Button searchButton;

    // Legacy Dashboard.fxml support.
    @FXML private TableView<ShoppingItem> itemsTable;
    @FXML private TableColumn<ShoppingItem, String> colName;
    @FXML private TableColumn<ShoppingItem, String> colQty;
    @FXML private TableColumn<ShoppingItem, String> colPrice;
    @FXML private TableColumn<ShoppingItem, String> colStore;
    @FXML private TableColumn<ShoppingItem, String> colStatus;

    private final OpenFoodFactsService apiService = new OpenFoodFactsService();
    private final ProductImportService productImportService = new ProductImportService();
    private final RecommendationService recommendationService = new RecommendationService();
    private ItemDao itemDao;

    @FXML
    public void initialize() {
        initialiseModernDashboardIfPresent();
        initialiseLegacyDashboardIfPresent();
        connectBackend();
    }

    private void initialiseModernDashboardIfPresent() {
        if (budgetLabel != null) {
            budgetLabel.setText("Weekly Budget\n$100.00");
        }

        if (totalLabel != null) {
            totalLabel.setText("Basket Total\n$72.40");
        }

        if (briefingLabel != null) {
            briefingLabel.setText(recommendationService.buildFrugalBriefing("Aldi", 72.40, 100.00));
        }

        setStatus("Ready. Search real products or add a local item.");

        if (importedCountLabel != null) {
            importedCountLabel.setText("0");
        }
    }

    private void initialiseLegacyDashboardIfPresent() {
        if (itemsTable == null) {
            return;
        }

        if (colName != null) {
            colName.setCellValueFactory(data -> data.getValue().nameProperty());
        }

        if (colQty != null) {
            colQty.setCellValueFactory(data -> data.getValue().qtyProperty());
        }

        if (colPrice != null) {
            colPrice.setCellValueFactory(data -> data.getValue().bestPriceProperty());
        }

        if (colStore != null) {
            colStore.setCellValueFactory(data -> data.getValue().storeProperty());
        }

        if (colStatus != null) {
            colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        }

        itemsTable.setItems(FXCollections.observableArrayList(
                new ShoppingItem("Whole milk 2L", "1", "$2.80", "Aldi", "Needed"),
                new ShoppingItem("Chicken breast 500g", "1", "$6.49", "Coles", "Needed"),
                new ShoppingItem("Pasta 500g", "2", "$1.20", "Aldi", "Stock up"),
                new ShoppingItem("Greek yoghurt 1kg", "1", "$3.90", "Woolworths", "Needed")
        ));
    }

    private void connectBackend() {
        try {
            Connection connection = DatabaseManager.getConnection();
            itemDao = new ItemDao(connection);
            refreshLocalItems();
        } catch (Exception e) {
            itemDao = null;
            setStatus("Database unavailable. Dashboard running in demo mode: " + e.getMessage());
        }
    }

    @FXML
    private void handleQuickAdd() {
        String text = quickAddField == null || quickAddField.getText() == null
                ? ""
                : quickAddField.getText().trim();

        if (text.isBlank()) {
            setStatus("Enter an item name before clicking Quick Add.");
            return;
        }

        Item item = new Item(0, text, "Uncategorized", "Manual Entry", "unit");

        if (itemDao == null) {
            addLocalListItem(item);

            if (quickAddField != null) {
                quickAddField.clear();
            }

            setStatus("Added demo item: " + item.getName());
            return;
        }

        try {
            itemDao.insert(item);

            if (quickAddField != null) {
                quickAddField.clear();
            }

            refreshLocalItems();
            setStatus("Added local item: " + item.getName());
        } catch (Exception e) {
            addLocalListItem(item);
            setStatus("Added item to UI only. Backend insert failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String term = searchField == null || searchField.getText() == null
                ? ""
                : searchField.getText().trim();

        if (term.isBlank()) {
            setStatus("Enter a product name before searching.");
            return;
        }

        if (searchButton != null) {
            searchButton.setDisable(true);
        }

        setStatus("Searching Open Food Facts for '" + term + "'...");

        CompletableFuture.supplyAsync(() -> {
            try {
                return apiService.searchProducts(term);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).whenComplete((products, throwable) -> Platform.runLater(() -> {
            if (searchButton != null) {
                searchButton.setDisable(false);
            }

            if (throwable != null) {
                String msg = throwable.getCause() == null
                        ? throwable.getMessage()
                        : throwable.getCause().getMessage();

                setStatus("Search failed: " + msg);
                return;
            }

            if (resultsListView != null) {
                resultsListView.setItems(FXCollections.observableArrayList(products));
            }

            setStatus(products.isEmpty()
                    ? "No results found for '" + term + "'."
                    : "Found " + products.size() + " product(s). Select one and import it.");
        }));
    }

    @FXML
    private void handleImportSelected() {
        if (resultsListView == null) {
            setStatus("Product results are not available on this screen.");
            return;
        }

        ApiProduct selected = resultsListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            setStatus("Select a product from the results first.");
            return;
        }

        try {
            Item item = productImportService.mapApiProductToItem(selected);

            if (itemDao != null) {
                itemDao.insert(item);
                refreshLocalItems();
            } else {
                addLocalListItem(item);
            }

            if (briefingLabel != null) {
                briefingLabel.setText("Imported " + item.getName() + " from Open Food Facts. Your AI workflow is now backed by real product data.");
            }

            setStatus("Imported: " + item.getName());
        } catch (Exception e) {
            setStatus("Import failed: " + e.getMessage());
        }
    }

    private void refreshLocalItems() throws Exception {
        if (itemDao == null || localItemsListView == null) {
            return;
        }

        List<String> items = itemDao.getAll().stream()
                .map(item -> item.getName() + "  •  " + item.getBrand() + "  •  " + item.getDefaultUnit())
                .toList();

        localItemsListView.setItems(FXCollections.observableArrayList(items));

        if (importedCountLabel != null) {
            importedCountLabel.setText(String.valueOf(items.size()));
        }
    }

    private void addLocalListItem(Item item) {
        if (localItemsListView == null) {
            return;
        }

        if (localItemsListView.getItems() == null) {
            localItemsListView.setItems(FXCollections.observableArrayList());
        }

        localItemsListView.getItems().add(
                item.getName() + "  •  " + item.getBrand() + "  •  " + item.getDefaultUnit()
        );

        if (importedCountLabel != null) {
            importedCountLabel.setText(String.valueOf(localItemsListView.getItems().size()));
        }
    }

    @FXML
    private void handleOpenSettings(ActionEvent event) {
        goSettings(event);
    }

    private void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}


