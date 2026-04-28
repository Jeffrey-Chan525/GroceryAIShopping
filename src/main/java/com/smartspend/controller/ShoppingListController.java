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

public class ShoppingListController extends BaseController {
    @FXML private TableView<Item> itemsTable;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, String> categoryColumn;
    @FXML private TableColumn<Item, String> brandColumn;
    @FXML private TableColumn<Item, String> unitColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("defaultUnit"));

        try {
            Connection connection = DatabaseManager.getConnection();
            ItemDao itemDao = new ItemDao(connection);
            itemsTable.setItems(FXCollections.observableArrayList(itemDao.getAll()));
        } catch (Exception ignored) {
        }
    }
}
