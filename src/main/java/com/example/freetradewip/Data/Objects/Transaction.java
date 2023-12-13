package com.example.freetradewip.Data.Objects;

import java.time.LocalDateTime;

public class Transaction {
    private int stockID; // foreign key of corresponding stock
    private LocalDateTime transactionDate;
    private TransactionType transaction; // sale, dividend or purchase
    private double amount; // either +ve or -ve

    public Transaction(int stockID, LocalDateTime transactionDate, TransactionType transaction, double amount) {
        this.stockID = stockID;
        this.transactionDate = transactionDate;
        this.transaction = transaction;
        this.amount = amount;
    }
}
