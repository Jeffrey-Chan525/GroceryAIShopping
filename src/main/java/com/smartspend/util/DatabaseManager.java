package com.smartspend.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
// see how to setup the .env file here: docs/setup/environment-variables-SETUP.md
    private static final String LOCAL_DB_URL = System.getenv("DATABASE_URL");
    private static final String DB_URL = "jdbc:sqlite:" + LOCAL_DB_URL;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
