module com.example.freetradewip {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.freetradewip to javafx.fxml;
    exports com.example.freetradewip;
}