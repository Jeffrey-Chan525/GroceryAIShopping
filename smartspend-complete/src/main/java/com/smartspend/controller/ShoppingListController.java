package com.smartspend.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ShoppingListController {

    public static class ShoppingItem {
        private final SimpleStringProperty name;
        private final SimpleStringProperty category;
        private final SimpleStringProperty quantity;
        private final SimpleStringProperty bestPrice;
        private final SimpleStringProperty store;
        private final SimpleStringProperty status;
        private final SimpleStringProperty actions;

        public ShoppingItem(String name, String category, String quantity,
                            String bestPrice, String store, String status, String actions) {
            this.name = new SimpleStringProperty(name);
            this.category = new SimpleStringProperty(category);
            this.quantity = new SimpleStringProperty(quantity);
            this.bestPrice = new SimpleStringProperty(bestPrice);
            this.store = new SimpleStringProperty(store);
            this.status = new SimpleStringProperty(status);
            this.actions = new SimpleStringProperty(actions);
        }

        public String getName() { return name.get(); }
        public String getCategory() { return category.get(); }
        public String getQuantity() { return quantity.get(); }
        public String getBestPrice() { return bestPrice.get(); }
        public String getStore() { return store.get(); }
        public String getStatus() { return status.get(); }
        public String getActions() { return actions.get(); }
    }

    @FXML private Button btnAddItem;
    @FXML private Button btnSaveItem;
    @FXML private Button btnCancelAdd;

    @FXML private VBox addItemForm;

    @FXML private TextField txtItemName;
    @FXML private TextField txtQty;
    @FXML private ComboBox<String> cmbCategory;
    @FXML private ComboBox<String> cmbUnit;

    @FXML private TableView<ShoppingItem> itemsTable;
    @FXML private TableColumn<ShoppingItem, String> colName;
    @FXML private TableColumn<ShoppingItem, String> colCategory;
    @FXML private TableColumn<ShoppingItem, String> colQty;
    @FXML private TableColumn<ShoppingItem, String> colBestPrice;
    @FXML private TableColumn<ShoppingItem, String> colStore;
    @FXML private TableColumn<ShoppingItem, String> colStatus;
    @FXML private TableColumn<ShoppingItem, String> colActions;

    @FXML private Label lblTotalItems;
    @FXML private Label lblPendingItems;
    @FXML private Label lblBasketTotal;
    @FXML private Label lblBestStore;
    @FXML private Label lblWeeklyBudget;
    @FXML private Label lblBudgetLeft;
    @FXML private Label lblAldiTotal;
    @FXML private Label lblColesTotal;
    @FXML private Label lblWooliesTotal;
    @FXML private Label lblRecommendation;

    private final ObservableList<ShoppingItem> items = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colCategory.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        colQty.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getQuantity()));
        colBestPrice.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBestPrice()));
        colStore.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStore()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        colActions.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getActions()));

        itemsTable.setItems(items);

        btnAddItem.setOnAction(event -> showAddForm());
        btnCancelAdd.setOnAction(event -> hideAddForm());
        btnSaveItem.setOnAction(event -> saveItem());

        seedDemoItems();
        updateSummary();
    }

    private void showAddForm() {
        addItemForm.setVisible(true);
        addItemForm.setManaged(true);
    }

    private void hideAddForm() {
        addItemForm.setVisible(false);
        addItemForm.setManaged(false);
        txtItemName.clear();
        txtQty.clear();
        cmbCategory.getSelectionModel().clearSelection();
        cmbUnit.getSelectionModel().clearSelection();
    }

    private void saveItem() {
        String name = txtItemName.getText();
        String qty = txtQty.getText();
        String category = cmbCategory.getValue();
        String unit = cmbUnit.getValue();

        if (name == null || name.isBlank()) {
            lblRecommendation.setText("Enter an item name first.");
            return;
        }

        if (qty == null || qty.isBlank()) {
            qty = "1";
        }

        if (category == null) {
            category = "Other";
        }

        if (unit == null) {
            unit = "each";
        }

        items.add(new ShoppingItem(
                name,
                category,
                qty + " " + unit,
                "$0.00",
                "Pending",
                "Needed",
                "Edit"
        ));

        hideAddForm();
        updateSummary();
        lblRecommendation.setText("Item added to shopping list.");
    }

    private void seedDemoItems() {
        items.add(new ShoppingItem("Whole milk 2L", "Dairy", "2 L", "$2.80", "Aldi", "In cart", "Edit"));
        items.add(new ShoppingItem("Chicken breast 500g", "Meat", "1 pack", "$6.49", "Coles", "In cart", "Edit"));
        items.add(new ShoppingItem("Pasta 500g", "Pantry", "3 packs", "$1.20", "Aldi", "Pending", "Edit"));
    }

    private void updateSummary() {
        int total = items.size();

        lblTotalItems.setText(String.valueOf(total));
        lblPendingItems.setText(total + " listed");
        lblBasketTotal.setText("$10.49");
        lblBestStore.setText("best at Aldi");
        lblWeeklyBudget.setText("$120");
        lblBudgetLeft.setText("$109.51 remaining");

        lblAldiTotal.setText("$4.00");
        lblColesTotal.setText("$6.49");
        lblWooliesTotal.setText("$0.00");
        lblRecommendation.setText("Aldi currently has the best basket value.");
    }
}