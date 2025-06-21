module com.tp.APP1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Ouvrir les packages pour la réflexion JavaFX
    opens com.tp.APP1 to javafx.fxml;
    opens com.tp.APP1.controllers to javafx.fxml;
    opens com.tp.APP1.models to javafx.fxml;

    // Exporter les packages pour l'accès externe
    exports com.tp.APP1;
    exports com.tp.APP1.controllers;
    exports com.tp.APP1.models;
    exports com.tp.APP1.dao;
    exports com.tp.APP1.utils;
}
