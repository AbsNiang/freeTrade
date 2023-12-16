package com.example.freetradewip.Data;

import com.example.freetradewip.Data.Objects.Activity;
import com.example.freetradewip.Data.Objects.Stock;
import com.example.freetradewip.Data.Objects.Transaction;

/*
 * This class will be used for anything we do with the activities CSV, primarily mapping the activity to a stock and/or
 * transaction. It will also be used for calling the methods which store these into the database.
 */
public class ActivityHandling {

    // converts the activity to a transaction, and stores it in DB
    public static void addTransactionToDB(Activity activity) {
        // search Stock table for the given title, and if not found then we know it is a new stock, so we record it
        if (DatabaseHandling.searchForStock(activity.getTitle()) == null) { // if stock doesn't exist
            addStockToDB(activity);
        }

        // record transaction always
        Transaction transaction = Transaction.parseTransaction(activity);
        DatabaseHandling.recordTransaction(transaction);

        switch (transaction.getTransaction()) {
            case PURCHASE:
                DatabaseHandling.updateCurrentQuantity(transaction.getStockName(), transaction.getQuantity());
                break;
            case SALE:
                DatabaseHandling.updateCurrentQuantity(transaction.getStockName(), -transaction.getQuantity());
                break;
        }
    }

    // we store the stock in the DB (only reached if a purchase since otherwise stock will be a current stock)
    private static void addStockToDB(Activity activity) {
        activity.setQuantity(0); // want to set it to 0 to not add the quantity twice
        DatabaseHandling.recordStock(Stock.parseStock(activity));
        // initialises the stock w a quantity of 0 since we later add on the quantity
    }

}
