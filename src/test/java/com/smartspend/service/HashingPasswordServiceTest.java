package com.smartspend.service;

import com.smartspend.DAL.MockSQLiteConnection;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static com.smartspend.service.HashingPasswordService.generateHashedPassword;
import static com.smartspend.service.HashingPasswordService.generateSalt;


public class HashingPasswordServiceTest {
    @Test
    public void test_Argon2PasswordGenerator(){
        byte[] salt = generateSalt();
        String password = "testing";

        byte[] ActualValue = generateHashedPassword(salt, password);
        // parameter values for argon generator
        int iterations = 2;
        int memoryLimit = 66536;
        int parallelism = 1;
        int hashLength = 32;
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(iterations)
                .withMemoryAsKB(memoryLimit)
                .withParallelism(parallelism)
                .withSalt(salt);
        Argon2BytesGenerator verifier = new Argon2BytesGenerator();
        verifier.init(builder.build());
        byte[] testHash = new byte[hashLength];
        verifier.generateBytes(password.getBytes(), testHash, 0, hashLength);
        Assertions.assertArrayEquals(testHash, ActualValue);

    }
}
