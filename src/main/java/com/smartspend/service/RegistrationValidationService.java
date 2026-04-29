package com.smartspend.service;

import com.smartspend.dao.UserDao;
import com.smartspend.model.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * this is mainly for validating if users or emails already are registered
 */
public class RegistrationValidationService {
    private final UserDao userDao;
    public RegistrationValidationService(Connection connection) {
        this.userDao = new UserDao(connection);
    }

    public boolean uniqueUserName(String username) throws SQLException{
        User user = userDao.getByName(username);
        return user == null;
    }

    public boolean uniqueEmail(String email) throws SQLException{
        User user = userDao.getByEmail(email);
        return user == null;
    }
}
