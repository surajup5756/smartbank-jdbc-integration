package com.smartbank.main;

import com.smartbank.models.*;
import com.smartbank.services.Bank;
import com.smartbank.services.StatementService;
import com.smartbank.utils.ConsoleTable;
import com.smartbank.utils.InputUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Bank BANK = new Bank();
    private static final StatementService STATEMENTS = new StatementService();

    public static void main(String[] args) {
        seedDemoAccounts();
        boolean running = true;
        while (running) {
            printMenu();
            int choice = InputUtil.readChoice(SCANNER);
            try {
                switch (choice) {
                    case 1: createSavings(); break;
                    case 2: createCurrent(); break;
                    case 3: listAccounts(); break;
                    case 4: searchAccount(); break;
                    case 5: deposit(); break;
                    case 6: withdraw(); break;
                    case 7: transfer(); break;
                    case 8: checkBalance(); break;
                    case 9: transactionHistory(); break;
                    case 10: accountStatement(); break;
                    case 11: changeStatus(); break;
                    case 12: statistics(); break;
                    case 13: exportStatement(); break;
                    case 0: running = false; System.out.println("\nThank you for using SmartBank."); break;
                    default: System.out.println("Invalid menu option. Select 0-13.");
                }
            } catch (RuntimeException ex) {
                System.out.println("\n[TRANSACTION FAILED] " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("\n[ERROR] " + ex.getMessage());
            }
            if (running) {
                System.out.println("\nPress Enter to continue...");
                SCANNER.nextLine();
            }
        }
        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println("\n==============================================================");
        System.out.println("                  SMARTBANK BANKING SYSTEM");
        System.out.println("==============================================================");
        System.out.println(" 1. Create Savings Account       2. Create Current Account");
        System.out.println(" 3. View All Accounts            4. Search Account");
        System.out.println(" 5. Deposit Money                6. Withdraw Money");
        System.out.println(" 7. Transfer Money               8. Check Balance");
        System.out.println(" 9. Transaction History         10. Generate Account Statement");
        System.out.println("11. Block / Unblock / Close     12. Banking Statistics");
        System.out.println("13. Export Statement (CSV)       0. Exit");
        System.out.println("--------------------------------------------------------------");
    }

    // These sample accounts make it possible to test transfers immediately after launch.
    private static void seedDemoAccounts() {
        BANK.createSavingsAccount("Aarav Sharma", new BigDecimal("10000.00"), new BigDecimal("2000.00"));
        BANK.createCurrentAccount("Neha Verma", new BigDecimal("25000.00"), new BigDecimal("10000.00"));
    }

    private static void createSavings() {
        String name = InputUtil.readNonEmpty(SCANNER, "Account holder name: ");
        BigDecimal minimum = InputUtil.readMoney(SCANNER, "Minimum balance (INR): ");
        BigDecimal opening = InputUtil.readMoney(SCANNER, "Opening deposit (INR): ");
        SavingsAccount account = BANK.createSavingsAccount(name, opening, minimum);
        System.out.println("Savings account created successfully. Account number: " + account.getAccountNumber());
    }

    private static void createCurrent() {
        String name = InputUtil.readNonEmpty(SCANNER, "Account holder name: ");
        BigDecimal overdraft = InputUtil.readMoney(SCANNER, "Overdraft limit (INR): ");
        BigDecimal opening = InputUtil.readMoney(SCANNER, "Opening deposit (INR): ");
        CurrentAccount account = BANK.createCurrentAccount(name, opening, overdraft);
        System.out.println("Current account created successfully. Account number: " + account.getAccountNumber());
    }

    private static void listAccounts() {
        List<String[]> rows = new ArrayList<String[]>();
        for (BankAccount account : BANK.getAllAccounts()) {
            rows.add(new String[] {account.getAccountNumber(), account.getAccountHolder(),
                    account.getAccountType(), account.getBalance().toPlainString(),
                    account.getStatus().toString(), account.getAccountRuleSummary()});
        }
        ConsoleTable.print(new String[] {"ACCOUNT NO.", "ACCOUNT HOLDER", "TYPE", "BALANCE (INR)", "STATUS", "ACCOUNT RULE"}, rows);
    }

    private static void searchAccount() {
        String number = InputUtil.readNonEmpty(SCANNER, "Enter account number: ");
        BankAccount account = BANK.getAccount(number);
        List<String[]> rows = new ArrayList<String[]>();
        rows.add(new String[] {account.getAccountNumber(), account.getAccountHolder(),
                account.getAccountType(), account.getBalance().toPlainString(),
                account.getStatus().toString(), account.getAccountRuleSummary()});
        ConsoleTable.print(new String[] {"ACCOUNT NO.", "ACCOUNT HOLDER", "TYPE", "BALANCE (INR)", "STATUS", "ACCOUNT RULE"}, rows);
    }

    private static void deposit() {
        BankAccount account = BANK.getAccount(InputUtil.readNonEmpty(SCANNER, "Account number: "));
        BigDecimal amount = InputUtil.readMoney(SCANNER, "Deposit amount (INR): ");
        account.deposit(amount);
        System.out.println("Deposit successful. Updated balance: INR " + account.getBalance());
    }

    private static void withdraw() {
        BankAccount account = BANK.getAccount(InputUtil.readNonEmpty(SCANNER, "Account number: "));
        BigDecimal amount = InputUtil.readMoney(SCANNER, "Withdrawal amount (INR): ");
        account.withdraw(amount);
        System.out.println("Withdrawal successful. Updated balance: INR " + account.getBalance());
    }

    private static void transfer() {
        String from = InputUtil.readNonEmpty(SCANNER, "From account number: ");
        String to = InputUtil.readNonEmpty(SCANNER, "To account number: ");
        BigDecimal amount = InputUtil.readMoney(SCANNER, "Transfer amount (INR): ");
        BANK.transfer(from, to, amount);
        System.out.println("Transfer successful. Reference: " + from + " -> " + to);
        System.out.println("Sender balance  : INR " + BANK.getAccount(from).getBalance());
        System.out.println("Receiver balance: INR " + BANK.getAccount(to).getBalance());
    }

    private static void checkBalance() {
        BankAccount account = BANK.getAccount(InputUtil.readNonEmpty(SCANNER, "Account number: "));
        System.out.println("\nAccount holder : " + account.getAccountHolder());
        System.out.println("Account type   : " + account.getAccountType());
        System.out.println("Account status : " + account.getStatus());
        System.out.println("Available balance: INR " + account.getBalance());
    }

    private static void transactionHistory() {
        BankAccount account = BANK.getAccount(InputUtil.readNonEmpty(SCANNER, "Account number: "));
        List<String[]> rows = new ArrayList<String[]>();
        for (Transaction tx : account.getTransactions()) {
            rows.add(new String[] {tx.getTransactionId(), tx.getFormattedDateTime(),
                    tx.getType(), tx.getAmount().toPlainString(),
                    tx.getBalanceAfter().toPlainString(), tx.getDescription()});
        }
        ConsoleTable.print(new String[] {"TXN ID", "DATE & TIME", "TYPE", "AMOUNT", "BALANCE AFTER", "DESCRIPTION"}, rows);
    }

    private static void accountStatement() {
        BankAccount account = BANK.getAccount(InputUtil.readNonEmpty(SCANNER, "Account number: "));
        STATEMENTS.printStatement(account);
    }

    private static void changeStatus() {
        String number = InputUtil.readNonEmpty(SCANNER, "Account number: ");
        System.out.println("1. Block  2. Unblock  3. Close");
        int choice = InputUtil.readChoice(SCANNER);
        AccountStatus status;
        if (choice == 1) status = AccountStatus.BLOCKED;
        else if (choice == 2) status = AccountStatus.ACTIVE;
        else if (choice == 3) status = AccountStatus.CLOSED;
        else { System.out.println("Invalid status option."); return; }
        BANK.changeAccountStatus(number, status);
        System.out.println("Account status updated to " + status + ".");
    }

    private static void statistics() {
        List<String[]> rows = new ArrayList<String[]>();
        rows.add(new String[] {"Total accounts", String.valueOf(BANK.getAllAccounts().size())});
        rows.add(new String[] {"Savings accounts", String.valueOf(BANK.countByType("SAVINGS"))});
        rows.add(new String[] {"Current accounts", String.valueOf(BANK.countByType("CURRENT"))});
        rows.add(new String[] {"Active accounts", String.valueOf(BANK.countByStatus(AccountStatus.ACTIVE))});
        rows.add(new String[] {"Blocked accounts", String.valueOf(BANK.countByStatus(AccountStatus.BLOCKED))});
        rows.add(new String[] {"Closed accounts", String.valueOf(BANK.countByStatus(AccountStatus.CLOSED))});
        rows.add(new String[] {"Net positive balances (INR)", BANK.getTotalDeposits().toPlainString()});
        ConsoleTable.print(new String[] {"BANKING METRIC", "VALUE"}, rows);
    }

    private static void exportStatement() throws Exception {
        BankAccount account = BANK.getAccount(InputUtil.readNonEmpty(SCANNER, "Account number: "));
        java.io.File file = STATEMENTS.exportCsv(account);
        System.out.println("Statement exported successfully: " + file.getAbsolutePath());
    }
}
