package com.smartspend.model;

import java.util.Objects;

public class User {
    private int userId;
    private String username;
    private String email;
    private String hashedPassword;
    private String salt;

    public User(String username, String email, String hashedPassword, String salt) {
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public User(int userId, String username, String email, String hashedPassword, String salt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getHashedPassword() { return hashedPassword; }
    public String getSalt() { return salt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        boolean isUserIDEquals = user.userId == this.userId;
        boolean isUsernameEquals = user.username.equals(this.username);
        boolean isEmailEquals = user.email.equals(this.email);
        return isEmailEquals && isUserIDEquals && isUsernameEquals;

    }
    @Override
    public int hashCode() {
        return  Objects.hash(userId);
    }
}
