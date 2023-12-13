module com.example.freetradewip {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires ucanaccess;
    requires com.opencsv;


    opens com.example.freetradewip to javafx.fxml;
    exports com.example.freetradewip;
    exports com.example.freetradewip.Data;
    opens com.example.freetradewip.Data to javafx.fxml;
}