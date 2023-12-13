package com.example.freetradewip;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private Button helloButton;

    @FXML
    protected void onHelloButtonClick() {
        Utils.updateData();
    }

//    @FXML
//    protected void onHelloButtonClick() {
//        Stage stage = (Stage) helloButton.getScene().getWindow();
//        Utils.changeWindow("second-view.fxml", stage, "Second");
//        DatabaseHandling.test();
//    }
}