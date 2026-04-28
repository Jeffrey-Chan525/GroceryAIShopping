package com.smartspend.service;

import com.smartspend.util.DatabaseManager;

public class UserAuthenticationService {
    private int userId;
    private String username;
    private String email;

    public UserAuthenticationService(int userId, String username, String email) {
        this.userId =  userId;
        this.username = username;
        this.email = email;
    }

    private int _initUserID(){
        // do be done after adding userDao
        return 0;
    }
}
