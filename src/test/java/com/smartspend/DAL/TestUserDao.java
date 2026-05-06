package com.smartspend.DAL;

import com.smartspend.dao.UserDao;
import com.smartspend.model.User;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

public class TestUserDao {
    private static Connection mockConnection = new MockSQLiteConnection().mockConnection;
    private static UserDao userDao = new UserDao(mockConnection);

    @BeforeEach
    void setUp() {
        mockConnection = new MockSQLiteConnection().mockConnection;
        userDao = new UserDao(mockConnection);
        String makeTable = "CREATE TABLE users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE ," +
                "email TEXT NOT NULL UNIQUE," +
                "hashedPassword TEXT," +
                "salt TEXT);";
        try{
            Statement MOCK_STATEMENT = mockConnection.createStatement();
            MOCK_STATEMENT.execute(makeTable);
        } catch (SQLException e){
            System.err.println("Error in creating user table: " + "\n");
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        mockConnection.close();
    }
    // creating the dummy data
    private final static int userId = 1;
    private final static String username = "Jane Doe";
    private final static String email = "janeDoe@example.email.com";
    private final static byte[]  hashedPassword = new byte[10];
    private final static byte[] salt= new byte[12];

    private static void insertDummyData(){
        String insertDummyDataQuery = "INSERT INTO users(user_id, username, email) VALUES (?, ?, ?)";
        try{
            PreparedStatement preparedStatement = mockConnection.prepareStatement(insertDummyDataQuery);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, email);
            preparedStatement.execute();
            preparedStatement.close();

        } catch (SQLException e){
            System.err.println("Error in inserting dummy data: " + "\n");
            e.printStackTrace();
        }
    }

    @Test
    void TestInsert(){
        User ExpectedValue =  new User(username, email, hashedPassword, salt);
        userDao.insert(ExpectedValue);

        String retrieveActualValueQuery = "SELECT * FROM users WHERE username = ?";
        User actualValue = null;
        try{
            PreparedStatement preparedStatement = mockConnection.prepareStatement(retrieveActualValueQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                actualValue = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getBytes("hashedPassword"), resultSet.getBytes("salt"));
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
        User ExpectedValue =  new User(userId, updatedName, email, hashedPassword, salt);

        userDao.update(ExpectedValue);
        User actualValue = null;
        String retrieveActualValueQuery = "SELECT * FROM users WHERE user_id = ?";
        try{
            PreparedStatement preparedStatement = mockConnection.prepareStatement(retrieveActualValueQuery);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                actualValue = new User(resultSet.getInt("user_id"), resultSet.getString("username"), resultSet.getString("email"),  resultSet.getBytes("hashedPassword"), resultSet.getBytes("salt"));
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
        User ExpectedValue =  new User(userId, username, email, hashedPassword, salt);
        userDao.delete(ExpectedValue);
        try{
            String query = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = mockConnection.prepareStatement(query);
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
        User ExpectedValue = new User(userId, username, email, hashedPassword, salt);
        User ActualValue = userDao.get(userId);
        Assertions.assertEquals(ExpectedValue, ActualValue);
    }

    @Test
    void TestGetAll(){
        insertDummyData();
        List<User> ExpectedValue = List.of(new User(userId, username, email, hashedPassword, salt));
        List<User> ActualValue = userDao.getAll();
        Assertions.assertEquals(ExpectedValue, ActualValue);
    }
}
