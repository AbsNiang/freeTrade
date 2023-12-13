package com.example.freetradewip;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Utils {

    // changes which fxml file we are viewing
    // code to use:  changeWindow(..., new (Stage) helloButton.getScene().getWindow(), ...);
    public static void changeWindow(String fileName, Stage stage, String windowName){
        try {
            FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fileName));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(windowName);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
