module de.sebi.employeesystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql; // Wichtig für SQLite

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // 1. Erlaubt JavaFX den Zugriff auf deine Controller
    opens de.sebi.employeesystem to javafx.fxml;

    // 2. Erlaubt JavaFX den Zugriff auf deine Daten (für die Tabelle später)
    opens de.sebi.employeesystem.domain to javafx.base;

    // 3. Macht deine Ordner für andere Teile des Programms verfügbar (WICHTIG!)
    exports de.sebi.employeesystem;
    exports de.sebi.employeesystem.database;
    exports de.sebi.employeesystem.domain;
}