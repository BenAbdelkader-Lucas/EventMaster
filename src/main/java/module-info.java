module com.example.megasae {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.logging;
    requires java.naming;
    requires javafx.graphics;
    requires java.desktop;

    opens com.example.megasae to javafx.fxml;
    exports com.example.megasae;
    opens com.example.megasae.controller.Pages to javafx.fxml;
    opens com.example.megasae.controller.Popups to javafx.fxml;

    opens com.example.megasae.model.Finance to javafx.base;
    opens com.example.megasae.model.Miscellaneous to javafx.base;
    opens com.example.megasae.controller.Popups.Ajout to javafx.fxml;
    opens com.example.megasae.controller.Popups.Attribution to javafx.fxml;
    opens com.example.megasae.controller.Popups.Choix to javafx.fxml;
}