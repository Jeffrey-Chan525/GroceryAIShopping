package com.smartspend.DAL;


import com.smartspend.dao.ItemDao;
import com.smartspend.model.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestItemDao {

    private static final Connection connection = _initConnection();
    private static final ItemDao itemDao = new ItemDao(connection);
    private static final Logger log = LoggerFactory.getLogger(TestItemDao.class);

    public static Connection _initConnection() {
        // using in memory database for easy clean up
        String url = "jdbc:sqlite::memory:";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url);
            if (connection != null) {
                System.out.print("Connected to sqlite in memory database");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    @BeforeAll
    static void BeforeAll(){
        // creating the table
        String query = "CREATE TABLE items ("
                + " item_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "category TEXT NOT NULL,"
                + "brand TEXT,"
                + "default_unit TEXT NOT NULL"
                + ");";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(query);
        } catch (SQLException e){
            log.error("e: ", e);
        }
    }

    @Test
    public void testInsert() {
        Item item = new Item(1,"toilet paper", "Household Essentials","Quilton","roll");
        itemDao.insert(item);
    }
}
