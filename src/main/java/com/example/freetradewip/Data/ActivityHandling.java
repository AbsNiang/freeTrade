package com.example.freetradewip.Data;

import com.example.freetradewip.Data.Objects.Activity;
import com.example.freetradewip.Data.Objects.Transaction;

import java.util.List;

/*
 * This class will be used for anything we do with the activities CSV, primarily mapping the activity to a stock and/or
 * transaction. It will also be used for calling the methods which store these into the database.
 */
public class ActivityHandling {

    // converts the activity to a transaction, and stores it in DB
    public static void addTransactionToDB(Activity activity){
        // search Stock table for the given title, and if not found then we know it is a new stock so we record it

    }

    // (only reached if it is an ORDER activity) then we store the stock in the DB
    public static void addStockToDB(Activity activity){

    }
}
