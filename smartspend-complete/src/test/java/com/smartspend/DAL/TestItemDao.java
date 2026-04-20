package com.smartspend.DAL;


import com.smartspend.dao.ItemDao;
import com.smartspend.model.Item;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class TestItemDao {

    private static final Connection MOCK_CONNECTION = MockSQLiteConnection.mockConnection;
    private static final ItemDao itemDao = new ItemDao(MOCK_CONNECTION);
    private static final Logger log = LoggerFactory.getLogger(TestItemDao.class);

    private Item createItemFromQuery(ResultSet resultSet){
        // these values correspond to the column names in the table
        int ITEM_ID = 1;
        int NAME = 2;
        int CATEGORY = 3;
        int BRAND = 4;
        int DEFAULT_UNIT = 5;

        try{
            if (resultSet.next()){
                int item = resultSet.getInt(ITEM_ID);
                String name = resultSet.getString(NAME);
                String category = resultSet.getString(CATEGORY);
                String brand = resultSet.getString(BRAND);
                String defaultUnit = resultSet.getString(DEFAULT_UNIT);
                return new Item(item, name, category, brand, defaultUnit);
            }
        } catch (SQLException e){
            log.error("an error has occurred when creating an Item");
        }
        return null;
    }

    int ITEM_ID_VALUE = 1;
    String NAME_VALUE = "Toilet paper";
    String CATEGORY_VALUE = "household items";
    String BRAND_VALUE = "quilton";
    String DEFAULT_VALUE = "rolls";

    Item EXPECTED_VALUE = new Item(ITEM_ID_VALUE, NAME_VALUE, CATEGORY_VALUE, BRAND_VALUE, DEFAULT_VALUE);
    /**
     * this is a helper function to allow insertion of test data
     * the specific test data inserted is EXPECTED DATA
      */
    private void insertTestData(){
        int ITEM_ID = 1;
        int NAME = 2;
        int CATEGORY = 3;
        int BRAND = 4;
        int DEFAULT_UNIT = 5;

        String insertTestData = "INSERT INTO items (item_id, name, category, brand, default_unit) values (?,?,?,?,?)";
        try {

            PreparedStatement preparedStatement = MOCK_CONNECTION.prepareStatement(insertTestData);
            preparedStatement.setInt(ITEM_ID,ITEM_ID_VALUE);
            preparedStatement.setString(NAME, NAME_VALUE);
            preparedStatement.setString(CATEGORY, CATEGORY_VALUE);
            preparedStatement.setString(BRAND, BRAND_VALUE);
            preparedStatement.setString(DEFAULT_UNIT, DEFAULT_VALUE);
            preparedStatement.execute();
        } catch (SQLException e){
            Assertions.assertThrows(SQLException.class, () -> {
                throw new SQLException("Something went wrong with inserting the test data into the in memory database");
            });
        }
    }

    private String ItemToString(Item item){
        String res = "";
        res += "Id: " + item.getId() + "\n";
        res += "Name: " + item.getName() + "\n";
        res += "Category: " + item.getCategory() + "\n";
        res += "Brand: " + item.getBrand() + "\n";
        res += "defaultUnit: " + item.getDefaultUnit() + "\n";
        return res;
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
            Statement stmt = MOCK_CONNECTION.createStatement();
            stmt.execute(query);
        } catch (SQLException e){
            log.error("e: ", e);
        }
    }

    @BeforeEach
    void init(){
        String query = "DELETE FROM items";
        try {
            Statement statement = MOCK_CONNECTION.createStatement();
            statement.execute(query);
        } catch (SQLException e ){
            log.error("something has happen while clearing the data from items", e);
        }

    }

    @Test
    void testInsert() {
        // inserting the item
        Item item = new Item(1,"toilet paper", "Household Essentials","Quilton","roll");
        itemDao.insert(item);
        //checking if the item is inserted
        String query = "SELECT * FROM items WHERE item_id = 1";
        try{
            PreparedStatement statement = MOCK_CONNECTION.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            Item insertedItem = createItemFromQuery(resultSet);
            Assertions.assertEquals(item, insertedItem);
        } catch (SQLException e){
            log.error("Error has occurred for insertTest", e);
        }
    }
    // this test is disabled until the equals method is implemented in Item
    @Disabled
    @Test
    void testUpdate(){
            String TestDataQuery = """
                    INSERT INTO items (item_id, name, category, default_unit) VALUES (?, ?, ?, ?)
                    """;
            try {
                // inserting the test data into the table
                PreparedStatement statement = MOCK_CONNECTION.prepareStatement(TestDataQuery);
                statement.setInt(1, 2);
                statement.setString(2, "toilet Paper");
                statement.setString(3, "household products");
                statement.setString(4, "rolls");
                statement.execute();

                // retrieving the inserted Test data
                String query = "SELECT * from items WHERE item_id = 2";
                Statement retrievedDataStatement = MOCK_CONNECTION.createStatement();
                ResultSet resultSet = retrievedDataStatement.executeQuery(query);
                // updating the data with ItemDAO
                int ITEM_ID = 1;
                int NAME = 2;
                int CATEGORY = 3;
                int DEFAULT_UNIT = 5;
                Item Expected = null;
                if (resultSet.next()){
                    int item_id = resultSet.getInt(ITEM_ID);
                    String name = resultSet.getString(NAME);
                    String category = resultSet.getString(CATEGORY);
                    String defaultUnit = resultSet.getString(DEFAULT_UNIT);
                    Expected = new Item(item_id, name, category, "quilton", defaultUnit);
                    itemDao.update(Expected);

                }

                ResultSet updatedResultSet = retrievedDataStatement.executeQuery(query);
                Item ActualUpdateItem = createItemFromQuery(updatedResultSet);
                Assertions.assertSame(Expected, ActualUpdateItem);

            } catch (SQLException e){
                log.error("error occurred from updateTest", e);
            }

    }
    @Test
    void testDelete(){
        String insertTestData = "INSERT INTO items (item_id, name, category, default_unit) values (?,?,?,?)";
        //inserts the data to be deleted
        int ID_VALUE = 1;
        String NAME_VALUE = "toilet paper";
        String CATEGORY_VALUE = "household items";
        String BRAND_VALUE = "quilton";
        String DEFAULT_UNIT_VALUE = "rolls";
        try{
            PreparedStatement preparedStatement = MOCK_CONNECTION.prepareStatement(insertTestData);
            int ITEM_ID = 1;
            int NAME = 2;
            int CATEGORY = 3;
            int BRAND = 4;
            int DEFAULT_UNIT = 5;

            preparedStatement.setInt(ITEM_ID, ID_VALUE);
            preparedStatement.setString(NAME, NAME_VALUE);
            preparedStatement.setString(CATEGORY, CATEGORY_VALUE);
            preparedStatement.setString(BRAND, BRAND_VALUE);
            preparedStatement.setString(DEFAULT_UNIT, DEFAULT_UNIT_VALUE);
        } catch (SQLException e){
            log.error("There was an error with inserting the test data",e );
        }
        // deletes the data with ItemDAO
        Item item = new Item(ID_VALUE, NAME_VALUE, CATEGORY_VALUE, BRAND_VALUE, DEFAULT_UNIT_VALUE);
        itemDao.delete(item);

        String selectTableQuery = "SELECT * FROM items";
        try {
            Statement statement = MOCK_CONNECTION.createStatement();
            ResultSet resultSet = statement.executeQuery(selectTableQuery);
            if (!resultSet.next() || resultSet.getInt(1) == 1){
                Assertions.fail();
            }
        } catch (SQLException e){
            log.error("an error has occurred while selecting data from the table");
        }
    }

    @Test
    void testGet(){
        insertTestData();

        Item ActualItem = itemDao.get(1);
        Item ExpectedItem = new Item(ITEM_ID_VALUE, NAME_VALUE, CATEGORY_VALUE, BRAND_VALUE, DEFAULT_VALUE);
        Assertions.assertEquals(ActualItem, EXPECTED_VALUE);
    }

    @Test
    void testGetAll(){
        insertTestData();

        List<Item> ActualItems = itemDao.getAll();
        List<Item> ExpectedItems = List.of(EXPECTED_VALUE);
        Assertions.assertEquals(ActualItems, ExpectedItems);
    }
}
