package com.example.freetradewip;

import com.example.freetradewip.Data.CSVHandling;
import com.example.freetradewip.Data.DatabaseHandling;
import com.example.freetradewip.Data.Objects.Activity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class Utils {

    // changes which fxml file we are viewing
    // code to use:  changeWindow(..., new (Stage) helloButton.getScene().getWindow(), ...);
    public static void changeWindow(String fileName, Stage stage, String windowName) {
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

    // updates the data in the DB (records any new activities)
    public static void updateData() {
        //? need to select all the data from the last date in the DB (so we don't have to check against old transactions)
        //? with this data we store the 'new' activities in DB, and remove any stocks that have been sold

        // date of when we last updated the DB with new activity
        LocalDateTime lastUpdatedDateTime = DatabaseHandling.getWhenLastUpdated();
        List<Activity> activities = CSVHandling.getActivityFromCSV(lastUpdatedDateTime); // recent list of activities


    }
}
