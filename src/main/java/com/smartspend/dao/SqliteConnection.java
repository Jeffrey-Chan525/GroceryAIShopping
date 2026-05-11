package com.smartspend.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {
    private static Connection instance = null;

    private SqliteConnection(){
        String DATABASE_FILE = System.getenv("DATABASE_URL"); // this is the database file name
        String url = "JDBC:sqlite:" + DATABASE_FILE;
        try{
            instance = DriverManager.getConnection(url);
        } catch(SQLException ex){
            System.err.print(ex);
        }
    }

    public static Connection getInstance(){
        if (instance == null){
            new SqliteConnection();
        }
        return instance;
    }
}

