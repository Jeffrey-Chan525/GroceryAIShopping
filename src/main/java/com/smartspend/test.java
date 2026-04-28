package com.smartspend;

import com.smartspend.model.AI;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class test {
    public static void main(String[] args){
        String url = System.getenv("DATABASE_URL");

        System.out.print(url);
    }

    public static void scrape(){
        String Url = "https://www.recipetineats.com/egg-fried-rice/";
        try {
            Document doc = Jsoup.connect(Url).get();
            String text = doc.text();
            System.out.print(text);
        } catch (IOException e){
            System.out.print("An IOException as happened: " + e);
        }
    }

    public static void Database(){
        String dbms = "sqlite";
        String fileName = "Test.db";
        String url = "jdbc" + ":" + dbms+ ":" + fileName;

        try {
            Connection conn= DriverManager.getConnection(url);
            System.out.print("Connection successful");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}


