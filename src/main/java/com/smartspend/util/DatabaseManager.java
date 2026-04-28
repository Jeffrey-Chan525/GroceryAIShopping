package com.smartspend.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String LOCAL_DB_URL = System.getenv("DATABASE_URL");
    private static final String DB_URL = "jdbc:sqlite:" + LOCAL_DB_URL;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
