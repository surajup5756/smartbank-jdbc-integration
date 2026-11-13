package com.smartbank.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Central JDBC connection factory. Configure credentials with environment variables. */
public final class DatabaseConnection {
    private static final String URL = System.getenv().getOrDefault(
            "SMARTBANK_DB_URL",
            "jdbc:mysql://localhost:3306/smartbank_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private static final String USER = System.getenv().getOrDefault("SMARTBANK_DB_USER", "root");
   private static final String PASSWORD = "Suraj@9984";

    private DatabaseConnection() { }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("MySQL JDBC Driver not found. Run the project with Maven.", ex);
        }
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            throw new SQLException("Unable to connect to MySQL. Check that MySQL Server is running and credentials are correct. " + ex.getMessage(), ex);
        }
    }
}
