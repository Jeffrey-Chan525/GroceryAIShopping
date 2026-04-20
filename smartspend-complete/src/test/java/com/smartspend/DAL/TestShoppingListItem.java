package com.smartspend.DAL;

import com.smartspend.dao.ShoppingListDao;
import com.smartspend.model.ShoppingListEntry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

public class TestShoppingListItem {
    private static final Connection MOCK_CONNECTION = MockSQLiteConnection.mockConnection;
    private static final ShoppingListDao shoppingListDao = new ShoppingListDao(MOCK_CONNECTION);

    @BeforeAll
    static void BeforeAll(){
        String createTable = "CREATE TABLE shopping_list_items (" +
                "    list_item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    user_id INTEGER NOT NULL," +
                "    item_id INTEGER NOT NULL," +
                "    quantity REAL NOT NULL DEFAULT 1," +
                "    unit TEXT NOT NULL," +
                "    is_completed INTEGER NOT NULL DEFAULT 0 CHECK (is_completed IN (0,1))," +
                "    added_date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE," +
                "    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE" +
                ");";

        try{
            Statement statement = MOCK_CONNECTION.createStatement();
            statement.execute(createTable);
        }catch (SQLException e){
            System.err.print("failed to initialize table");
        }
    }

    @BeforeEach
    void ClearTable(){
        String clearTableQuery = "DELETE FROM shopping_list_items";
        try{
            Statement statement = MOCK_CONNECTION.createStatement();
            statement.execute(clearTableQuery);
        } catch (SQLException e){
            System.err.print("failed to delete all records from all records before test");
        }
    }

    // this is the dummy data values
    private final int EXPECTED_LIST_ITEM_ID = 1;
    private final int EXPECTED_USER_ID = 2;
    private final int EXPECTED_ITEM_ID = 3;
    private final double EXPECTED_QUANTITY = 4.5;
    private final String EXPECTED_UNIT = "gallons";
    private final boolean EXPECTED_iS_COMPLETED = true;
    private final String EXPECTED_ADDED_DATE = "2/2/2026";
    private final ShoppingListEntry EXPECTED_SHOPPING_LIST_ENTRY_OBJECT = new ShoppingListEntry(EXPECTED_LIST_ITEM_ID, EXPECTED_USER_ID, EXPECTED_ITEM_ID, EXPECTED_QUANTITY, EXPECTED_UNIT, EXPECTED_iS_COMPLETED, EXPECTED_ADDED_DATE);

    // the following is the column values
    // the values may change as the query as the queries may be formatted differently
    private final int LIST_ITEM_ID = 1;
    private final int USER_ID = 2;
    private final int ITEM_ID = 3;
    private final int QUANTITY = 4;
    private final int UNIT = 5;
    private final int IS_COMPLETED = 6;
    private final int ADDED_DATE = 7;
    private void insertDummydata(){
        String insertDummyDataQuery = "INSERT INTO shopping_list_items (list_item_id, user_id, item_id, quantity, unit, is_completed, added_date) VALUES  (?,?,?,?,?,?,?)";


        try{
            PreparedStatement preparedStatement = MOCK_CONNECTION.prepareStatement(insertDummyDataQuery);
            preparedStatement.setInt(LIST_ITEM_ID, EXPECTED_LIST_ITEM_ID);
            preparedStatement.setInt(USER_ID, EXPECTED_USER_ID);
            preparedStatement.setInt(ITEM_ID, EXPECTED_ITEM_ID);
            preparedStatement.setDouble(QUANTITY, EXPECTED_QUANTITY);
            preparedStatement.setString(UNIT, EXPECTED_UNIT);
            preparedStatement.setBoolean(IS_COMPLETED, EXPECTED_iS_COMPLETED);
            preparedStatement.setString(ADDED_DATE, EXPECTED_ADDED_DATE);
            preparedStatement.execute();
        }catch (SQLException e){
            System.err.print("failed to insert dummy data");
        }
    }

