package com.example.freetradewip.Data;

import com.example.freetradewip.Data.Objects.Transaction;
import com.example.freetradewip.Data.Objects.TransactionType;
import com.example.freetradewip.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class GainLoss {

    public static ObservableList<Transaction> getGainLoss() {
        ObservableList<Transaction> transactions = DatabaseHandling.getGainLossData();
        cleanData(transactions);
        return calcGains(transactions);
    }

    private static void cleanData(ObservableList<Transaction> transactions) {
        Comparator<Transaction> dateComparator = Comparator.comparing(Transaction::getTransactionDate)
                .reversed(); // To sort from newest to oldest
        transactions.sort(dateComparator);
        cleanTransactions(transactions);
    }

    private static ObservableList<Transaction> calcGains(ObservableList<Transaction> transactions) {
        ObservableList<Transaction> gains = FXCollections.observableArrayList();
        // list is starting w latest, so it's calm
        // for each sale we get the profit gained
        for (Transaction sale : transactions) {
            if (sale.getTransaction() == TransactionType.SALE) {
                String stockName = sale.getStockName();
                double quantitySold = sale.getQuantity();

                double totalPurchaseCosts = 0;
                // for each purchase for that stock
                for (Transaction purchase : transactions) {
                    if (purchase.getTransaction() == TransactionType.PURCHASE &&
                            stockName.equals(purchase.getStockName()) &&
                            purchase.getTransactionDate().isBefore(sale.getTransactionDate())) {

                        if (quantitySold > purchase.getQuantity()) {
                            // if more then the profit includes all of it
                            totalPurchaseCosts += purchase.getMoneyAmount();
                            quantitySold -= purchase.getQuantity();
                        } else { // should never be < theoretically
                            double pricePerShare = purchase.getMoneyAmount() / purchase.getQuantity();
                            totalPurchaseCosts += pricePerShare * quantitySold;
                            break;
                        }
                    }
                }

                gains.add(new Transaction( // represents the gain / loss
                        stockName,
                        sale.getTransactionDate(),
                        TransactionType.SALE, // not rly but not important
                        Utils.formatDouble(sale.getMoneyAmount() - totalPurchaseCosts),
                        quantitySold // (always 0)
                ));
            }


        }
        return gains;
    }

    private static void cleanTransactions(List<Transaction> transactions) {
        // Identify stock names with purchase transactions
        java.util.Set<String> purchaseStocks = new java.util.HashSet<>();
        for (Transaction transaction : transactions) {
            if (TransactionType.PURCHASE.equals(transaction.getTransaction())) {
                purchaseStocks.add(transaction.getStockName());
            }
        }

        // Remove purchase transactions without a corresponding sale transaction
        Iterator<Transaction> iterator = transactions.iterator();
        while (iterator.hasNext()) {
            Transaction transaction = iterator.next();
            if (TransactionType.PURCHASE.equals(transaction.getTransaction())) {
                String stockName = transaction.getStockName();
                if (!hasSaleTransaction(transactions, stockName)) {
                    iterator.remove();
                }
            }
        }
    }

    private static boolean hasSaleTransaction(List<Transaction> transactions, String stockName) {
        for (Transaction transaction : transactions) {
            if (TransactionType.SALE.equals(transaction.getTransaction()) &&
                    stockName.equals(transaction.getStockName())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        for (Transaction transaction : getGainLoss()) {
            System.out.println(transaction.getStockName() + ", " + transaction.getTransaction() +
                    ", £" + transaction.getMoneyAmount() + ", " + transaction.getQuantity());
        }
    }
}
