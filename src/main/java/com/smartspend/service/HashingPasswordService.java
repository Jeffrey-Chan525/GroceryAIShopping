package com.smartspend.service;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class HashingPasswordService {

    public static byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] generateHashedPassword(byte[] salt, String password){
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
    }}
