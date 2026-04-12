package com.smartspend.dao;

import com.smartspend.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {
    private final Connection connection;

    public ItemDao(Connection connection) {
        this.connection = connection;
    }

    public void insertItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (name, category, brand, default_unit) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getCategory());
            statement.setString(3, item.getBrand());
            statement.setString(4, item.getDefaultUnit());
            statement.executeUpdate();
        }
    }

    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT item_id, name, category, brand, default_unit FROM items ORDER BY item_id DESC";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                items.add(new Item(
                        resultSet.getInt("item_id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getString("brand"),
                        resultSet.getString("default_unit")
                ));
            }
        }
        return items;
    }
}
