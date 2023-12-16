package com.example.freetradewip.Data.Objects;

import java.time.LocalDateTime;

public class Transaction {
    private final String stockName; // foreign key of corresponding stock
    private final LocalDateTime transactionDate;
    private final TransactionType transaction; // sale, dividend or purchase
    private final double moneyAmount; // either +ve or -ve
    private final double quantity; // quantity of shares the transaction relates to

    public Transaction(String stockName, LocalDateTime transactionDate, TransactionType transaction, double amount, double quantity) {
        this.stockName = stockName;
        this.transactionDate = transactionDate;
        this.transaction = transaction;
        this.moneyAmount = amount;
        this.quantity = quantity;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public TransactionType getTransaction() {
        return transaction;
    }

    // Parses an activity to a transaction
    public static Transaction parseTransaction(Activity activity) {
        return new Transaction(
                activity.getTitle(),
                activity.getTimestamp(),
                TransactionType.getActivityTypeAsTransactionType(activity),
                activity.getTotalAmount(),
                activity.getQuantity());
    }

    public double getQuantity() {
        return quantity;
    }

    public String getStockName(){
        return stockName;
    }
}
