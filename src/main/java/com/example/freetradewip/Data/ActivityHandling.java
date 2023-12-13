package com.example.freetradewip.Data;

import com.example.freetradewip.Data.Objects.Activity;
import com.example.freetradewip.Data.Objects.Transaction;
import com.example.freetradewip.Data.Objects.TransactionType;

/*
 * This class will be used for anything we do with the activities CSV, primarily mapping the activity to a stock and/or
 * transaction. It will also be used for calling the methods which store these into the database.
 */
public class ActivityHandling {

    // converts the activity to a transaction, and stores it in DB
    public static void addTransactionToDB(Activity activity) {
        // search Stock table for the given title, and if not found then we know it is a new stock, so we record it
        int stockID = DatabaseHandling.searchForStock(activity.getTitle());
        if (stockID != -1) { // if stock exists already
            Transaction transaction = new Transaction(
                    stockID,
                    activity.getTimestamp(),
                    getActivityTypeAsTransactionType(activity),
                    activity.getTotalAmount());
            DatabaseHandling.recordTransaction(transaction);
            //TODO: if type is SALE, then we remove from Stock table
        } else { // if -1 then it means that the stock isn't in the DB yet.
            addStockToDB(activity);
        }

    }

    // we store the stock in the DB
    private static void addStockToDB(Activity activity) {
        DatabaseHandling.recordStock();
    }

    private static TransactionType getActivityTypeAsTransactionType(Activity activity) {
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
