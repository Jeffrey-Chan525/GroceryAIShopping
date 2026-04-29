package com.smartspend.service;

import com.smartspend.dao.UserDao;
import com.smartspend.model.User;
import com.smartspend.model.UserRegistrationDTO;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Connection;

/**
 * this is to create/register new users
 * It mainly handles things like creating the salt + hashing passwords before inserting the data
 */
public class UserRegistrationService {
    private final UserDao userDao;
    public UserRegistrationService(Connection connection) {
        this.userDao =  new UserDao(connection);
    }


    public byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public byte[] generateHashedPassword(byte[] salt, String password){
        int iterations = 2;
        int memoryLimit = 66536;
        int parallelism = 1;
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(iterations)
                .withMemoryAsKB(memoryLimit)
                .withParallelism(parallelism)
                .withSalt(salt);
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());
        int hashLength = 32;
        byte[] hashedPassword = new byte[hashLength];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), hashedPassword, 0,  hashedPassword.length);

        return hashedPassword;
    }

    public void register(UserRegistrationDTO newUser) {
        byte[] salt = generateSalt();
        byte[] hashedPassword = generateHashedPassword(salt, newUser.getPassword());
        String hashedPasswordString = new String(hashedPassword, StandardCharsets.UTF_8);
        String saltString = new String(generateSalt());

        User user = new User(newUser.getUsername(),  newUser.getEmail(), hashedPasswordString, saltString);
        userDao.insert(user);
    }
}
