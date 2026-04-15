package com.smartspend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Dashboard controller
 * (still testing some stuff)
 */
public class DashboardController {

    @FXML private TableView<ShoppingItem> itemsTable;
    @FXML private TableColumn<ShoppingItem, String> colName;
    @FXML private TableColumn<ShoppingItem, String> colQty;
    @FXML private TableColumn<ShoppingItem, String> colPrice;
    @FXML private TableColumn<ShoppingItem, String> colStore;
    @FXML private TableColumn<ShoppingItem, String> colStatus;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(c -> c.getValue().nameProperty());
        colQty.setCellValueFactory(c -> c.getValue().qtyProperty());
        colPrice.setCellValueFactory(c -> c.getValue().bestPriceProperty());
        colStore.setCellValueFactory(c -> c.getValue().storeProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        // Coloured status pill
        colStatus.setCellFactory(col -> new TableCell<ShoppingItem, String>() {
            private final Label pill = new Label();
            { pill.getStyleClass().add("pill"); }

            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    pill.setText(value);
                    pill.getStyleClass().removeAll("pill-in-cart", "pill-pending");
                    if ("In cart".equalsIgnoreCase(value)) {
                        pill.getStyleClass().add("pill-in-cart");
                    } else {
                        pill.getStyleClass().add("pill-pending");
                    }
                    setGraphic(pill);
                }
            }
        });

        // Green qty column
        colQty.setCellFactory(col -> new TableCell<ShoppingItem, String>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(value);
                    if (!getStyleClass().contains("qty-cell")) getStyleClass().add("qty-cell");
                }
            }
        });

        ObservableList<ShoppingItem> rows = FXCollections.observableArrayList(
                new ShoppingItem("Whole milk 2L", "×2", "$2.80", "Aldi", "In cart"),
                new ShoppingItem("Chicken breast 500g", "×1", "$6.49", "Coles", "In cart"),
                new ShoppingItem("Pasta 500g", "×3", "$1.20", "Aldi", "Pending"),
                new ShoppingItem("Greek yoghurt 1kg", "×1", "$3.90", "Woolworths", "Pending")
        );
        itemsTable.setItems(rows);
    }
}
