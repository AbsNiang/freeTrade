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

    // Method to get TransactionType from a string
    public static TransactionType fromString(String input) {
        for (TransactionType type : TransactionType.values()) {
            if (type.getTypeString().equalsIgnoreCase(input)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with typeString: " + input);
    }

    public static TransactionType getActivityTypeAsTransactionType(Activity activity) {
        if (Activity.isTypeOfDividend(activity.getType())) {
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
