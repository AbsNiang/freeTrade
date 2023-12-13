package com.example.freetradewip;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private Button helloButton;
    @FXML
    protected void onHelloButtonClick() {
        Stage stage = (Stage) helloButton.getScene().getWindow();
        Utils.changeWindow("second-view.fxml", stage, "Second");
    }
}