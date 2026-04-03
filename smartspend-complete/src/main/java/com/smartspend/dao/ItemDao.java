package com.smartspend.dao;

import com.smartspend.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            log.error("An sqliteException has occurred", e);
        }
    }

    @Override
    public void update(Item value) {
        String query = "UPDATE items SET ";
    }

    @Override
    public void delete(Item value) {

    }

    @Override
    public List<Item> getAll() {
        return List.of();
    }

    @Override
    public Item get(int id) {
        return null;
    }


}
