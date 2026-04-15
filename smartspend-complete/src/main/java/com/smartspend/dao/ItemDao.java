package com.smartspend.dao;

import com.smartspend.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDao implements DAO<Item> {
    private static final Logger log = LoggerFactory.getLogger(ItemDao.class);
    Connection connection;

    /**
     * instantiates with the default sqliteConnection instance
     */
    public ItemDao(){
        this.connection = SqliteConnection.getInstance();
    }

    /**
     * Instantiates with the connection you specify
     * @param connection the connection to the database
     */
    public ItemDao(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Item value) {
        String query = "INSERT INTO items (name, category, brand, default_unit) VALUES (?,?,?,?)";
        int NAME = 1;
        int CATEGORY = 2;
        int BRAND = 3;
        int DEFAULT_UNIT = 4;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(NAME, value.getName());
            statement.setString(CATEGORY, value.getCategory());
            statement.setString(BRAND, value.getBrand());
            statement.setString(DEFAULT_UNIT, value.getDefaultUnit());
            statement.execute();

        } catch (SQLException e){
            log.error("An sqliteException has occurred when inserting a new Item: ", e);
        }
    }

    @Override
    public void update(Item value) {
        String query = "UPDATE items " +
                "SET name = ?, " +
                "category = ?, " +
                "brand = ?, " +
                "default_unit = ? " +
                "WHERE item_id = ?";
        int NAME = 1;
        int CATEGORY = 2;
        int BRAND = 3;
        int DEFAULT_UNIT = 4;
        int ITEM_ID = 5;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(NAME, value.getName());
            statement.setString(CATEGORY, value.getCategory());
            statement.setString(BRAND, value.getBrand());
            statement.setString(DEFAULT_UNIT, value.getDefaultUnit());
            statement.setInt(ITEM_ID, value.getId());
            statement.execute();
        } catch (SQLException e ){
            log.error("An error has occurred when updating an Item: ", e);
        }

    }

    @Override
    public void delete(Item value) {
        String query = "DELETE FROM items WHERE item_id = ?";
        int ITEM_ID = 1;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(ITEM_ID, value.getId());
        } catch (SQLException e){
            log.error("An error has occurred when deleting an Item: ", e);
        }
    }

    @Override
    public List<Item> getAll() {
        String query = "SELECT * from items";
        int ITEM_ID = 1;
        int NAME = 2;
        int CATEGORY = 3;
        int BRAND = 4;
        int DEFAULT_UNIT = 5;
        List<Item> items = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int item_id = resultSet.getInt(ITEM_ID);
                String name = resultSet.getString(NAME);
                String category = resultSet.getString(CATEGORY);
                String brand = resultSet.getString(BRAND);
                String defaultUnit = resultSet.getString(DEFAULT_UNIT);
                Item currentItem = new Item(item_id, name, category, brand, defaultUnit);
                items.add(currentItem);
            }
        } catch (SQLException e){
            log.error("An error has occurred while getting the table: ", e);
        }
        return items;
    }

    @Override
    public Item get(int id) {
        String query = "SELECT * FROM items WHERE item_id = ?";
        int ITEM_ID = 1;
        int NAME = 2;
        int CATEGORY = 3;
        int BRAND = 4;
        int DEFAULT_UNIT = 5;
        Item item = null;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                int item_id = resultSet.getInt(ITEM_ID);
                String name = resultSet.getString(NAME);
                String category = resultSet.getString(CATEGORY);
                String brand = resultSet.getString(BRAND);
                String defaultUnit = resultSet.getString(DEFAULT_UNIT);
                item = new Item(item_id, name, category, brand, defaultUnit);
            }
        } catch (SQLException e){
            log.error("An error has occurred while getting an Item: ", e);
        }
        return item;
    }


}
