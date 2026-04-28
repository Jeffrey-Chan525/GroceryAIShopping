package com.smartspend.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MockSQLiteConnection {
    public static Connection mockConnection = _initConnection();
    private static Connection _initConnection() {
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
}
