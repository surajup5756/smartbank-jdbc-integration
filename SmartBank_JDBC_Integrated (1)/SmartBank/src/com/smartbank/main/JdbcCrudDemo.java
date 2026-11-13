package com.smartbank.main;

import com.smartbank.dao.AccountDAO;
import com.smartbank.database.DatabaseInitializer;
import java.math.BigDecimal;
import java.sql.SQLException;

/** Runs a safe end-to-end CRUD demonstration against a dedicated demo account. */
public class JdbcCrudDemo {
    public static void main(String[] args) {
        String id = "JDBC-DEMO-001";
        AccountDAO dao = new AccountDAO();
        try {
            DatabaseInitializer.createTables();
            // Make repeat runs safe: remove only this reserved demonstration row.
            dao.deleteRecord(id);

            System.out.println("\\n1) INSERT");
            dao.insertRecord(id, "JDBC Demo User", "SAVINGS",
                    new BigDecimal("5000.00"), "ACTIVE",
                    new BigDecimal("1000.00"), null);
            System.out.println("Inserted: " + dao.exists(id));

            System.out.println("\\n2) SELECT");
            for (String row : dao.getAllRecords()) System.out.println(row);

            System.out.println("\\n3) UPDATE");
            System.out.println("Updated: " + dao.updateRecord(id,
                    new BigDecimal("6500.00"), "ACTIVE"));

            System.out.println("\\n4) SELECT AFTER UPDATE");
            for (String row : dao.getAllRecords()) {
                if (row.startsWith(id + " |")) System.out.println(row);
            }

            System.out.println("\\n5) DELETE");
            System.out.println("Deleted: " + dao.deleteRecord(id));
            System.out.println("Record exists after delete: " + dao.exists(id));
            System.out.println("\\nCRUD demonstration completed successfully.");
        } catch (SQLException ex) {
            System.err.println("[DATABASE ERROR] " + ex.getMessage());
            System.err.println("Check MySQL Server, schema setup, username and password.");
        }
    }
}
