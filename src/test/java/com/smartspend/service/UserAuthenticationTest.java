package com.smartspend.service;

import com.smartspend.DAL.MockSQLiteConnection;
import com.smartspend.dao.UserDao;
import com.smartspend.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class UserAuthenticationTest {
    private final static Connection connection = MockSQLiteConnection.mockConnection;
    private final static UserDao userDao = new UserDao(connection);
    private final static UserAuthenticationService userAuthenticationService = new UserAuthenticationService(connection);
    @BeforeAll
    static void setUp() throws SQLException, ClassNotFoundException {
        String clearTable = "DELETE FROM users";
        Statement statement = connection.createStatement();
        statement.execute(clearTable);
    }
    @Test
    void MatchingHashedPassword() {
        String username = "Jane Doe";
        String email = "placeholder@example.com";
        String password = "password";
        byte[] salt = HashingPasswordService.generateSalt();
        byte[] hashedPassword = HashingPasswordService.generateHashedPassword(salt, password);

        User user = new User(username, email, hashedPassword, salt);
        userDao.insert(user);
        try{
            Assertions.assertTrue(userAuthenticationService.isPasswordCorrect(email, password));
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
