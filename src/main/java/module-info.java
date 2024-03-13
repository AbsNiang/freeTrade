module com.example.freetradewip {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires ucanaccess;
    requires com.opencsv;
    requires com.healthmarketscience.jackcess;

    opens com.example.freetradewip to javafx.fxml;
    opens com.example.freetradewip.Data to javafx.fxml; // Open Data package
    opens com.example.freetradewip.Data.Objects to javafx.fxml; // Open Objects package
    exports com.example.freetradewip;
    exports com.example.freetradewip.Data;
    exports com.example.freetradewip.Data.Objects;
}