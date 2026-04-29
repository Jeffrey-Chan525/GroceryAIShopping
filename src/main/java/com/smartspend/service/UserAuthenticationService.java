package com.smartspend.service;

import com.smartspend.util.DatabaseManager;

/**
 * this is for authenticating for user login
 */
public class UserAuthenticationService {
    private String username;
    private String email;

    public UserAuthenticationService(String username, String email) {
        this.username = username;
        this.email = email;
    }

    private int _initUserID(){
        // do be done after adding userDao
        return 0;
    }
}
