package com.example.freetradewip.Data;

import com.example.freetradewip.Data.Objects.Stock;
import com.example.freetradewip.Data.Objects.Transaction;
import com.example.freetradewip.Data.Objects.TransactionType;
import com.example.freetradewip.HelloApplication;
import com.healthmarketscience.jackcess.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;

public class DatabaseHandling {
    // DB URL
    private static final String dbURL = "jdbc:ucanaccess://" + System.getProperty("user.dir") + "/database.mdb";

    private static String getJarDirectory() {
        try {
            // Get the directory where the JAR file is located
            String jarPath = HelloApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String jarDirectory = new File(jarPath).getParent();

            // Convert to the proper format (replace %20 with spaces, etc.)
            return URLDecoder.decode(jarDirectory, StandardCharsets.UTF_8.toString());
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

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
                        System.out.println("Last transaction was: " + mostRecentDate);
                        return mostRecentDate.toLocalDateTime();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("No last transaction");
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
            preparedStatement.setDouble(2, 0);

            preparedStatement.executeUpdate(); // execute SQL
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // adds the quantity to add with the quantity stored in the record for that stock
    public static void updateCurrentQuantity(String stockName, double quantityToAdd) {
        String updateQuery = "UPDATE Stock SET Quantity = Quantity + ? WHERE StockName = ?";

        try (Connection connection = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setDouble(1, quantityToAdd);
            preparedStatement.setString(2, stockName);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // returns a list of stocks that have a quantity > 0
    public static ObservableList<Stock> getCurrentStocks() {
        ObservableList<Stock> stocks = FXCollections.observableArrayList();
        try {
            Connection connection = DriverManager.getConnection(dbURL);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Stock WHERE Quantity > 0");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                stocks.add(new Stock(
                        resultSet.getString("StockName"),
                        resultSet.getDouble("Quantity")
                ));
            }

            // closing connections
            preparedStatement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stocks;
    }

    // going to test all we've done so far:
    public static void main(String[] args) {
        if (new File(System.getProperty("user.dir") + "/database.mdb").exists()) {
            System.out.println("exists");
        }
    }


    private static void test() {
        try (Connection connection = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Stock (StockName, Quantity) " +
                             "VALUES (?, ?)")) {

            preparedStatement.setString(1, "testname");
            preparedStatement.setDouble(2, 1.22200332);

            preparedStatement.executeUpdate(); // execute SQL
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Transaction> getDividends() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        try {
            Connection connection = DriverManager.getConnection(dbURL);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Transaction WHERE Type = ?");
            preparedStatement.setString(1, "Dividend");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getString("StockName"),
                        resultSet.getTimestamp("TransactionDate").toLocalDateTime(),
                        TransactionType.DIVIDEND,
                        resultSet.getDouble("Amount"),
                        resultSet.getDouble("Quantity")
                ));
            }

            // closing connections
            preparedStatement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public static ObservableList<Transaction> getGainLossData() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        try {
            Connection connection = DriverManager.getConnection(dbURL);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Transaction WHERE Type IN (?, ?)");
            preparedStatement.setString(1, "Sale");
            preparedStatement.setString(2, "Purchase");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getString("StockName"),
                        resultSet.getTimestamp("TransactionDate").toLocalDateTime(),
                        TransactionType.fromString(resultSet.getString("Type")),
                        resultSet.getDouble("Amount"),
                        resultSet.getDouble("Quantity")
                ));
            }

            // closing connections
            preparedStatement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public static void initializeDatabase() {
        try {
            String DB_FILE_PATH = System.getProperty("user.dir") + "/database.mdb";
            File dbFile = new File(DB_FILE_PATH);

            // Check if the database file exists
            if (!dbFile.exists()) {
                // If the file doesn't exist, create a new empty database
                createEmptyDatabase(DB_FILE_PATH);
                createTables(DB_FILE_PATH);
                System.out.println("Tables created successfully in the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createEmptyDatabase(String filePath) throws Exception {
        // Create a new Access database file
        Database db = DatabaseBuilder.create(Database.FileFormat.V2010, new File(filePath));
        db.close();
        System.out.println("Empty database created successfully at: " + filePath);
    }


    private static void createTables(String filePath) throws IOException {
        // Open the existing Access database
        Database db = DatabaseBuilder.open(new File(filePath));


        // Create "Stock" table
        Table stockTable = new TableBuilder("Stock")
                .addColumn(new ColumnBuilder("StockName", DataType.TEXT).toColumn())
                .addColumn(new ColumnBuilder("Quantity", DataType.DOUBLE).toColumn())
                .addIndex(new IndexBuilder("PK_Stock").addColumns("StockName").setPrimaryKey())
                .toTable(db);

        // Create "Transaction" table
        Table transactionTable = new TableBuilder("Transaction")
                .addColumn(new ColumnBuilder("TransactionID", DataType.GUID).setAutoNumber(true).toColumn())
                .addColumn(new ColumnBuilder("StockName", DataType.TEXT).toColumn())
                .addColumn(new ColumnBuilder("TransactionDate", DataType.SHORT_DATE_TIME).toColumn())
                .addColumn(new ColumnBuilder("Type", DataType.TEXT).toColumn())
                .addColumn(new ColumnBuilder("Amount", DataType.MONEY).toColumn())
                .addColumn(new ColumnBuilder("Quantity", DataType.DOUBLE).toColumn())
                .addIndex(new IndexBuilder("PK_Transaction").addColumns("TransactionID").setPrimaryKey())
                .toTable(db);

        db.flush();
        db.close();
    }


    public static void checkIfDatabaseExists() {
        // if file doesn't exist then we will initialise it
        if (!(new File(System.getProperty("user.dir") + "/database.mdb").exists())) {
            try (Connection conn = DriverManager.getConnection(dbURL)) {
                DatabaseMetaData dmd = conn.getMetaData();

                // Check if Stock table exists
                try (ResultSet rsStock = dmd.getTables(null, null, "Stock", new String[]{"TABLE"})) {
                    if (rsStock.next()) {
                        System.out.println("Table [Stock] already exists.");
                    } else {
                        System.out.println("Table [Stock] does not exist.");
                        try (Statement s = conn.createStatement()) {
                            // Create Stock table
                            s.executeUpdate("CREATE TABLE Stock (" +
                                    "StockName VARCHAR(255) PRIMARY KEY," +
                                    "Quantity DECIMAL(8,2))");
                            System.out.println("Table [Stock] created.");
                        }
                    }
                }

                // Check if Transaction table exists
                try (ResultSet rsTransaction = dmd.getTables(null, null, "Transaction", new String[]{"TABLE"})) {
                    if (rsTransaction.next()) {
                        System.out.println("Table [Transaction] already exists.");
                    } else {
                        System.out.println("Table [Transaction] does not exist.");
                        try (Statement s = conn.createStatement()) {
                            // Create Transaction table
                            s.executeUpdate("CREATE TABLE Transaction (" +
                                    "TransactionID INT PRIMARY KEY," +
                                    "StockName VARCHAR(255)," +
                                    "TransactionDate TIMESTAMP," +
                                    "Type VARCHAR(255)," +
                                    "Amount DECIMAL(8,2)," +
                                    "Quantity DECIMAL(8,2)," +
                                    "FOREIGN KEY (StockName) REFERENCES Stock(StockName))");
                            System.out.println("Table [Transaction] created.");
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
