package com.example.sqlgateway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static String jdbcUrl = "jdbc:h2:file:./data/sqlgateway;AUTO_SERVER=TRUE;MODE=PostgreSQL;DATABASE_TO_UPPER=false";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, USER, PASS);
    }
}
