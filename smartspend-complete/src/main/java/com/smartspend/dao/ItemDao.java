package com.smartspend.dao;

import com.smartspend.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class ItemDao implements DAO<Item> {
    private static final Logger log = LoggerFactory.getLogger(ItemDao.class);
    Connection connection = SqliteConnection.getInstance();

    @Override
    public void create(Item value) {
        String query = "INSERT INTO items (name, category, brand, default_unit) VALUES (?,?,?,?)";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e){
            log.error("An sqliteException has occurred", e);
        }
    }

    @Override
    public void update(Item value) {

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
