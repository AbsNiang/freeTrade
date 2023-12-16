package com.example.freetradewip.Data;

import com.example.freetradewip.Data.Objects.Activity;
import com.example.freetradewip.Data.Objects.Stock;
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

        if (stockID == -1) { // if stock doesn't exist
            addStockToDB(activity);
            stockID = DatabaseHandling.searchForStock(activity.getTitle()); // update stockID
        }

        // record transaction
        Transaction transaction = Transaction.parseTransaction(activity, stockID);
        DatabaseHandling.recordTransaction(transaction);

        //if it is a SALE, then we uncheck the isCurrent
        if (transaction.getTransaction() == TransactionType.SALE) {
            //TODO: if the quantity of stock is less than the quantity for the current purchase, then we update
            //TODO: the quantity. If it is more (after rounding), then we subtract from any other occurrences of
            //TODO: that stock, that are isCurrent. If none left then an error (unit test for this)

            double quantitySold = transaction.getQuantity();
            double mostRecentSameStockQuantity = DatabaseHandling.getMostRecentStockQuantity(activity.getTitle());
            dealWithSale(quantitySold, mostRecentSameStockQuantity, activity.getTitle());
        }
    }

    // we store the stock in the DB (only reached if a purchase since otherwise stock will be a current stock)
    private static void addStockToDB(Activity activity) {
        DatabaseHandling.recordStock(Stock.parseStock(activity));
    }

    // recursive method that will:
    // un-tick the isCurrent if QS >= RSQ, and then if it is >, it will replace RSQ with the previous occurrence of it
    private static void dealWithSale(double quantitySold, double recentStockQuantity, String stockName){
        if (quantitySold < recentStockQuantity){
            DatabaseHandling.updateStockQuantity(stockName, recentStockQuantity - quantitySold);
        }else if (quantitySold == recentStockQuantity){
            DatabaseHandling.updateStockQuantity(stockName, 0);
            DatabaseHandling.uncheckCurrent(stockName);
        }else { // if >
            DatabaseHandling.updateStockQuantity(stockName, 0); // want it to reach 0
            DatabaseHandling.uncheckCurrent(stockName);
            double newRecentStockQuantity = DatabaseHandling.getMostRecentStockQuantity(stockName);
            dealWithSale(quantitySold - recentStockQuantity, newRecentStockQuantity, stockName);
        }
    }
}
