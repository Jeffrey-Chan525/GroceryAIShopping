package com.smartspend.controller;

import com.smartspend.dao.PantryDao;
import com.smartspend.model.PantryItem;
import com.smartspend.util.DatabaseManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PantryController extends BaseController {

    // FXML fields
    @FXML private TableView<PantryItem> pantryTable;
    @FXML private TableColumn<PantryItem, String> itemColumn;
    @FXML private TableColumn<PantryItem, String> quantityColumn;
    @FXML private TableColumn<PantryItem, String> bestBeforeColumn;
    @FXML private TableColumn<PantryItem, String> statusColumn;
    @FXML private TableColumn<PantryItem, String> suggestedUseColumn;
    @FXML private Label statusLabel;

    // Data
    private final ObservableList<PantryItem> pantryItems = FXCollections.observableArrayList();
    private PantryDao pantryDao;

    // Lifecycle

    @FXML
    public void initialize() {
        setupTableColumns();
        pantryTable.setItems(pantryItems);

        try {
            Connection connection = DatabaseManager.getConnection();
            pantryDao = new PantryDao(connection);
            loadPantryItems();
        } catch (Exception e) {
            showStatus("Could not connect to database: " + e.getMessage());
            loadDemoItems();
        }
    }

    // Table

    private void setupTableColumns() {
        itemColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getName()));

        quantityColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getQuantity()));

        bestBeforeColumn.setCellValueFactory(data -> {
            String expiry = data.getValue().getExpiryDate();
            return new SimpleStringProperty(
                    (expiry != null && !expiry.isBlank()) ? expiry : "—");
        });

        statusColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().isLowStock() ? "⚠ Low Stock" : "In Stock"));

        suggestedUseColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        suggestUse(data.getValue().getName())));
    }

    // Data

    private void loadPantryItems() {
        List<PantryItem> items = pantryDao.getAll();

        if (items.isEmpty()) {
            loadDemoItems();
            showStatus("No pantry items yet. Demo data shown. Click '+ Add Item' to get started.");
        } else {
            pantryItems.setAll(items);
            showStatus("Loaded " + items.size() + " pantry item(s) from database.");
        }
    }

    private void loadDemoItems() {
        pantryItems.setAll(
                new PantryItem(0, 1, "Rice 1kg",       "2 bags",   "Pantry", "2026-12-01", false),
                new PantryItem(0, 1, "Pasta 500g",      "3 packs",  "Pantry", "2026-10-01", false),
                new PantryItem(0, 1, "Olive Oil 500ml", "1 bottle", "Pantry", "2027-01-01", false),
                new PantryItem(0, 1, "Salt",            "500g",     "Pantry", "2028-01-01", false),
                new PantryItem(0, 1, "Milk 2L",         "1 bottle", "Dairy",  "2026-05-25", true)
        );
    }

    // Actions

    @FXML
    private void handleAddPantryItem() {
        Dialog<PantryItem> dialog = new Dialog<>();
        dialog.setTitle("Add Pantry Item");
        dialog.setHeaderText("Add an item you already have at home");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Rice, Pasta, Olive Oil");

        TextField quantityField = new TextField();
        quantityField.setPromptText("e.g. 1kg, 500ml, 2 cans");

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.setItems(FXCollections.observableArrayList(
                "Pantry", "Dairy", "Meat", "Fruit",
                "Vegetables", "Frozen", "Snacks", "Drinks"));
        categoryCombo.setValue("Pantry");

        TextField expiryField = new TextField();
        expiryField.setPromptText("e.g. " + LocalDate.now().plusMonths(3));

        CheckBox lowStockCheck = new CheckBox("Mark as low stock");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.add(new Label("Item name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryCombo, 1, 2);
        grid.add(new Label("Best before:"), 0, 3);
        grid.add(expiryField, 1, 3);
        grid.add(lowStockCheck, 1, 4);

        dialog.getDialogPane().setContent(grid);
        nameField.requestFocus();

        dialog.setResultConverter(button -> {
            if (button != addButton) return null;

            String name = nameField.getText() == null ? "" : nameField.getText().trim();
            String quantity = quantityField.getText() == null ? "" : quantityField.getText().trim();
            String category = categoryCombo.getValue();
            String expiry = expiryField.getText() == null ? "" : expiryField.getText().trim();
            boolean lowStock = lowStockCheck.isSelected();

            if (name.isBlank()) return null;
            if (quantity.isBlank()) quantity = "unit";

            return new PantryItem(0, 1, name, quantity, category, expiry, lowStock);
        });

        Optional<PantryItem> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        PantryItem newItem = result.get();

        if (pantryDao != null) {
            pantryDao.insert(newItem);
            // Reload from DB to get the real auto-generated pantry_id
            pantryItems.setAll(pantryDao.getAll());
            showStatus("Added: " + newItem.getName());
        } else {
            pantryItems.add(newItem);
            showStatus("Added to table (not saved — DB unavailable).");
        }
    }

    @FXML
    private void handleDeletePantryItem() {
        PantryItem selected = pantryTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No item selected", "Please select a pantry item to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Pantry Item");
        confirm.setHeaderText("Delete \"" + selected.getName() + "\"?");
        confirm.setContentText("This will permanently remove it from your pantry.");
        Optional<ButtonType> response = confirm.showAndWait();

        if (response.isEmpty() || response.get() != ButtonType.OK) return;

        pantryItems.remove(selected);

        if (pantryDao != null && selected.getPantryId() != 0) {
            pantryDao.delete(selected);
            showStatus("Deleted: " + selected.getName());
        }
    }

    // Helpers

    private String suggestUse(String itemName) {
        if (itemName == null) return "—";
        String lower = itemName.toLowerCase();

        if (lower.contains("rice")    || lower.contains("pasta"))   return "Base for meals";
        if (lower.contains("milk")    || lower.contains("yoghurt")) return "Breakfast / snacks";
        if (lower.contains("oil")     || lower.contains("salt"))    return "Cooking staple";
        if (lower.contains("egg"))                                   return "Breakfast / baking";
        if (lower.contains("bread"))                                 return "Sandwiches / toast";
        if (lower.contains("chicken") || lower.contains("beef"))    return "Main protein";

        return "General use";
    }

    /**
     * Updates the status label with a message.
     *
     * @param message the message to display
     */
    private void showStatus(String message) {
        if (statusLabel != null) statusLabel.setText(message);
        System.out.println("PantryController: " + message);
    }

    /**
     * Shows a simple information alert dialog.
     *
     * @param title   the alert title
     * @param message the alert message body
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
