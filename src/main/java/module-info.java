module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
//    requires jfxrt;
//    requires rt;

    opens Main to javafx.fxml;
    exports Main;
    exports sql;
    opens sql to javafx.fxml;
    exports controllers;
    opens controllers to javafx.fxml;
    exports Entity;
    opens Entity to javafx.fxml;

}