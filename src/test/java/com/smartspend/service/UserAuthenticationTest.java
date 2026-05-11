package com.smartspend.service;

import com.smartspend.DAL.MockSQLiteConnection;
import com.smartspend.dao.UserDao;
import com.smartspend.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class UserAuthenticationTest {
    private Connection connection = new MockSQLiteConnection().mockConnection;
    private UserDao userDao = new UserDao(connection);
    private UserAuthenticationService userAuthenticationService = new UserAuthenticationService(connection);

    @BeforeEach
    void init() throws SQLException {
        connection = new MockSQLiteConnection().mockConnection;
        userDao = new UserDao(connection);
        userAuthenticationService = new UserAuthenticationService(connection);
        String makeTable = "CREATE TABLE users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE ," +
                "email TEXT NOT NULL UNIQUE," +
                "hashedPassword TEXT," +
                "salt TEXT);";
        Statement statement = connection.createStatement();
        statement.execute(makeTable);
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
