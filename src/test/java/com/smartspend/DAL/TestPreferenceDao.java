package com.smartspend.DAL;

import com.smartspend.dao.PreferenceDao;
import com.smartspend.model.UserPreferences;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

public class TestPreferenceDao {
    private final static Connection MOCK_CONNECTION = MockSQLiteConnection.mockConnection;
    private final PreferenceDao preferenceDao = new PreferenceDao(MOCK_CONNECTION);

    // this is the used for the dummy data to be tested with
    private final int EXPECTED_PREFERENCE_ID = 1;
    private final int EXPECTED_USER_ID = 1;
    private final double EXPECTED_WEEKLY_BUDGET = 100.0;
    private final String EXPECTED_PRIMARY_STORE = "woolworths";
    private final boolean EXPECTED_SHOW_SALE_PREDICTIONS = true;
    private final boolean EXPECTED_SHOW_VALUE_SUGGESTIONS = false;
    private final UserPreferences EXPECTED_USER_PREFERENCES_OBJECT = new UserPreferences(EXPECTED_PREFERENCE_ID, EXPECTED_USER_ID, EXPECTED_WEEKLY_BUDGET, EXPECTED_PRIMARY_STORE, EXPECTED_SHOW_SALE_PREDICTIONS, EXPECTED_SHOW_VALUE_SUGGESTIONS);

    @BeforeAll
    static void beforeAll(){
        String createTable = "CREATE TABLE user_preferences (" +
                "    preference_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    user_id INTEGER NOT NULL UNIQUE," +
                "    weekly_budget REAL NOT NULL DEFAULT 100 CHECK (weekly_budget >= 0)," +
                "    primary_store TEXT," +
                "    show_sale_predictions INTEGER NOT NULL DEFAULT 1 CHECK (show_sale_predictions IN (0,1))," +
                "    show_value_suggestions INTEGER NOT NULL DEFAULT 1 CHECK (show_value_suggestions IN (0,1))," +
                "    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE" +
                ");";

        try{
            Statement statement = MOCK_CONNECTION.createStatement();
            statement.execute(createTable);
        } catch (SQLException e){
            System.err.print("An error has occurred when creating the user_preferences table");
        }
    }

    @BeforeEach
    void beforeEach(){
        // clearing the table of any data before each test
        String clearTable = "DELETE FROM user_preferences";

        try{
            Statement statement = MOCK_CONNECTION.createStatement();
            statement.execute(clearTable);
        } catch (SQLException e){
            System.err.print("An error has occurred when clearing the user_preferences table");
        }
    }


    private void insertDummyData(){
        String insertData = "INSERT INTO user_preferences (preference_id ,user_id, weekly_budget, primary_store, show_sale_predictions, show_value_suggestions) VALUES " +
                "(?, ?, ?, ?, ?, ?)";
        // these values correspond to the column values
        int PREFERENCE_ID = 1;
        int USER_ID = 2;
        int WEEKLY_BUDGET = 3;
        int PRIMARY_STORE = 4;
        int SHOW_SALE_PREDICTION = 5;
        int SHOW_VALUE_SUGGESTIONS = 6;
        try{
            PreparedStatement preparedStatement = MOCK_CONNECTION.prepareStatement(insertData);
            preparedStatement.setInt(PREFERENCE_ID, EXPECTED_PREFERENCE_ID);
            preparedStatement.setInt(USER_ID, EXPECTED_USER_ID);
            preparedStatement.setDouble(WEEKLY_BUDGET, EXPECTED_WEEKLY_BUDGET);
            preparedStatement.setString(PRIMARY_STORE, EXPECTED_PRIMARY_STORE);
            preparedStatement.setBoolean(SHOW_SALE_PREDICTION, EXPECTED_SHOW_SALE_PREDICTIONS);
            preparedStatement.setBoolean(SHOW_VALUE_SUGGESTIONS, EXPECTED_SHOW_VALUE_SUGGESTIONS);
            preparedStatement.execute();
        }catch (SQLException e){
            System.err.print("an error has occurred when inserting dummy data into the user_preference table");
        }
    }

