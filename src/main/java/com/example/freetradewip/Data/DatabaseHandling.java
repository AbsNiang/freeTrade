package com.example.freetradewip.Data;

import com.example.freetradewip.Data.Objects.Stock;
import com.example.freetradewip.Data.Objects.Transaction;

import java.sql.*;
import java.time.LocalDateTime;

public class DatabaseHandling {
    // DB URL
    private static final String dbURL = "jdbc:ucanaccess://" + System.getProperty("user.dir") + "/database.accdb";

    public static void test() {
        try {
            Connection connection = DriverManager.getConnection(dbURL);

            String sql = "INSERT INTO Transaction (StockID, TransactionDate, Type, Amount) VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 3);
            Timestamp dateTime = Timestamp.valueOf(LocalDateTime.now());
            preparedStatement.setTimestamp(2, dateTime);
            preparedStatement.setString(3, "DIVIDEND");
            preparedStatement.setDouble(4, 122.25);
            preparedStatement.executeUpdate(); // execute sql
            // closing connections
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // returns the id of the stock that matches the name, and is currently owned by the user
    public static int searchForStock(String stockName) {
        try {
            Connection connection = DriverManager.getConnection(dbURL);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Stock WHERE StockName = ? " +
                            "AND isCurrent = TRUE");
            preparedStatement.setString(1, stockName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("found");
                return resultSet.getInt("StockID");
            }

            // closing connections
            preparedStatement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
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

    public static void recordTransaction(Transaction transaction) {

    }

    public static void recordStock(Stock stock) {

    }

    public static double getMostRecentStockQuantity(String name) {
        try (Connection connection = DriverManager.getConnection(dbURL)) {
            String query = "SELECT Quantity " +
                    "FROM Stock " +
                    "WHERE StockName = ? AND IsCurrent = TRUE " +
                    "ORDER BY PurchaseDate DESC LIMIT 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("Quantity");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("No stock found?");
    }

    public static void main(String[] args) {
        System.out.println("id: " + searchForStock("filler"));
        System.out.println("id: " + searchForStock("other"));
    }

    public static void updateStockQuantity(int stockID, double newQuantity) {
        try (Connection connection = DriverManager.getConnection(dbURL)) {
            String updateQuery = "UPDATE Stock SET Quantity = ? WHERE StockID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setDouble(1, newQuantity);
                preparedStatement.setInt(2, stockID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Stock quantity updated successfully.");
                } else {
                    System.out.println("No stock found with StockID: " + stockID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
