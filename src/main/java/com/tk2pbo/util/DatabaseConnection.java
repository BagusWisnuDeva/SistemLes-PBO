package com.tk2pbo.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private static String url = "jdbc:mysql://localhost:3306/db_kursus?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Makassar";
    private static String user = "root";
    private static String password = "";

    static {
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream("db.properties")) {
            p.load(in);
            url = p.getProperty("db.url", url);
            user = p.getProperty("db.user", user);
            password = p.getProperty("db.password", password);
        } catch (IOException e) {
            // file tidak ada: pakai nilai bawaan XAMPP
        }
    }

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}