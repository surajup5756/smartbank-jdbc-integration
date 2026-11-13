package com.smartbank.services;

import com.smartbank.models.BankAccount;
import com.smartbank.models.Transaction;
import com.smartbank.utils.ConsoleTable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatementService {
    public void printStatement(BankAccount account) {
        List<Transaction> transactions = account.getTransactions();
        List<String[]> rows = new ArrayList<String[]>();
        for (Transaction tx : transactions) {
            rows.add(new String[] {
                tx.getTransactionId(), tx.getFormattedDateTime(), tx.getType(),
                tx.getAmount().toPlainString(), tx.getBalanceAfter().toPlainString(),
                tx.getDescription()
            });
        }
        System.out.println("\nACCOUNT STATEMENT");
        System.out.println("Account       : " + account.getAccountNumber());
        System.out.println("Account holder: " + account.getAccountHolder());
        System.out.println("Account type  : " + account.getAccountType());
        System.out.println("Status        : " + account.getStatus());
        System.out.println("Current balance: " + account.getBalance());
        ConsoleTable.print(new String[] {"TXN ID", "DATE & TIME", "TYPE", "AMOUNT", "BALANCE AFTER", "DESCRIPTION"}, rows);
    }

    public File exportCsv(BankAccount account) throws IOException {
        File directory = new File("reports");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Could not create reports directory.");
        }
        File file = new File(directory, "statement_" + account.getAccountNumber() + ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Transaction ID,Date Time,Type,Amount,Balance After,Description");
            writer.newLine();
            for (Transaction tx : account.getTransactions()) {
                writer.write(csv(tx.getTransactionId()) + "," +
                        csv(tx.getFormattedDateTime()) + "," +
                        csv(tx.getType()) + "," +
                        csv(tx.getAmount().toPlainString()) + "," +
                        csv(tx.getBalanceAfter().toPlainString()) + "," +
                        csv(tx.getDescription()));
                writer.newLine();
            }
        }
        return file;
    }

    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
