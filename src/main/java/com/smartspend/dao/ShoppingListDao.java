package com.smartspend.dao;

import com.smartspend.model.ShoppingListEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListDao implements DAO<ShoppingListEntry> {
    Connection connection;
    public ShoppingListDao(Connection connection){
        this.connection = connection;
    }
    @Override
    public void insert(ShoppingListEntry value) {
        String query = "INSERT into shopping_list_items values (?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            int LIST_ITEM_ID= 1;
            int USER_ID = 2;
            int ITEM_ID = 3;
            int QUANTITY = 4;
            int UNIT = 5;
            int IS_COMPLETED = 6;
            int ADDED_DATE = 7;
            statement.setInt(LIST_ITEM_ID, value.getListItemId());
            statement.setInt(USER_ID, value.getUserId());
            statement.setInt(ITEM_ID, value.getItemId());
            statement.setDouble(QUANTITY, value.getQuantity());
            statement.setString(UNIT, value.getUnit());
            statement.setBoolean(IS_COMPLETED, value.isCompleted());
            statement.setString(ADDED_DATE, value.getAddedDate());
            statement.execute();
        }catch (SQLException e){
            System.err.print("An error has occurred while inserting into shopping_list_items: " + e);
        }
    }

    @Override
    public void update(ShoppingListEntry value) {
        String query = "UPDATE shopping_list_items SET " +
                "user_id = ?," +
                "item_id = ?," +
                "quantity = ?," +
                "unit = ?," +
                "is_completed = ?," +
                "added_date = ?" +
                "where list_item_id = ?";
    try {
            PreparedStatement statement = connection.prepareStatement(query);
            int USER_ID = 1;
            int ITEM_ID = 2;
            int QUANTITY = 3;
            int UNIT = 4;
            int IS_COMPLETED = 5;
            int ADDED_DATE = 6;
            int LIST_ITEM_ID = 7;
            statement.setInt(USER_ID, value.getUserId());
            statement.setInt(ITEM_ID, value.getItemId());
            statement.setDouble(QUANTITY, value.getQuantity());
            statement.setString(UNIT, value.getUnit());
            statement.setBoolean(IS_COMPLETED, value.isCompleted());
            statement.setString(ADDED_DATE, value.getAddedDate());
            statement.setInt(LIST_ITEM_ID, value.getListItemId());
            statement.execute();
        }catch (SQLException e){
        System.err.print("An error has occurred when updating the table shopping_list_items: " + e);
    }
    }

    @Override
    public void delete(ShoppingListEntry value) {
        String query = "DELETE FROM shopping_list_items where list_item_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            int LIST_ITEM_ID = 1;
            statement.setInt(LIST_ITEM_ID, value.getListItemId());
            statement.execute();
        } catch (SQLException e){
            System.err.print("An error has occurred when deleting an item from Shopping_list_items: " + e);
        }
    }

    @Override
    public List<ShoppingListEntry> getAll() {
        String query = "SELECT * FROM shopping_list_items";
        List<ShoppingListEntry> shoppingListEntries = new ArrayList<>();
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            int LIST_ITEM_ID = 1;
            int USER_ID = 2;
            int ITEM_ID = 3;
            int QUANTITY = 4;
            int UNIT = 5;
            int IS_COMPLETED = 6;
            int ADDED_DATE = 7;
            while (resultSet.next()){
                int listItemID = resultSet.getInt(LIST_ITEM_ID);
                int userID = resultSet.getInt(USER_ID);
                int itemID = resultSet.getInt(ITEM_ID);
                double quantity = resultSet.getDouble(QUANTITY);
                String unit = resultSet.getString(UNIT);
                boolean isCompleted = resultSet.getBoolean(IS_COMPLETED);
                String addedDate = resultSet.getString(ADDED_DATE);
                ShoppingListEntry shoppingListEntry = new ShoppingListEntry(listItemID, userID, itemID, quantity, unit, isCompleted, addedDate);
                shoppingListEntries.add(shoppingListEntry);
            }
        } catch (SQLException e ){
            System.err.print("An error has occurred while retrieving all items from the table shopping_list_items: " + e);
        }
        return shoppingListEntries;
    }

    @Override
    public ShoppingListEntry get(int id) {
        String query = "SELECT * FROM shopping_list_items WHERE list_item_id = ?";
        ShoppingListEntry shoppingListEntry = null;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            int LIST_ITEM_ID = 1;
            int USER_ID = 2;
            int ITEM_ID = 3;
            int QUANTITY = 4;
            int UNIT = 5;
            int IS_COMPLETED = 6;
            int ADDED_DATE = 7;
            statement.setInt(LIST_ITEM_ID, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                int listItemID = resultSet.getInt(LIST_ITEM_ID);
                int userID = resultSet.getInt(USER_ID);
                int itemID = resultSet.getInt(ITEM_ID);
                double quantity = resultSet.getDouble(QUANTITY);
                String unit = resultSet.getString(UNIT);
                boolean isCompleted = resultSet.getBoolean(IS_COMPLETED);
                String addedDate = resultSet.getString(ADDED_DATE);
                shoppingListEntry = new ShoppingListEntry(listItemID, userID, itemID, quantity, unit, isCompleted, addedDate);
            }
        } catch (SQLException e){
            System.err.print("An error has occurred while retrieving a single item from shopping_list_items: " + e);
        }
        return shoppingListEntry;
    }
}
