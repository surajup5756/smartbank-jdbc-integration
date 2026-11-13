package com.smartbank.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Creates application tables if they do not already exist. */
public final class DatabaseInitializer {
    private DatabaseInitializer() { }

    public static void createTables() throws SQLException {
        String accounts = "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_number VARCHAR(20) PRIMARY KEY," +
                "account_holder VARCHAR(100) NOT NULL," +
                "account_type VARCHAR(20) NOT NULL," +
                "balance DECIMAL(15,2) NOT NULL DEFAULT 0.00," +
                "status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'," +
                "minimum_balance DECIMAL(15,2) NULL," +
                "overdraft_limit DECIMAL(15,2) NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        String transactions = "CREATE TABLE IF NOT EXISTS transactions (" +
                "transaction_id VARCHAR(20) PRIMARY KEY," +
                "account_number VARCHAR(20) NOT NULL," +
                "transaction_type VARCHAR(30) NOT NULL," +
                "amount DECIMAL(15,2) NOT NULL," +
                "balance_after DECIMAL(15,2) NOT NULL," +
                "description VARCHAR(255)," +
                "transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "CONSTRAINT fk_transactions_account FOREIGN KEY (account_number) " +
                "REFERENCES accounts(account_number) ON DELETE CASCADE)";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(accounts);
            statement.executeUpdate(transactions);
        }
    }
}
