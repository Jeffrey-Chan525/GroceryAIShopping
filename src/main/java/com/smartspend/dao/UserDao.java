package com.smartspend.dao;

import com.smartspend.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class UserDao implements DAO<User>{
    private final Connection connection ;

    // default values for database columns
    private int ID = 1;
    private int NAME = 2;
    private int EMAIL = 3;

    public UserDao(Connection connection) {this.connection = connection;}
    @Override
    public void insert(User value) {
        String insertQuery =  "INSERT INTO users (username, email, hashedPassword, salt) VALUES (?, ?, ?, ?)";
        NAME = 1;
        EMAIL = 2;
        int HASHED_PASSWORD = 3;
        int SALT = 4;
        try{
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(NAME, value.getUsername());
            insertStatement.setString(EMAIL, value.getEmail());
            insertStatement.setString(HASHED_PASSWORD, value.getHashedPassword());
            insertStatement.setString(SALT, value.getSalt());
            insertStatement.execute();
        } catch (SQLException e){
            System.err.println("Error in inserting user: " + "\n");
            e.printStackTrace();
        }
    }

    @Override
    public void update(User value) {
        String updateQuery = "UPDATE users SET username = ?, email = ? WHERE user_id = ?";
        NAME = 1;
        EMAIL = 2;
        ID = 3;
        try{
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(NAME, value.getUsername());
            updateStatement.setString(EMAIL, value.getEmail());
            updateStatement.setInt(ID, value.getUserId());
            updateStatement.execute();
        } catch (SQLException e){
            System.err.println("Error in updating user: " + "\n");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(User value) {
        String  deleteQuery = "DELETE FROM users WHERE user_id = ?";
        try{
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(ID, value.getUserId());
            deleteStatement.execute();
        } catch (Exception e){
            System.err.println("Error in deleting user: " + "\n");
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAll() {
        String  query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try{
            Statement getAllUsersStatement = connection.createStatement();
            ResultSet resultSet = getAllUsersStatement.executeQuery(query);
            while(resultSet.next()){
                int userID = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String  hashedPassword = resultSet.getString("hashedPassword");
                String  salt = resultSet.getString("salt");
                users.add(new User(userID,username,email, hashedPassword, salt));
            }
        } catch (SQLException e){
            System.err.println("Error in getting users: " + "\n");
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User get(int id) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        User res =  null;
        ID = 1;
        try{
            PreparedStatement getUsersStatement = connection.prepareStatement(query);
            getUsersStatement.setInt(ID, id);
            ResultSet resultSet = getUsersStatement.executeQuery();
            if (resultSet.next()){
                int userID = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String hashedPassword = resultSet.getString("hashedPassword");
                String salt = resultSet.getString("salt");
                res = new User(userID,username,email, hashedPassword, salt);
            }
        } catch (SQLException e){
            System.err.println("Error in getting user: " + "\n");
            e.printStackTrace();
        }

        System.out.print(res);
        return res;
    }

    public User getByName(String name) throws SQLException{
        String query = "SELECT * FROM users WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new User(resultSet.getInt("user_id"), resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("hashedPassword"), resultSet.getString("salt"));
        }
        return null;
    }

    public User getByEmail(String email) throws SQLException{
        String query = "SELECT * FROM users WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new User(resultSet.getInt("user_id"), resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("hashedPassword"), resultSet.getString("salt"));
        }
        return null;
    }
}
