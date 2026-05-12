package com.smartspend.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DEFAULT_DB_PATH = "smartspend.db";

    public static Connection getConnection() throws SQLException {
        String configuredPath = System.getenv("DATABASE_URL");

        if (configuredPath == null || configuredPath.isBlank()) {
            configuredPath = DEFAULT_DB_PATH;
        }

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + configuredPath);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }

        initialiseSchemaIfMissing(connection);

        return connection;
    }

    private static void initialiseSchemaIfMissing(Connection connection) throws SQLException {
        if (tableExists(connection, "users")) {
            return;
        }

        try (InputStream inputStream = DatabaseManager.class.getResourceAsStream("/db/schema.sql")) {
            if (inputStream == null) {
                throw new SQLException("Could not find /db/schema.sql in resources.");
            }

            String schemaSql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String[] statements = schemaSql.split(";");

            try (Statement statement = connection.createStatement()) {
                for (String sql : statements) {
                    String cleanedSql = sql.trim();

                    if (!cleanedSql.isEmpty()) {
                        statement.execute(cleanedSql);
                    }
                }
            }

        } catch (IOException e) {
            throw new SQLException("Could not read schema.sql.", e);
        }
    }

    private static boolean tableExists(Connection connection, String tableName) throws SQLException {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            return resultSet.next();
        }
    }
}