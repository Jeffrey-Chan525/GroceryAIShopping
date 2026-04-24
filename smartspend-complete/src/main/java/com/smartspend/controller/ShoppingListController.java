package com.smartspend.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ShoppingListController {

    public static class Item {
        private final SimpleStringProperty name;
        private final SimpleDoubleProperty price;

        public Item(String name, double price) {
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleDoubleProperty(price);
        }

        public String getName() { return name.get(); }
        public double getPrice() { return price.get(); }
    }

    @FXML private TextField itemField;
    @FXML private TextField priceField;
    @FXML private TableView<Item> tableView;
    @FXML private TableColumn<Item, String> itemCol;
    @FXML private TableColumn<Item, Number> priceCol;
    @FXML private Label totalLabel;

    private final ObservableList<Item> items = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        itemCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()));

        tableView.setItems(items);
    }

    @FXML
    private void handleAddItem() {
        try {
            String name = itemField.getText();
            double price = Double.parseDouble(priceField.getText());

            items.add(new Item(name, price));
            itemField.clear();
            priceField.clear();

            updateTotal();
        } catch (Exception e) {
            totalLabel.setText("Invalid input");
        }
    }

    private void updateTotal() {
        double total = items.stream().mapToDouble(Item::getPrice).sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }
}