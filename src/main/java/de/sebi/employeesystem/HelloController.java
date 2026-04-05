package de.sebi.employeesystem;

import de.sebi.employeesystem.database.DatabaseManager;
import de.sebi.employeesystem.domain.Employee;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HelloController {

    // ── Tabelle ──────────────────────────────────────────────────
    @FXML private TableView<Employee>            employeeTable;
    @FXML private TableColumn<Employee, Integer> colId;
    @FXML private TableColumn<Employee, String>  colFirstName;
    @FXML private TableColumn<Employee, String>  colLastName;
    @FXML private TableColumn<Employee, String>  colPosition;
    @FXML private TableColumn<Employee, Boolean> colActive;

    // ── Steuerelemente ───────────────────────────────────────────
    @FXML private TextField txtSearch;
    @FXML private Label     lblStatus;
    @FXML private Label     lblPage;
    @FXML private Button    btnPrev;
    @FXML private Button    btnNext;

    // ── State ────────────────────────────────────────────────────
    private final DatabaseManager        dbManager  = new DatabaseManager();
    private final ObservableList<Employee> allLoaded = FXCollections.observableArrayList();

    private static final int PAGE_SIZE = 50;
    private int currentPage    = 0;

    // ─────────────────────────────────────────────────────────────
    // Init
    // ─────────────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        setupColumns();
        setupDoubleClick();
        dbManager.initializeDatabase();
        loadPage();
    }

    private void setupColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPosition.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getPosition().name()));
        colActive.setCellValueFactory(cell ->
                new SimpleBooleanProperty(cell.getValue().isActive()).asObject());

        // Status-Badge
        colActive.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                if (item) {
                    setText("✅ Aktiv");
                    setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                } else {
                    setText("❌ Inaktiv");
                    setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }
            }
        });
    }

    /** Doppelklick auf eine Zeile → Profilfenster öffnen */
    private void setupDoubleClick() {
        employeeTable.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    openDetailWindow(row.getItem());
                }
            });
            return row;
        });
    }

    // ─────────────────────────────────────────────────────────────
    // Daten laden & Paginierung
    // ─────────────────────────────────────────────────────────────

    void loadPage() {
        int totalEmployees = dbManager.countEmployees();
        List<Employee> page = dbManager.getEmployeesPage(PAGE_SIZE, currentPage * PAGE_SIZE);

        allLoaded.setAll(page);
        employeeTable.setItems(allLoaded);
        txtSearch.clear();

        int totalPages = Math.max(1, (int) Math.ceil((double) totalEmployees / PAGE_SIZE));
        lblPage.setText("Seite " + (currentPage + 1) + " / " + totalPages);
        btnPrev.setDisable(currentPage == 0);
        btnNext.setDisable((currentPage + 1) >= totalPages);
        lblStatus.setText("Mitarbeiter gesamt: " + totalEmployees + "  |  Diese Seite: " + page.size()
                + "   —   Doppelklick zum Öffnen eines Profils");
    }

    @FXML
    protected void onLoadDataButtonClick() {
        currentPage = 0;
        loadPage();
    }

    @FXML
    protected void onPrevPageClick() {
        if (currentPage > 0) { currentPage--; loadPage(); }
    }

    @FXML
    protected void onNextPageClick() {
        currentPage++;
        loadPage();
    }

    // ─────────────────────────────────────────────────────────────
    // Suche
    // ─────────────────────────────────────────────────────────────

    @FXML
    protected void onSearchClick() {
        String q = txtSearch.getText().trim().toLowerCase();
        if (q.isEmpty()) { employeeTable.setItems(allLoaded); return; }

        FilteredList<Employee> filtered = new FilteredList<>(allLoaded, emp ->
                emp.getFirstName().toLowerCase().contains(q)
                        || emp.getLastName().toLowerCase().contains(q)
                        || emp.getPosition().name().toLowerCase().contains(q)
                        || emp.getEmail().toLowerCase().contains(q));

        employeeTable.setItems(filtered);
        lblStatus.setText("Suchergebnisse: " + filtered.size());
    }

    // ─────────────────────────────────────────────────────────────
    // Fenster öffnen
    // ─────────────────────────────────────────────────────────────

    /** Neuen Mitarbeiter anlegen → Detailfenster ohne Employee-Objekt */
    @FXML
    protected void onNewEmployeeClick() {
        openDetailWindow(null);
    }

    /**
     * Öffnet das Profilfenster.
     * @param employee  bestehender Mitarbeiter, oder null für Neuanlage
     */
    private void openDetailWindow(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("employee-detail-view.fxml"));
            Parent root = loader.load();

            EmployeeDetailController controller = loader.getController();
            // Callback: nach Speichern/Löschen die Masterliste aktualisieren
            controller.setup(employee, dbManager, this::loadPage);

            Stage stage = new Stage();
            stage.setTitle(employee == null
                    ? "Neuer Mitarbeiter"
                    : "Profil – " + employee.getFirstName() + " " + employee.getLastName());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Profilfenster konnte nicht geöffnet werden:\n" + e.getMessage())
                    .showAndWait();
            e.printStackTrace();
        }
    }
}