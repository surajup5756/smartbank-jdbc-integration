package com.smartbank.models;

import java.math.BigDecimal;

public class CurrentAccount extends BankAccount {
    private final BigDecimal overdraftLimit;

    public CurrentAccount(String accountNumber, String accountHolder,
                          BigDecimal openingBalance, BigDecimal overdraftLimit) {
        super(accountNumber, accountHolder, "CURRENT", openingBalance);
        if (overdraftLimit == null || overdraftLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    protected BigDecimal getMinimumPermittedBalance() { return overdraftLimit.negate(); }

    @Override
    protected String getWithdrawalFailureMessage() {
        return "Insufficient funds. Overdraft limit: " + overdraftLimit;
    }

    @Override
    public String getAccountRuleSummary() { return "Overdraft limit: " + overdraftLimit; }

    public BigDecimal getOverdraftLimit() { return overdraftLimit; }
}
