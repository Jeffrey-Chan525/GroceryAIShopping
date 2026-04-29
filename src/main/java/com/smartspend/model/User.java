package com.smartspend.model;

import java.util.Objects;

public class User {
    private int userId;
    private String username;
    private String email;

    public User(int userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }

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
