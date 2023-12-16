package com.example.freetradewip.Data.Objects;

/*
 * When displaying stocks we'll only display the current ones
 */

public class Stock {
    private final String stockName;
    private final double quantity; // no of shares currently owned

    public Stock(String stockName, double quantity) {
        this.stockName = stockName;
        this.quantity = quantity;
    }

    // parses an activity into a stock
    public static Stock parseStock(Activity activity) {
        return new Stock(
                activity.getTitle(),
                activity.getQuantity());//TODO need to add it to the amount or sum
    }

    public String getStockName() {
        return stockName;
    }

    public double getQuantity() {
        return quantity;
    }
}