    /**
     * Turns a result set to a preference object
     * the result set must contain all columns in the table
      * @param resultSet the result set from a query
     * @return the corresponding userPreference object
     */
    private UserPreferences turnResultSetIntoUserPreferenceObject(ResultSet resultSet) throws SQLException{
        // column values
        int PREFERENCE_ID = 1;
        int USER_ID = 2;
        int WEEKLY_BUDGET = 3;
        int PRIMARY_STORE = 4;
        int SHOW_SALE_PREDICTIONS = 5;
        int SHOW_VALUE_SUGGESTIONS = 6;

        int preferenceID = resultSet.getInt(PREFERENCE_ID);
        int userID = resultSet.getInt(USER_ID);
        double weeklyBudget = resultSet.getDouble(WEEKLY_BUDGET);
        String primaryStore = resultSet.getString(PRIMARY_STORE);
        boolean showSalePredictions = resultSet.getBoolean(SHOW_SALE_PREDICTIONS);
        boolean showValueSuggestions = resultSet.getBoolean(SHOW_VALUE_SUGGESTIONS);

        return new UserPreferences(preferenceID,userID, weeklyBudget, primaryStore, showSalePredictions,showValueSuggestions);
    }

    @Test
    void testInsert(){
        //inserting a user_preference object into the database
        preferenceDao.insert(EXPECTED_USER_PREFERENCES_OBJECT);

        // retrieving the inserted data from the database

        String retrieveDummyData = "SELECT * FROM main.user_preferences WHERE preference_id = 1";

        UserPreferences ActualValue = null;
        try{
            Statement statement = MOCK_CONNECTION.createStatement();
            ResultSet resultSet = statement.executeQuery(retrieveDummyData);
            ActualValue = turnResultSetIntoUserPreferenceObject(resultSet);
        } catch (SQLException e){
            System.err.print("something went wrong when retrieving the dummy data");
        }

        Assertions.assertEquals(EXPECTED_USER_PREFERENCES_OBJECT, ActualValue);

    }

    @Test
    void testUpdate(){
        // inserting the dummy data
        insertDummyData();

        // updating the value with preferenceDao
        boolean updatedShowSaleSuggestions = false;
        UserPreferences EXPECTED_VALUE = new UserPreferences(EXPECTED_PREFERENCE_ID, EXPECTED_USER_ID, EXPECTED_WEEKLY_BUDGET, EXPECTED_PRIMARY_STORE, updatedShowSaleSuggestions, EXPECTED_SHOW_VALUE_SUGGESTIONS);
        preferenceDao.update(EXPECTED_VALUE);
        // retrieving the updated value
        String retrieveUpdatedData = "SELECT * FROM user_preferences WHERE preference_id = 1";
        UserPreferences ActualValue = null;
        try {
            Statement statement = MOCK_CONNECTION.createStatement();
            ResultSet resultSet = statement.executeQuery(retrieveUpdatedData);
            ActualValue = turnResultSetIntoUserPreferenceObject(resultSet);
        } catch (SQLException e){
            System.err.print("An error has occurred when retrieving the updated user preference object");
        }

        Assertions.assertEquals(EXPECTED_VALUE, ActualValue);
    }

    @Test
    void testDelete() {
        // inserting the data to be deleted
        insertDummyData();

        //deleting the data with preference dao
        preferenceDao.delete(EXPECTED_USER_PREFERENCES_OBJECT);

        //retrieving the empty table
        String queryEntireTable = "SELECT * FROM user_preferences";
        ResultSet resultSet;
        try{
            Statement statement = MOCK_CONNECTION.createStatement();
            resultSet = statement.executeQuery(queryEntireTable);
            Assertions.assertFalse(resultSet.next());
        } catch (SQLException e){
            System.err.print("An error has occurred when querying the entire table from the database during testDelete");
        }
    }

    @Test
    void testGet(){
        insertDummyData();
        UserPreferences ActualValue = preferenceDao.get(1);
        Assertions.assertEquals(EXPECTED_USER_PREFERENCES_OBJECT, ActualValue);

    }

    @Test
    void testGetAll(){
        insertDummyData();
        List<UserPreferences> EXPECTED_VALUES = List.of(EXPECTED_USER_PREFERENCES_OBJECT);
        List<UserPreferences> ActualValues = preferenceDao.getAll();

        Assertions.assertEquals(EXPECTED_VALUES, ActualValues);

    }
}
