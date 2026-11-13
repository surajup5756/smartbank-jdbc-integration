package com.smartbank.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BankAccount {
    private final String accountNumber;
    private final String accountHolder;
    private final String accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private final List<Transaction> transactions;

    protected BankAccount(String accountNumber, String accountHolder,
                          String accountType, BigDecimal openingBalance) {
        if (accountHolder == null || accountHolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name is required.");
        }
        validatePositive(openingBalance);
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder.trim();
        this.accountType = accountType;
        this.balance = openingBalance;
        this.status = AccountStatus.ACTIVE;
        this.transactions = new ArrayList<Transaction>();
        addTransaction("OPENING", openingBalance, "Initial account deposit");
    }

    // Keep validation and balance update together so one operation cannot be half-applied.
    public synchronized void deposit(BigDecimal amount) {
        ensureActive();
        validatePositive(amount);
        balance = balance.add(amount);
        addTransaction("DEPOSIT", amount, "Cash deposit");
    }

    public synchronized void withdraw(BigDecimal amount) {
        ensureActive();
        validatePositive(amount);
        BigDecimal minimumAllowed = getMinimumPermittedBalance();
        BigDecimal resultingBalance = balance.subtract(amount);
        if (resultingBalance.compareTo(minimumAllowed) < 0) {
            throw new IllegalStateException(getWithdrawalFailureMessage());
        }
        balance = resultingBalance;
        addTransaction("WITHDRAWAL", amount, "Cash withdrawal");
    }

    // Each account type supplies its own lower balance boundary.
    protected abstract BigDecimal getMinimumPermittedBalance();
    protected abstract String getWithdrawalFailureMessage();

    public synchronized void addTransferDebit(BigDecimal amount, String description) {
        ensureActive();
        validatePositive(amount);
        BigDecimal resultingBalance = balance.subtract(amount);
        if (resultingBalance.compareTo(getMinimumPermittedBalance()) < 0) {
            throw new IllegalStateException(getWithdrawalFailureMessage());
        }
        balance = resultingBalance;
        addTransaction("TRANSFER OUT", amount, description);
    }

    public synchronized void addTransferCredit(BigDecimal amount, String description) {
        ensureActive();
        validatePositive(amount);
        balance = balance.add(amount);
        addTransaction("TRANSFER IN", amount, description);
    }

    // Save the balance after the operation; this makes the history useful as an audit trail.
    private void addTransaction(String type, BigDecimal amount, String description) {
        transactions.add(new Transaction(type, amount, description, balance));
    }

    protected void ensureActive() {
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is " + status + ". Transaction not allowed.");
        }
    }

    protected static void validatePositive(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount cannot have more than 2 decimal places.");
        }
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public String getAccountType() { return accountType; }
    public synchronized BigDecimal getBalance() { return balance; }
    public synchronized AccountStatus getStatus() { return status; }
    public synchronized void setStatus(AccountStatus status) { this.status = status; }
    public synchronized List<Transaction> getTransactions() {
        return Collections.unmodifiableList(new ArrayList<Transaction>(transactions));
    }
    public abstract String getAccountRuleSummary();
}
