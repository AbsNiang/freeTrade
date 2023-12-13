package com.example.freetradewip.Data.Objects;

import java.time.LocalDateTime;

/*
 !DIVIDEND
 ?ORDER
MONTHLY_STATEMENT - can ignore
TOP_UP - can ignore
 !PROPERTY
 !CAPITAL
 !INTEREST
 !SPECIAL_DIVIDEND
 */
public class Activity { // for CSV
    private String title;
    private String type; // order if either a purchase or a sale, dividend otherwise
    private LocalDateTime timestamp;
    private double totalAmount;
    private String buySell; // if the stock has been bought or sold (empty if dividend)

    // returns if the type is a type of dividend in client's case
    public static boolean isTypeOfDividend(String type){
        // excluding TOP_UP and MONTHLY_STATEMENT as these aren't transactions we want
        return "DIVIDEND".equals(type) || "ORDER".equals(type) ||
                "PROPERTY".equals(type) || "CAPITAL".equals(type) || "INTEREST".equals(type) ||
                "SPECIAL_DIVIDEND".equals(type);
    }

    // returns if it is a purchase or not (if not then it is a sale)
    public static boolean isPurchase(String buySell){
        //TODO: test needs to verify all ORDER's have either BUY or SELL
        return "BUY".equals(buySell);
    }

    public Activity(String title, String type, LocalDateTime timestamp, double totalAmount, String buySell) {
        this.title = title;
        this.type = type;
        this.timestamp = timestamp;
        this.totalAmount = totalAmount;
        this.buySell = buySell;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBuySell() {
        return buySell;
    }

    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }
}
