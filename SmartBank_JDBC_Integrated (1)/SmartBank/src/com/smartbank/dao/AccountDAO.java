package com.smartbank.dao;

import com.smartbank.database.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO layer: all account SQL is parameterized with PreparedStatement. */
public class AccountDAO {
    public void insertRecord(String accountNumber, String holder, String type,
                             BigDecimal balance, String status,
                             BigDecimal minimumBalance, BigDecimal overdraftLimit) throws SQLException {
        String sql = "INSERT INTO accounts(account_number, account_holder, account_type, balance, status, minimum_balance, overdraft_limit) VALUES(?,?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.setString(2, holder);
            ps.setString(3, type);
            ps.setBigDecimal(4, balance);
            ps.setString(5, status);
            ps.setBigDecimal(6, minimumBalance);
            ps.setBigDecimal(7, overdraftLimit);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Could not insert account '" + accountNumber + "': " + ex.getMessage(), ex);
        }
    }

    public List<String> getAllRecords() throws SQLException {
        String sql = "SELECT account_number, account_holder, account_type, balance, status, minimum_balance, overdraft_limit FROM accounts ORDER BY account_number";
        List<String> rows = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(String.format("%s | %s | %s | %s | %s",
                        rs.getString("account_number"), rs.getString("account_holder"),
                        rs.getString("account_type"), rs.getBigDecimal("balance"),
                        rs.getString("status")));
            }
            return rows;
        } catch (SQLException ex) {
            throw new SQLException("Could not retrieve account records: " + ex.getMessage(), ex);
        }
    }

    public boolean updateRecord(String accountNumber, BigDecimal newBalance, String newStatus) throws SQLException {
        String sql = "UPDATE accounts SET balance = ?, status = ? WHERE account_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, newBalance);
            ps.setString(2, newStatus);
            ps.setString(3, accountNumber);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new SQLException("Could not update account '" + accountNumber + "': " + ex.getMessage(), ex);
        }
    }

    public boolean deleteRecord(String accountNumber) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new SQLException("Could not delete account '" + accountNumber + "': " + ex.getMessage(), ex);
        }
    }

    public boolean exists(String accountNumber) throws SQLException {
        String sql = "SELECT 1 FROM accounts WHERE account_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
