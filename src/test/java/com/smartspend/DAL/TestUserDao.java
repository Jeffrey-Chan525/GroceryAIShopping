package com.smartspend.DAL;

import com.smartspend.dao.UserDao;
import com.smartspend.model.User;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

public class TestUserDao {
    private final static Connection MOCK_CONNECTION = MockSQLiteConnection.mockConnection;
    private final static UserDao userDao = new UserDao(MOCK_CONNECTION);

    @BeforeAll
    static void setUp() {
        String makeTable = "CREATE TABLE users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE ," +
                "email TEXT NOT NULL UNIQUE" +
                ");";
        try{
            Statement MOCK_STATEMENT = MOCK_CONNECTION.createStatement();
            MOCK_STATEMENT.execute(makeTable);
        } catch (SQLException e){
            System.err.println("Error in creating user table: " + "\n");
            e.printStackTrace();
        }
    }

    // creating the dummy data
    private final static int userId = 1;
    private final static String username = "Jane Doe";
    private final static String email = "janeDoe@example.email.com";

    private static void insertDummyData(){
        String insertDummyDataQuery = "INSERT INTO users(user_id, username, email) VALUES (?, ?, ?)";
        try{
            PreparedStatement preparedStatement = MOCK_CONNECTION.prepareStatement(insertDummyDataQuery);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, email);
            preparedStatement.execute();

        } catch (SQLException e){
            System.err.println("Error in inserting dummy data: " + "\n");
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUpBeforeEach() {
        String ClearTable = "DELETE FROM users";
        try {
            MOCK_CONNECTION.createStatement().execute(ClearTable);
        } catch (SQLException e){
            System.err.println("Error in clearing user table: " + "\n");
            e.printStackTrace();
        }
    }

    @Test
    void TestInsert(){
        User ExpectedValue =  new User(userId, username, email);
        userDao.insert(ExpectedValue);

        String retrieveActualValueQuery = "SELECT * FROM users WHERE user_id = ?";
        User actualValue = null;
        try{
            PreparedStatement preparedStatement = MOCK_CONNECTION.prepareStatement(retrieveActualValueQuery);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                actualValue = new User(resultSet.getInt("user_id"), resultSet.getString("username"), resultSet.getString("email"));
            }
        } catch (SQLException e){
            System.err.println("Error in retrieving Actual value during TestInsert:" + "\n");
            e.printStackTrace();
        }

        Assertions.assertEquals(ExpectedValue, actualValue);
    }

    @Test
    void TestUpdate(){
        insertDummyData();
        String updatedName = "John Doe";
        User ExpectedValue =  new User(userId, updatedName, email);

        userDao.update(ExpectedValue);
        User actualValue = null;
        String retrieveActualValueQuery = "SELECT * FROM users WHERE user_id = ?";
        try{
            PreparedStatement preparedStatement = MOCK_CONNECTION.prepareStatement(retrieveActualValueQuery);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                actualValue = new User(resultSet.getInt("user_id"), resultSet.getString("username"), resultSet.getString("email"));
            }
        } catch (SQLException e){
            System.err.println("Error in retrieving Actual value during TestUpdate:" + "\n");
            e.printStackTrace();
        }
        Assertions.assertEquals(ExpectedValue, actualValue);
    }

    @Test
    void TestDelete(){
        insertDummyData();
        User ExpectedValue =  new User(userId, username, email);
        userDao.delete(ExpectedValue);
        try{
            String query = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = MOCK_CONNECTION.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Assertions.assertFalse(resultSet.next());
        } catch (SQLException e){
            System.err.println("Error in retrieving Actual value during TestDelete:" + "\n");
            e.printStackTrace();
        }
    }

    @Test
    void TestGet(){
        insertDummyData();
        User ExpectedValue = new User(userId, username, email);
        User ActualValue = userDao.get(userId);
        Assertions.assertEquals(ExpectedValue, ActualValue);
    }

    @Test
    void TestGetAll(){
        insertDummyData();
        List<User> ExpectedValue = List.of(new User(userId, username, email));
        List<User> ActualValue = userDao.getAll();
        Assertions.assertEquals(ExpectedValue, ActualValue);
    }
}
