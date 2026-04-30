package com.smartspend.service;

import com.smartspend.dao.SqliteConnection;
import com.smartspend.dao.UserDao;
import com.smartspend.model.User;
import com.smartspend.util.DatabaseManager;
import org.testcontainers.shaded.com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;

/**
 * this is for authenticating for user login
 */
public class UserAuthenticationService {
    private final UserDao userDao;
    private String email;
    private String password;

    public UserAuthenticationService(Connection connection) {
        userDao = new UserDao(connection);
    }

    public boolean IsUserRegistered(String email) throws SQLException {
        return (userDao.getByEmail(email) == null);
    }

    public boolean isPasswordCorrect(String email, String password) throws SQLException {
        User user = userDao.getByEmail(email);
        if (user == null) {
            throw new SQLException("This email does not exist in the database");
        }

        byte[] salt = user.getSalt();
        byte[] hashedPassword = HashingPasswordService.generateHashedPassword(salt, password);
        return Arrays.equals(hashedPassword, user.getHashedPassword());

    }
}