    private ShoppingListEntry turnResultSetToShoppingListEntryObject(ResultSet resultSet) throws SQLException{
        int list_item_id = resultSet.getInt(LIST_ITEM_ID);
        int user_id = resultSet.getInt(USER_ID);
        int item_id = resultSet.getInt(ITEM_ID);
        double quantity = resultSet.getDouble(QUANTITY);
        String unit = resultSet.getString(UNIT);
        boolean is_completed = resultSet.getBoolean(IS_COMPLETED);
        String added_date = resultSet.getString(ADDED_DATE);

        return new ShoppingListEntry(list_item_id, user_id,item_id, quantity, unit, is_completed, added_date);
    }

    @Test
    void TestInsert(){
        // inserting into the table with the DAO object
       shoppingListDao.insert(EXPECTED_SHOPPING_LIST_ENTRY_OBJECT);

       // retrieving the inserted data
       String retrieveStatement = "SELECT * FROM shopping_list_items where list_item_id = ?";
       ShoppingListEntry actualValue = null;
       try{
           PreparedStatement statement = MOCK_CONNECTION.prepareStatement(retrieveStatement);
           statement.setInt(LIST_ITEM_ID, 1);
           ResultSet resultSet = statement.executeQuery();
           actualValue = turnResultSetToShoppingListEntryObject(resultSet);
       }catch (SQLException e){
           System.err.print("an error occurred when retrieving the dummy data from the table");
       }

        Assertions.assertEquals(EXPECTED_SHOPPING_LIST_ENTRY_OBJECT, actualValue);
    }

    @Test
    void TestUpdate() {
        insertDummydata();

        // this shoppingListEntry object will be our expected value for the test
        // the only difference is updatedQuantity
        double updatedQuantity = 5.5;
        ShoppingListEntry expectedValue = new ShoppingListEntry(EXPECTED_LIST_ITEM_ID, EXPECTED_USER_ID, EXPECTED_ITEM_ID, updatedQuantity, EXPECTED_UNIT, EXPECTED_iS_COMPLETED, EXPECTED_ADDED_DATE);
        shoppingListDao.update(expectedValue);

        // retrieving the actual updated value in the database
        String retrieveUpdatedData = "SELECT * FROM main.shopping_list_items WHERE list_item_id = 1";
        ShoppingListEntry actualValue = null;
        try {
            Statement statement = MOCK_CONNECTION.createStatement();
            ResultSet resultSet = statement.executeQuery(retrieveUpdatedData);
            actualValue = turnResultSetToShoppingListEntryObject(resultSet);
        } catch (SQLException e) {
            System.err.print("error encountered when retrieving updated data");
        }
        Assertions.assertEquals(expectedValue, actualValue);
    }
    @Test
    void testDelete(){
        insertDummydata();

        shoppingListDao.delete(EXPECTED_SHOPPING_LIST_ENTRY_OBJECT);

        String retrieveTableQuery = "SELECT * FROM shopping_list_items";

        try{
            Statement statement = MOCK_CONNECTION.createStatement();
            ResultSet resultSet = statement.executeQuery(retrieveTableQuery);
            Assertions.assertFalse(resultSet.next());
        } catch (SQLException e){
            System.err.print("something went wrong retrieving the empty table");
        }
    }

    @Test
    void testGet(){
        insertDummydata();

        ShoppingListEntry actualValue = shoppingListDao.get(EXPECTED_LIST_ITEM_ID);

        Assertions.assertEquals(EXPECTED_SHOPPING_LIST_ENTRY_OBJECT, actualValue);
    }

    @Test
    void testGetAll(){
        insertDummydata();

        List<ShoppingListEntry> expectedValue = List.of(EXPECTED_SHOPPING_LIST_ENTRY_OBJECT);
        List<ShoppingListEntry> actualValue = shoppingListDao.getAll();

        Assertions.assertEquals(expectedValue, actualValue);
    }

}
