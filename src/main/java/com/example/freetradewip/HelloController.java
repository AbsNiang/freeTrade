package com.example.freetradewip;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;

public class HelloController {
    public AnchorPane stocksPage;
    public AnchorPane gainLossPage;

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

    @FXML
    public void viewStocks(ActionEvent actionEvent) {
        stocksPage.setVisible(true);
        gainLossPage.setVisible(false);
    }

    @FXML
    public void viewGainLoss(ActionEvent actionEvent) {
        stocksPage.setVisible(false);
        gainLossPage.setVisible(true);
    }
}