package com.smartbank.models;

import java.math.BigDecimal;

public class SavingsAccount extends BankAccount {
    private final BigDecimal minimumBalance;

    public SavingsAccount(String accountNumber, String accountHolder,
                          BigDecimal openingBalance, BigDecimal minimumBalance) {
        super(accountNumber, accountHolder, "SAVINGS", openingBalance);
        if (minimumBalance == null || minimumBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimum balance cannot be negative.");
        }
        if (openingBalance.compareTo(minimumBalance) < 0) {
            throw new IllegalArgumentException("Opening balance must be at least the minimum balance.");
        }
        this.minimumBalance = minimumBalance;
    }

    @Override
    protected BigDecimal getMinimumPermittedBalance() { return minimumBalance; }

    @Override
    protected String getWithdrawalFailureMessage() {
        return "Insufficient funds or minimum balance rule violated. Minimum balance: " + minimumBalance;
    }

    @Override
    public String getAccountRuleSummary() { return "Minimum balance: " + minimumBalance; }

    public BigDecimal getMinimumBalance() { return minimumBalance; }
}
