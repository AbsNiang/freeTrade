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
}
