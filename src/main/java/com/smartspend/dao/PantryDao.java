package com.smartspend.dao;

import com.smartspend.model.PantryItem;

import java.sql.*;

/**
 * Data Access Object for pantry items
 */
public class PantryDao implements DAO<PantryItem> {

    private final Connection connection;

    // Column constants
    private static final int PANTRY_ID   = 1;
    private static final int USER_ID     = 2;
    private static final int NAME        = 3;
    private static final int QUANTITY    = 4;
    private static final int CATEGORY    = 5;
    private static final int EXPIRY_DATE = 6;
    private static final int IS_LOW_STOCK = 7;

    /**
     * Constructor
     */
    public PantryDao(Connection connection) {
        this.connection = connection;
        createTable();
    }

    // Table

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS pantry_items (" +
                "pantry_id    INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id      INTEGER NOT NULL, " +
                "name         TEXT NOT NULL, " +
                "quantity     TEXT, " +
                "category     TEXT, " +
                "expiry_date  TEXT, " +
                "is_low_stock INTEGER DEFAULT 0)";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("PantryDao: Could not create pantry_items table: " + e.getMessage());
        }
    }

    // DAO interface methods

    /**
     * Inserts a pantry item into the database
     * @param value
     */
    @Override
    public void insert(PantryItem value) {
        String query = "INSERT INTO pantry_items " +
                "(user_id, name, quantity, category, expiry_date, is_low_stock) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,     value.getUserId());
            statement.setString(2,  value.getName());
            statement.setString(3,  value.getQuantity());
            statement.setString(4,  value.getCategory());
            statement.setString(5,  value.getExpiryDate());
            statement.setBoolean(6, value.isLowStock());
            statement.execute();
        } catch (SQLException e) {
            System.err.println("PantryDao: Error inserting pantry item: " + e.getMessage());
        }
    }

    /**
     * Retrieves all pantry items from the database.
     * @return a list of all PantryItem objects
     */
    @Override
    public java.util.List<PantryItem> getAll() {
        String query = "SELECT * FROM pantry_items";
        java.util.List<PantryItem> items = new java.util.ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                items.add(mapResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("PantryDao: Error retrieving all pantry items: " + e.getMessage());
        }
        return items;
    }

    /**
     * Updates a pantry item in the database
     * @param value the instance of the object
     */
    @Override
    public void update(PantryItem value) {
        String query = "UPDATE pantry_items SET " +
                "name = ?, " +
                "quantity = ?, " +
                "category = ?, " +
                "expiry_date = ?, " +
                "is_low_stock = ? " +
                "WHERE pantry_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,  value.getName());
            statement.setString(2,  value.getQuantity());
            statement.setString(3,  value.getCategory());
            statement.setString(4,  value.getExpiryDate());
            statement.setBoolean(5, value.isLowStock());
            statement.setInt(6,     value.getPantryId());
            statement.execute();
        } catch (SQLException e) {
            System.err.println("PantryDao: Error updating pantry item: " + e.getMessage());
        }
    }

    /**
     * Deletes a pantry item from the database
     * @param value the instance of the object
     */
    @Override
    public void delete(PantryItem value) {
        String query = "DELETE FROM pantry_items WHERE pantry_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, value.getPantryId());
            statement.execute();
        } catch (SQLException e) {
            System.err.println("PantryDao: Error deleting pantry item: " + e.getMessage());
        }
    }

    /**
     * Retrieves all pantry items from the database.
     * @param id the id of a record in the table
     * @return
     */
    @Override
    public PantryItem get(int id) {
        String query = "SELECT * FROM pantry_items WHERE pantry_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("PantryDao: Error retrieving pantry item by id: " + e.getMessage());
        }
        return null;
    }

    // Helper

    /**
     * Maps a ResultSet to a PantryItem object
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private PantryItem mapResultSet(ResultSet resultSet) throws SQLException {
        return new PantryItem(
                resultSet.getInt(PANTRY_ID),
                resultSet.getInt(USER_ID),
                resultSet.getString(NAME),
                resultSet.getString(QUANTITY),
                resultSet.getString(CATEGORY),
                resultSet.getString(EXPIRY_DATE),
                resultSet.getBoolean(IS_LOW_STOCK)
        );
    }
}
