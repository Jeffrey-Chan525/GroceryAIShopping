package com.smartspend.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final Path DATABASE_DIR = Paths.get("database");
    private static final String DB_URL = "jdbc:sqlite:" + DATABASE_DIR.resolve("smartspend.db");
    private static boolean initialized = false;

    public static Connection getConnection() throws SQLException {
        ensureInitialized();
        return DriverManager.getConnection(DB_URL);
    }

    private static synchronized void ensureInitialized() {
        if (initialized) {
            return;
        }

        try {
            Files.createDirectories(DATABASE_DIR);
            Path dbPath = DATABASE_DIR.resolve("smartspend.db");
            boolean newDatabase = Files.notExists(dbPath);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                if (newDatabase) {
                    runSqlScript(connection, Paths.get("src/main/resources/db/schema.sql"));
                    runSqlScript(connection, Paths.get("src/main/resources/db/seed.sql"));
                }
            }
            initialized = true;
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private static void runSqlScript(Connection connection, Path scriptPath) throws IOException, SQLException {
        String sql = Files.readString(scriptPath, StandardCharsets.UTF_8);
        String[] statements = sql.split(";");
        for (String statementSql : statements) {
            String trimmed = statementSql.trim();
            if (trimmed.isBlank()) {
                continue;
            }
            try (Statement statement = connection.createStatement()) {
                statement.execute(trimmed);
            }
        }
    }
}
