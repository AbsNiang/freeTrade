package com.example.freetradewip.Data.Objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/*
 * When displaying stocks we'll only display the current ones
 */

public class Stock {
    private final String stockName;
    private final LocalDateTime purchaseDate;
    private final BigDecimal pricePerShare;
    private boolean isCurrent; // if the stock is currently owned
    private final double quantity; // no of shares purchased

    public Stock(String stockName, LocalDateTime purchaseDate, BigDecimal pricePerShare, boolean isCurrent, double quantity) {
        this.stockName = stockName;
        this.purchaseDate = purchaseDate;
        this.pricePerShare = pricePerShare;
        this.isCurrent = isCurrent;
        this.quantity = quantity;
    }

    // parses an activity into a stock
    public static Stock parseStock(Activity activity) {
        return new Stock(
                activity.getTitle(),
                activity.getTimestamp(),
                getPPS(activity),
                true, // as we only add parse from activity when a PURCHASE is made
                activity.getQuantity());
    }

    // calculates the price per share
    private static BigDecimal getPPS(Activity activity) {
        return BigDecimal.valueOf(activity.getTotalAmount())
                .divide(BigDecimal.valueOf(activity.getQuantity()),
                        8,
                        RoundingMode.HALF_UP);
    }

    public void setCurrent(boolean current) {
        this.isCurrent = current;
    }

    @Override
    public String toString() {
        return "Name: " + stockName
                + ", Purchased On: " + purchaseDate
                + ", PPS: " + pricePerShare
                + ", Quantity bought: " + quantity;
    }
}