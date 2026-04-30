package com.smartspend.service;

import com.smartspend.dao.UserDao;
import com.smartspend.model.User;
import com.smartspend.model.UserRegistrationDTO;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Connection;

import static com.smartspend.service.HashingPasswordService.generateHashedPassword;
import static com.smartspend.service.HashingPasswordService.generateSalt;

/**
 * this is to create/register new users
 * It mainly handles things like creating the salt + hashing passwords before inserting the data
 */
public class UserRegistrationService {
    private final UserDao userDao;
    public UserRegistrationService(Connection connection) {
        this.userDao =  new UserDao(connection);
    }
    public void register(UserRegistrationDTO newUser) {
        byte[] salt = generateSalt();
        byte[] hashedPassword = generateHashedPassword(salt, newUser.getPassword());
        User user = new User(newUser.getUsername(),  newUser.getEmail(), hashedPassword, salt);
        userDao.insert(user);
    }

}
