package com.example.freetradewip.Data;

import com.example.freetradewip.Data.Objects.Activity;
import com.example.freetradewip.Data.Objects.Stock;
import com.example.freetradewip.Data.Objects.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class DatabaseHandling {
    // DB URL
    private static final String dbURL = "jdbc:ucanaccess://" + System.getProperty("user.dir") + "/database.accdb";

    // returns the id of the stock that matches the name, and is currently owned by the user
    public static String searchForStock(String stockName) {
        try {
            Connection connection = DriverManager.getConnection(dbURL);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Stock WHERE StockName = ? ");
            preparedStatement.setString(1, stockName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("StockName");
            }

            // closing connections
            preparedStatement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // returns the date of the most recent transaction, so we only get activities of past this date
    public static LocalDateTime getWhenLastUpdated() {
        try (Connection connection = DriverManager.getConnection(dbURL)) {
            String query = "SELECT MAX(TransactionDate) AS MostRecentDate FROM Transaction;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    java.sql.Timestamp mostRecentDate = resultSet.getTimestamp("MostRecentDate");
                    if (mostRecentDate != null) {
                        return mostRecentDate.toLocalDateTime();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("No transactions found.");
        return LocalDateTime.MIN;
    }

    // records a new transaction in the DB
    public static void recordTransaction(Transaction transaction) {
        try (Connection connection = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Transaction (StockName, TransactionDate, Type, Amount, Quantity) " +
                             "VALUES (?, ?, ?, ?, ?)")) {

            preparedStatement.setString(1, transaction.getStockName());
            Timestamp dateTime = Timestamp.valueOf(transaction.getTransactionDate());
            preparedStatement.setTimestamp(2, dateTime);
            preparedStatement.setString(3, transaction.getTransaction().getTypeString());
            preparedStatement.setDouble(4, transaction.getMoneyAmount());
            preparedStatement.setDouble(5, transaction.getQuantity());

            preparedStatement.executeUpdate(); // execute SQL
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // records a new stock in the DB
    public static void recordStock(Stock stock) {
        try (Connection connection = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Stock (StockName, Quantity) " +
                             "VALUES (?, ?)")) {

            preparedStatement.setString(1, stock.getStockName());
            preparedStatement.setDouble(2, stock.getQuantity());

            preparedStatement.executeUpdate(); // execute SQL
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // adds the quantity to add with the quantity stored in the record for that stock
    public static void updateCurrentQuantity(String stockName, double quantityToAdd) {
        String updateQuery = "UPDATE Stock SET CurrentQuantity = CurrentQuantity + ? WHERE StockName = ?";

        try (Connection connection = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setDouble(1, quantityToAdd);
            preparedStatement.setString(2, stockName);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Current quantity updated successfully.");
            } else {
                System.out.println("No rows updated. Stock not found?");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // going to test all we've done so far:
    public static void main(String[] args) {
        LocalDateTime lastUpdated = getWhenLastUpdated();
        List<Activity> activityList = CSVHandling.getActivityFromCSV(lastUpdated);
        for (Activity activity:activityList) {
            ActivityHandling.addTransactionToDB(activity);
        }

    }

}
