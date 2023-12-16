package com.example.freetradewip.Data.Objects;

public enum TransactionType {
    PURCHASE("Purchase"),
    DIVIDEND("Dividend"),
    SALE("Sale");

    private final String typeString;

    TransactionType(String typeString) {
        this.typeString = typeString;
    }

    public String getTypeString() {
        return typeString;
    }

    public static TransactionType getActivityTypeAsTransactionType(Activity activity) {
        if (!"ORDER".equals(activity.getType())) {
            return TransactionType.DIVIDEND;
        } else { // if it is an ORDER, need to check the BUY/SELL
            if (Activity.isPurchase(activity.getBuySell())) {
                return TransactionType.PURCHASE;
            } else {
                return TransactionType.SALE;
            }
        }
    }
}
