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
}
