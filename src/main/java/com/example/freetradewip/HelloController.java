package com.example.freetradewip;

import com.example.freetradewip.Data.CSVHandling;
import com.example.freetradewip.Data.DatabaseHandling;
import com.example.freetradewip.Data.GainLoss;
import com.example.freetradewip.Data.Objects.Stock;
import com.example.freetradewip.Data.Objects.Transaction;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class HelloController {

    @FXML
    public Label div_total;
    @FXML
    public Label gainTotal;
    @FXML
    public ComboBox<String> fiscalYearSelector;
    @FXML
    public Button update_tables;
    @FXML
    private AnchorPane stocksPage;
    @FXML
    private AnchorPane gainLossPage;
    @FXML
    private AnchorPane dividendsPage;

    @FXML
    private TableView<Transaction> gainsTable;
    @FXML
    public TableColumn<Transaction, String> gainsTable_gain;
    @FXML
    private TableColumn<Transaction, String> gainsTable_name;
    @FXML
    private TableColumn<Transaction, String> gainsTable_date;

    @FXML
    public TableView<Transaction> dividendsTable;
    @FXML
    private TableColumn<Transaction, String> dividendsTable_dividends;
    @FXML
    private TableColumn<Transaction, String> dividendsTable_name;
    @FXML
    private TableColumn<Transaction, String> dividendsTable_date;

    @FXML
    private TableView<Stock> stockTable;
    @FXML
    private TableColumn<Stock, String> stockTable_shares;
    @FXML
    private TableColumn<Stock, String> stockTable_name;


    @FXML
    private void handleOpenFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        // Show open dialog
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            CSVHandling.saveActivitiesFromCSVToDB(DatabaseHandling.getWhenLastUpdated(), selectedFile.getAbsolutePath());
            initialize();
        }
    }

    private static ObservableList<Transaction> dividendsTableList = null;
    private static ObservableList<Transaction> gainLossTableList = null;

    private static final String totalText = "Total: £";

    @FXML
    private void initialize() {
        setupDividendsTable();
        setupStocksTable();
        setupGainLossTable();

        //TODO: will need to add more in the future or generate them
        fiscalYearSelector.getItems().addAll("All", "2020/2021", "2021/2022", "2022/2023", "2023/2024", "2024/2025", "2025/2026", "2026/2027");

        // Listener for selection changes
        fiscalYearSelector.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTableViews(newValue)
        );
    }

    private void filterTableViews(String fiscalYear) {
        // Assuming transactionsObservableList is the ObservableList backing your TableView

        // Filter for Gain/Loss Table
        FilteredList<Transaction> filteredGainsList = new FilteredList<>(gainLossTableList);
        Predicate<Transaction> fiscalYearFilter = createFiscalYearFilter(fiscalYear);
        filteredGainsList.setPredicate(fiscalYearFilter);
        gainsTable.setItems(filteredGainsList);
        replaceTotalText(gainTotal, filteredGainsList);

        // Filter for Dividends Table
        FilteredList<Transaction> filteredDividendsList = new FilteredList<>(dividendsTableList);
        Predicate<Transaction> dividendFiscalYearFilter = createFiscalYearFilter(fiscalYear);
        filteredDividendsList.setPredicate(dividendFiscalYearFilter);
        dividendsTable.setItems(filteredDividendsList);
        replaceTotalText(div_total,filteredDividendsList);
    }

    private Predicate<Transaction> createFiscalYearFilter(String fiscalYear) {
        if ("All".equals(fiscalYear)){
            return transaction -> true;
        }

        int startYear = Integer.parseInt(fiscalYear.split("/")[0]);
        int endYear = Integer.parseInt(fiscalYear.split("/")[1]);

        return transaction -> {
            LocalDateTime transactionDate = transaction.getTransactionDate();
            // 6th april to the 5th april (exclusive so I did + 1 for the values)
            return (transactionDate.isAfter(LocalDateTime.of(startYear, 4, 5, 0, 0))
                    && transactionDate.isBefore(LocalDateTime.of(endYear, 4, 6, 0, 0)));
        };
    }

    @FXML
    private void viewStocks() {
        stocksPage.setVisible(true);
        gainLossPage.setVisible(false);
        dividendsPage.setVisible(false);
    }

    @FXML
    private void viewGainLoss() {
        stocksPage.setVisible(false);
        gainLossPage.setVisible(true);
        dividendsPage.setVisible(false);
    }

    @FXML
    private void viewDividends() {
        stocksPage.setVisible(false);
        gainLossPage.setVisible(false);
        dividendsPage.setVisible(true);
    }

    private void setupStocksTable() {
        ObservableList<Stock> stocksTableList = DatabaseHandling.getCurrentStocks();
        stockTable_name.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        stockTable_shares.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        stockTable.setItems(stocksTableList);
    }

    private void setupDividendsTable() {
        dividendsTableList = DatabaseHandling.getDividends();
        dividendsTable_name.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        dividendsTable_date.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        dividendsTable_dividends.setCellValueFactory(new PropertyValueFactory<>("moneyAmount"));
        dividendsTable.setItems(dividendsTableList);
        replaceTotalText(div_total, dividendsTableList);
    }

    private void setupGainLossTable() {
        gainLossTableList = GainLoss.getGainLoss();
        gainsTable_name.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        gainsTable_date.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        gainsTable_gain.setCellValueFactory(new PropertyValueFactory<>("moneyAmount"));
        gainsTable.setItems(gainLossTableList);
        replaceTotalText(gainTotal, gainLossTableList);
    }

    private void replaceTotalText(Label label, ObservableList<Transaction> list){
        label.setText(totalText + calcTotal(list));
    }

    private double calcTotal(ObservableList<Transaction> transactions) {
        double count = 0;
        for (Transaction transaction : transactions) {
            count += transaction.getMoneyAmount();
        }

        return Utils.formatDouble(count);
    }

}