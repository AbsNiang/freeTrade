package com.example.freetradewip.Data;

import com.example.freetradewip.Data.Objects.Stock;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandling {
    // DB URL
    private static final String dbURL ="jdbc:ucanaccess://" +  System.getProperty("user.dir") + "/database.accdb";
    public static void test(){
        try{
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

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int searchForStock(String stockName){
        List<Stock> stocks = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(dbURL);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Stock WHERE StockName = ?");
            preparedStatement.setString(1, stockName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                System.out.println("found");
                return resultSet.getInt("StockID");
            }

            // closing connections
            preparedStatement.close();
            resultSet.close();
            connection.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    // returns the date of the most recent transaction, so we only get activities of past this date
    public static LocalDateTime getWhenLastUpdated(){
        try (Connection connection = DriverManager.getConnection(dbURL)) {
            String query = "SELECT MAX(TransactionDate) AS MostRecentDate FROM Transaction;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    java.sql.Timestamp mostRecentDate = resultSet.getTimestamp("MostRecentDate");
                    if (mostRecentDate != null){
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
    public static void main(String[] args){
        System.out.println("id: " + searchForStock("filler"));
        System.out.println("id: " + searchForStock("other"));
    }
}
