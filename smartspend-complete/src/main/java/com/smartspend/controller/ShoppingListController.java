package com.smartspend.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ShoppingListController {

    public static class ShoppingItem {
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty quantity;
        private final SimpleDoubleProperty price;

        public ShoppingItem(String name, int quantity, double price) {
            this.name = new SimpleStringProperty(name);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.price = new SimpleDoubleProperty(price);
        }

        public String getName() {
            return name.get();
        }

        public int getQuantity() {
            return quantity.get();
        }

        public double getPrice() {
            return price.get();
        }

        public double getSubtotal() {
            return quantity.get() * price.get();
        }
    }

    @FXML private TextField itemField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private TableView<ShoppingItem> tableView;
    @FXML private TableColumn<ShoppingItem, String> itemColumn;
    @FXML private TableColumn<ShoppingItem, Number> quantityColumn;
    @FXML private TableColumn<ShoppingItem, Number> priceColumn;
    @FXML private Label totalLabel;
    @FXML private Label statusLabel;

    private final ObservableList<ShoppingItem> items = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        itemColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()));
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()));

        tableView.setItems(items);
    }

    @FXML
    private void handleAddItem() {
        try {
            String name = itemField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            if (name.isEmpty() || quantity <= 0 || price < 0) {
                statusLabel.setText("Enter valid item details.");
                return;
            }

            items.add(new ShoppingItem(name, quantity, price));

            itemField.clear();
            quantityField.clear();
            priceField.clear();

            updateTotal();
            statusLabel.setText("Item added.");
        } catch (NumberFormatException e) {
            statusLabel.setText("Quantity and price must be numbers.");
        }
    }

    @FXML
    private void handleRemoveSelected() {
        ShoppingItem selected = tableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Select an item to remove.");
            return;
        }

        items.remove(selected);
        updateTotal();
        statusLabel.setText("Item removed.");
    }

    private void updateTotal() {
        double total = items.stream().mapToDouble(ShoppingItem::getSubtotal).sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }
}