package com.example.freetradewip;

import com.example.freetradewip.Data.DatabaseHandling;
import com.example.freetradewip.Data.Objects.Stock;
import com.example.freetradewip.Data.Objects.Transaction;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class HelloController {
    @FXML
    private AnchorPane stocksPage;
    @FXML
    private AnchorPane gainLossPage;
    @FXML
    private AnchorPane dividendsPage;

    @FXML
    private TableView gainsTable;
    @FXML
    public TableColumn gainsTable_gain;
    @FXML
    private TableColumn gainsTable_name;

    @FXML
    public TableView<Transaction> dividendsTable;
    @FXML
    private TableColumn<Transaction, String> dividendsTable_dividends;
    @FXML
    private TableColumn<Transaction, String> dividendsTable_name;

    @FXML
    private TableView<Stock> stockTable;
    @FXML
    private TableColumn<Stock, String> stockTable_shares;
    @FXML
    private TableColumn<Stock, String> stockTable_name;


//    @FXML
//    private TableView<?> tableView;
//
//    @FXML
//    private void handleOpenFile(ActionEvent event) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Resource File");
//
//        // Show open dialog
//        File selectedFile = fileChooser.showOpenDialog(null);
//        if (selectedFile != null) {
//            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
//            // Perform further processing with the selected file, if needed
//        }
//    }

    private static ObservableList<Stock> stocksTableList = null;
    private static ObservableList<Stock> dividendsTableList = null;

    @FXML
    private void viewStocks(ActionEvent actionEvent) {
        stocksPage.setVisible(true);
        gainLossPage.setVisible(false);
        dividendsPage.setVisible(false);
        if (stocksTableList == null){
            setupStocksTable();
        }
    }

    @FXML
    private void viewGainLoss(ActionEvent actionEvent) {
        stocksPage.setVisible(false);
        gainLossPage.setVisible(true);
        dividendsPage.setVisible(false);
    }

    @FXML
    private void viewDividends(ActionEvent actionEvent) {
        stocksPage.setVisible(false);
        gainLossPage.setVisible(false);
        dividendsPage.setVisible(true);
        if (dividendsTableList == null){
            setupDividendsTable();
        }
    }

    private void setupStocksTable(){
        stocksTableList = DatabaseHandling.getCurrentStocks();
        stockTable_name.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        stockTable_shares.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        stockTable.setItems(stocksTableList);
    }

    private void setupDividendsTable(){
        dividendsTableList = DatabaseHandling.getDividends();
        dividendsTable_name.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        dividendsTable_dividends.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        stockTable.setItems(stocksTableList);
    }
}