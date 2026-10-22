package de.sebi.employeesystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EmployeeApp.class.getResource("employee-list-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("employee-system by logic-over-luck");
        stage.setScene(scene);
        stage.show();
    }
}
