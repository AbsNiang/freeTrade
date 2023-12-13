package com.example.freetradewip.Data.Objects;

import java.time.LocalDateTime;

public class Stock {
    private String stockName;
    private LocalDateTime purchaseDate;
    private double purchasePrice;

    public Stock(String stockName, LocalDateTime purchaseDate, double purchasePrice) {
        this.stockName = stockName;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    @Override
    public String toString(){
        return "Name: " + stockName
                + ", Purchased On: " + purchaseDate
                + ", Spent: " + purchasePrice;
    }
}
