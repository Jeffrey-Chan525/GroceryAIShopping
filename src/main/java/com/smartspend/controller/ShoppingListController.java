package com.smartspend.controller;

import com.smartspend.dao.ItemDao;
import com.smartspend.model.Item;
import com.smartspend.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.util.List;

public class ShoppingListController extends BaseController {

    @FXML private TableView<Item> itemsTable;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, String> categoryColumn;
    @FXML private TableColumn<Item, String> brandColumn;
    @FXML private TableColumn<Item, String> unitColumn;

    @FXML
    public void initialize() {
        setupTable();
        loadItems();
    }

    private void setupTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("defaultUnit"));
    }

    private void loadItems() {
        try {
            Connection connection = DatabaseManager.getConnection();
            ItemDao itemDao = new ItemDao(connection);

            List<Item> items = itemDao.getAll();
            itemsTable.setItems(FXCollections.observableArrayList(items));

            System.out.println("Loaded " + items.size() + " shopping list item(s) from backend.");
        } catch (Exception e) {
            System.err.println("Failed to load shopping list items from backend: " + e.getMessage());
            loadDemoItems();
        }
    }

    private void loadDemoItems() {
        itemsTable.setItems(FXCollections.observableArrayList(
                new Item(1, "Whole milk 2L", "Dairy", "Aldi", "2L"),
                new Item(2, "Chicken breast 500g", "Meat", "Coles", "500g"),
                new Item(3, "Pasta 500g", "Pantry", "Aldi", "500g"),
                new Item(4, "Greek yoghurt 1kg", "Dairy", "Woolworths", "1kg")
        ));

        System.out.println("Loaded demo items because backend/database load failed.");
    }
}