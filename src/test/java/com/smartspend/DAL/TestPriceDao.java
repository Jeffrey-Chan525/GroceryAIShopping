package com.smartspend.DAL;

import com.smartspend.dao.PriceDao;
import com.smartspend.model.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

public class TestPriceDao {
    private static final Connection MOCK_CONNECTION = MockSQLiteConnection.mockConnection;
    private static final PriceDao priceDao = new PriceDao(MOCK_CONNECTION);

    @BeforeAll
    static void beforeAll(){
        String createPricesTable = "CREATE TABLE prices (" +
                "price_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    item_id INTEGER NOT NULL," +
                "    store_name TEXT NOT NULL," +
                "    price REAL NOT NULL CHECK (price >= 0)," +
                "    package_quantity REAL NOT NULL CHECK (package_quantity > 0)," +
                "    package_unit TEXT NOT NULL," +
                "    last_updated TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "    is_on_sale INTEGER NOT NULL DEFAULT 0 CHECK (is_on_sale IN (0,1)))";

        try {
            Statement statement = MOCK_CONNECTION.createStatement();
            statement.execute(createPricesTable);

        } catch (SQLException e){
            System.err.print("A problem has occurred when creating the price table");
        }
    }

    @BeforeEach
    void beforeEach(){
        String query = "DELETE FROM prices";
        try{
            Statement statement = MOCK_CONNECTION.createStatement();
            statement.execute(query);
        } catch (SQLException e ){
            System.err.print("A problem has occurred when clearing the prices table");
        }
    }

    // this is the test data that will be used throughout the tests
    private final int EXPECTED_ITEM_ID = 1;
    private final int EXPECTED_PRICE_ID = 1;
    private final String EXPECTED_STORE_NAME = "woolworths";
    private final Double EXPECTED_PRICE = 100.0;
    private final Double EXPECTED_PACKAGE_QUANTITY = 2.2;
    private final String EXPECTED_PACKAGE_UNIT = "rolls";
    private final String EXPECTED_DATE = "2/2/2026";
    private final Boolean EXPECTED_IS_ON_SALE = false;
    private final Price EXPECTED_PRICE_OBJECT = new Price(EXPECTED_PRICE_ID,EXPECTED_ITEM_ID,EXPECTED_STORE_NAME, EXPECTED_PRICE,EXPECTED_PACKAGE_QUANTITY, EXPECTED_PACKAGE_UNIT, EXPECTED_DATE, EXPECTED_IS_ON_SALE);

    private void insertData(){
        String insertTestData = "INSERT INTO prices (price_id, item_id, store_name, price, package_quantity, package_unit, last_updated, is_on_sale) VALUES (?,?,?,?,?,?,?,?)";
        // these are values corresponding to the values of the columns in the query
        int PRICE_ID = 1;
        int ITEM_ID = 2;
        int STORE_NAME = 3;
        int PRICE = 4;
        int PACKAGE_QUANTITY = 5;
        int PACKAGE_UNIT = 6;
        int LAST_UPDATED = 7;
        int IS_ON_SALE = 8;
        try{
            PreparedStatement preparedStatement = MOCK_CONNECTION.prepareStatement(insertTestData);
            preparedStatement.setInt(PRICE_ID, EXPECTED_PRICE_ID);
            preparedStatement.setInt(ITEM_ID, EXPECTED_ITEM_ID);
            preparedStatement.setString(STORE_NAME, EXPECTED_STORE_NAME);
            preparedStatement.setDouble(PRICE, EXPECTED_PRICE);
            preparedStatement.setDouble(PACKAGE_QUANTITY, EXPECTED_PACKAGE_QUANTITY);
            preparedStatement.setString(PACKAGE_UNIT, EXPECTED_PACKAGE_UNIT);
            preparedStatement.setString(LAST_UPDATED, EXPECTED_DATE);
            preparedStatement.setBoolean(IS_ON_SALE, EXPECTED_IS_ON_SALE);
            preparedStatement.execute();
        } catch (SQLException e){
            System.err.print("An error has occurred when inserting the test data");
        }
    }

    /**
     * This turns a result set which retrieves all columns from the prices table into a Price object
     * @param resultSet the result set which contains all columns from prices
     * @return A price object
     */
    private Price turnResultSetIntoPrice(ResultSet resultSet) throws SQLException{
        int PRICE_ID = 1;
        int ITEM_ID = 2;
        int STORE_NAME = 3;
        int PRICE = 4;
        int PACKAGE_QUANTITY = 5;
        int PACKAGE_UNIT = 6;
        int LAST_UPDATED = 7;
        int IS_ON_SALE = 8;

        int priceID = resultSet.getInt(PRICE_ID);
        int itemID = resultSet.getInt(ITEM_ID);
        String storeName = resultSet.getString(STORE_NAME);
        double price = resultSet.getDouble(PRICE);
        double packageQuantity = resultSet.getDouble(PACKAGE_QUANTITY);
        String packageUnit = resultSet.getString(PACKAGE_UNIT);
        String lastUpdated = resultSet.getString(LAST_UPDATED);
        boolean isOnSale = resultSet.getBoolean(IS_ON_SALE);

        return new Price(priceID, itemID ,storeName, price, packageQuantity,packageUnit, lastUpdated, isOnSale);

    }

    @Test
    void testInsert(){
        // inserts into the table with the priceDao object
       priceDao.insert(EXPECTED_PRICE_OBJECT);
       //retrieving the inserted data
       String query = "SELECT price_id FROM prices WHERE price_id = 1";
       try{
           Statement statement = MOCK_CONNECTION.createStatement();
           ResultSet resultSet = statement.executeQuery(query);
           if (resultSet.next()){
               // this compares the actual price_id to the expected price_id
               Assertions.assertEquals(resultSet.getInt(1), EXPECTED_PRICE_ID);
           }
       } catch (SQLException e){
           System.err.print("something wrong has happened when retrieving price_id from the price table in testInsert");
       }
    }

    @Test
    void testUpdate(){
        // for this test the insertData method will be used to put in dummy data into the table

        // putting in dummy data
        insertData();

        // updating the data
        // updated price id will be the expected value of this test
        int UPDATED_ITEM_ID = 2;
        Price updatePrice = new Price(EXPECTED_PRICE_ID, UPDATED_ITEM_ID, EXPECTED_STORE_NAME, EXPECTED_PRICE, EXPECTED_PACKAGE_QUANTITY, EXPECTED_PACKAGE_UNIT, EXPECTED_DATE, EXPECTED_IS_ON_SALE);
        priceDao.update(updatePrice);

        //checking what was updated to the database
        String retrievePriceID = "SELECT * FROM prices WHERE price_id = 1";
        Price actualPrice = null;
        try{
            Statement statement = MOCK_CONNECTION.createStatement();
            ResultSet resultSet = statement.executeQuery(retrievePriceID);
            actualPrice = turnResultSetIntoPrice(resultSet);
        }catch (SQLException e){
            System.err.print("an error occurred during testUpdate when trying to retrieve the updated dummy record");
        }

        Assertions.assertEquals(actualPrice, updatePrice);
    }

    @Test
    void testDelete(){
        // inserting the dummy data to be deleted
        insertData();

        // deleting the object with priceDao
        priceDao.delete(EXPECTED_PRICE_OBJECT);

        // checking if the table is empty
        String query = "SELECT * FROM prices";

        ResultSet resultSet;
        try{
            Statement statement = MOCK_CONNECTION.createStatement();
            resultSet = statement.executeQuery(query);
            Assertions.assertFalse(resultSet.next());
        } catch (SQLException e){
            System.err.print("something went wrong when checking if the price table was empty");
        }
    }

    @Test
    void testGet(){
        // inserting the dummy data
        insertData();

        // using priceDao to get the dummy data
        Price actualPrice = priceDao.get(EXPECTED_PRICE_ID);

        //checking if the objects are the same
        Assertions.assertEquals(EXPECTED_PRICE_OBJECT, actualPrice);
    }

    @Test
    void testGetAll(){
        //inserting the dummy data
        insertData();

        // using PriceDao to the entire table
        List<Price> actualPrices = priceDao.getAll();
        List<Price> expectedPrices = List.of(EXPECTED_PRICE_OBJECT);

        //comparing the actual value to the expected one
        Assertions.assertEquals(actualPrices, expectedPrices);
    }
}
