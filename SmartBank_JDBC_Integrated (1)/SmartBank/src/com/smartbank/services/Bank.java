package com.smartbank.services;

import com.smartbank.models.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Bank {
    // Account numbers are the lookup keys, so searching for an account is direct.
    private final Map<String, BankAccount> accounts = new HashMap<String, BankAccount>();
    private long nextAccountNumber = 100001L;

    public synchronized SavingsAccount createSavingsAccount(String holder,
            BigDecimal openingBalance, BigDecimal minimumBalance) {
        String number = generateAccountNumber();
        SavingsAccount account = new SavingsAccount(number, holder, openingBalance, minimumBalance);
        accounts.put(number, account);
        return account;
    }

    public synchronized CurrentAccount createCurrentAccount(String holder,
            BigDecimal openingBalance, BigDecimal overdraftLimit) {
        String number = generateAccountNumber();
        CurrentAccount account = new CurrentAccount(number, holder, openingBalance, overdraftLimit);
        accounts.put(number, account);
        return account;
    }

    private String generateAccountNumber() {
        return String.valueOf(nextAccountNumber++);
    }

    public synchronized BankAccount getAccount(String accountNumber) {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account;
    }

    public synchronized Collection<BankAccount> getAllAccounts() {
        return Collections.unmodifiableCollection(accounts.values());
    }

    // Validate both sides before applying the debit and credit entries.
    public synchronized void transfer(String fromNumber, String toNumber, BigDecimal amount) {
        if (fromNumber == null || toNumber == null || fromNumber.equals(toNumber)) {
            throw new IllegalArgumentException("Source and destination accounts must be different.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.scale() > 2) {
            throw new IllegalArgumentException("Transfer amount must be positive and have at most 2 decimal places.");
        }

        BankAccount source = getAccount(fromNumber);
        BankAccount destination = getAccount(toNumber);
        if (source.getStatus() != AccountStatus.ACTIVE ||
                destination.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Both accounts must be active to transfer funds.");
        }

        // Validate source funds before changing either account.
        BigDecimal sourceFloor = source instanceof SavingsAccount
                ? ((SavingsAccount) source).getMinimumBalance()
                : ((CurrentAccount) source).getOverdraftLimit().negate();

        if (source.getBalance().subtract(amount).compareTo(sourceFloor) < 0) {
            throw new IllegalStateException("Insufficient funds or account limit would be violated.");
        }

        source.addTransferDebit(amount, "Transfer to " + toNumber);
        destination.addTransferCredit(amount, "Transfer from " + fromNumber);
    }

    public synchronized void changeAccountStatus(String accountNumber, AccountStatus status) {
        BankAccount account = getAccount(accountNumber);
        if (status == AccountStatus.CLOSED && account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Account can be closed only when its balance is zero.");
        }
        account.setStatus(status);
    }

    public synchronized BigDecimal getTotalDeposits() {
        BigDecimal total = BigDecimal.ZERO;
        for (BankAccount account : accounts.values()) {
            if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                total = total.add(account.getBalance());
            }
        }
        return total;
    }

    public synchronized int countByType(String type) {
        int count = 0;
        for (BankAccount account : accounts.values()) {
            if (account.getAccountType().equals(type)) count++;
        }
        return count;
    }

    public synchronized int countByStatus(AccountStatus status) {
        int count = 0;
        for (BankAccount account : accounts.values()) {
            if (account.getStatus() == status) count++;
        }
        return count;
    }
}
