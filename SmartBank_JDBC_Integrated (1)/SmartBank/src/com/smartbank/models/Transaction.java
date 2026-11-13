package com.smartbank.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Transaction {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final String transactionId;
    private final LocalDateTime dateTime;
    private final String type;
    private final BigDecimal amount;
    private final String description;
    private final BigDecimal balanceAfter;

    public Transaction(String type, BigDecimal amount, String description,
                       BigDecimal balanceAfter) {
        this.transactionId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.dateTime = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.balanceAfter = balanceAfter;
    }

    public String getTransactionId() { return transactionId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getFormattedDateTime() { return dateTime.format(FORMATTER); }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
}
