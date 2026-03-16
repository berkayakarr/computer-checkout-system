module org.swe212.reservationapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.swe212.reservationapp to javafx.fxml;
    exports org.swe212.reservationapp;
    exports org.swe212.reservationapp.controller;
    opens org.swe212.reservationapp.controller to javafx.fxml;
}
