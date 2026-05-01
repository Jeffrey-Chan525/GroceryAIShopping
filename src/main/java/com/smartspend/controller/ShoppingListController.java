package com.smartspend.controller;

import com.smartspend.dao.ItemDao;
import com.smartspend.model.Item;
import com.smartspend.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class ShoppingListController extends BaseController {

    @FXML private TextField globalSearchField;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilterCombo;
    @FXML private ComboBox<String> storeFilterCombo;

    @FXML private TableView<Item> itemsTable;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, String> categoryColumn;
    @FXML private TableColumn<Item, String> brandColumn;
    @FXML private TableColumn<Item, String> unitColumn;

    private final ObservableList<Item> allItems = FXCollections.observableArrayList();
    private FilteredList<Item> filteredItems;

    private ItemDao itemDao;
    private boolean backendAvailable = false;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupFilters();
        loadItemsFromBackend();

        filteredItems = new FilteredList<>(allItems, item -> true);
        itemsTable.setItems(filteredItems);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        globalSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchField.setText(newValue);
            applyFilters();
        });
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("defaultUnit"));
    }

    private void setupFilters() {
        categoryFilterCombo.setItems(FXCollections.observableArrayList(
                "All categories",
                "Dairy",
                "Meat",
                "Pantry",
                "Fruit",
                "Vegetables",
                "Frozen",
                "Snacks",
                "Drinks",
                "Uncategorized"
        ));
        categoryFilterCombo.setValue("All categories");

        storeFilterCombo.setItems(FXCollections.observableArrayList(
                "All stores",
                "Aldi",
                "Coles",
                "Woolworths",
                "Manual Entry"
        ));
        storeFilterCombo.setValue("All stores");

        categoryFilterCombo.setButtonCell(createComboCell());
        categoryFilterCombo.setCellFactory(list -> createComboCell());

        storeFilterCombo.setButtonCell(createComboCell());
        storeFilterCombo.setCellFactory(list -> createComboCell());
    }

    private void loadItemsFromBackend() {
        try {
            Connection connection = DatabaseManager.getConnection();

            if (!itemsTableExists(connection)) {
                backendAvailable = false;
                itemDao = null;
                System.out.println("Backend connected, but items table does not exist. Using demo items only.");
                loadDemoItems();
                return;
            }

            itemDao = new ItemDao(connection);
            backendAvailable = true;

            List<Item> backendItems = itemDao.getAll();

            if (backendItems == null || backendItems.isEmpty()) {
                System.out.println("Backend items table exists but has no items. Loading demo shopping list items.");
                loadDemoItems();
                return;
            }

            allItems.setAll(backendItems);
            System.out.println("Loaded " + backendItems.size() + " shopping list item(s) from backend.");

        } catch (Exception e) {
            backendAvailable = false;
            itemDao = null;
            System.err.println("Failed to load shopping list items from backend: " + e.getMessage());
            loadDemoItems();
        }
    }

    private boolean itemsTableExists(Connection connection) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name='items'"
             )) {
            return resultSet.next();
        } catch (Exception e) {
            System.err.println("Could not check if items table exists: " + e.getMessage());
            return false;
        }
    }

    private void loadDemoItems() {
        allItems.setAll(
                new Item(1, "Whole milk 2L", "Dairy", "Aldi", "2L"),
                new Item(2, "Chicken breast 500g", "Meat", "Coles", "500g"),
                new Item(3, "Pasta 500g", "Pantry", "Aldi", "500g"),
                new Item(4, "Greek yoghurt 1kg", "Dairy", "Woolworths", "1kg")
        );
    }

    @FXML
    private void handleAddItem() {
        Dialog<Item> dialog = new Dialog<>();
        dialog.setTitle("Add Item");
        dialog.setHeaderText("Add a grocery item to your shopping list");

        ButtonType addButtonType = new ButtonType("Add Item", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Oat milk 1L");

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.setItems(FXCollections.observableArrayList(
                "Dairy", "Meat", "Pantry", "Fruit", "Vegetables", "Frozen", "Snacks", "Drinks", "Uncategorized"
        ));
        categoryCombo.setValue("Uncategorized");

        ComboBox<String> storeCombo = new ComboBox<>();
        storeCombo.setItems(FXCollections.observableArrayList(
                "Aldi", "Coles", "Woolworths", "Manual Entry"
        ));
        storeCombo.setValue("Manual Entry");

        TextField unitField = new TextField();
        unitField.setPromptText("e.g. 1L, 500g, each");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        grid.add(new Label("Item name"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Category"), 0, 1);
        grid.add(categoryCombo, 1, 1);
        grid.add(new Label("Store / brand"), 0, 2);
        grid.add(storeCombo, 1, 2);
        grid.add(new Label("Unit"), 0, 3);
        grid.add(unitField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == addButtonType) {
                String name = nameField.getText() == null ? "" : nameField.getText().trim();
                String category = categoryCombo.getValue();
                String brand = storeCombo.getValue();
                String unit = unitField.getText() == null ? "" : unitField.getText().trim();

                if (name.isBlank()) {
                    return null;
                }

                if (unit.isBlank()) {
                    unit = "each";
                }

                return new Item(generateNextItemId(), name, category, brand, unit);
            }

            return null;
        });

        Optional<Item> result = dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        Item newItem = result.get();
        allItems.add(newItem);
        applyFilters();

        saveItemToBackendIfAvailable(newItem);
    }

    @FXML
    private void handleBulkImport() {
        int nextId = generateNextItemId();

        Item[] demoImportItems = {
                new Item(nextId, "Eggs 12 pack", "Dairy", "Aldi", "12 pack"),
                new Item(nextId + 1, "Bananas 1kg", "Fruit", "Woolworths", "1kg"),
                new Item(nextId + 2, "Rice 1kg", "Pantry", "Coles", "1kg")
        };

        for (Item item : demoImportItems) {
            allItems.add(item);
            saveItemToBackendIfAvailable(item);
        }

        applyFilters();
        System.out.println("Bulk import added " + demoImportItems.length + " item(s).");
    }

    private void saveItemToBackendIfAvailable(Item item) {
        if (!backendAvailable || itemDao == null) {
            System.out.println("Item added to UI only because backend items table is unavailable: " + item.getName());
            return;
        }

        try {
            itemDao.insert(item);
            System.out.println("Added item to backend: " + item.getName());
        } catch (Exception e) {
            System.err.println("Item added to UI, but backend insert failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleFiltersChanged() {
        applyFilters();
    }

    private void applyFilters() {
        if (filteredItems == null) {
            return;
        }

        String searchText = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String selectedCategory = categoryFilterCombo.getValue();
        String selectedStore = storeFilterCombo.getValue();

        filteredItems.setPredicate(item -> {
            boolean matchesSearch = searchText.isBlank()
                    || safeLower(item.getName()).contains(searchText)
                    || safeLower(item.getBrand()).contains(searchText)
                    || safeLower(item.getCategory()).contains(searchText)
                    || safeLower(item.getDefaultUnit()).contains(searchText);

            boolean matchesCategory = selectedCategory == null
                    || selectedCategory.equals("All categories")
                    || selectedCategory.equalsIgnoreCase(item.getCategory());

            boolean matchesStore = selectedStore == null
                    || selectedStore.equals("All stores")
                    || selectedStore.equalsIgnoreCase(item.getBrand());

            return matchesSearch && matchesCategory && matchesStore;
        });
    }

    private int generateNextItemId() {
        return allItems.stream()
                .mapToInt(Item::getId)
                .max()
                .orElse(0) + 1;
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }

    private javafx.scene.control.ListCell<String> createComboCell() {
        return new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }

                setStyle("-fx-text-fill: #183f2e; -fx-font-weight: 700;");
            }
        };
    }
}