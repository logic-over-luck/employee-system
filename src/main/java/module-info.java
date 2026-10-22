module de.sebi.employeesystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens de.sebi.employeesystem to javafx.fxml;
    opens de.sebi.employeesystem.domain to javafx.base;

    exports de.sebi.employeesystem;
    exports de.sebi.employeesystem.database;
    exports de.sebi.employeesystem.domain;
}