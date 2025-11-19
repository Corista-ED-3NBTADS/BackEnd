package com.coral.util;
import java.sql.*;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/coral?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Ph230905?"; // ajuste conforme seu MySQL

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } 
        catch (ClassNotFoundException e) { e.printStackTrace(); }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void close(AutoCloseable c) {
        if (c == null) return;
        try { c.close(); } catch (Exception e) {/* ignore */}
    }
}
