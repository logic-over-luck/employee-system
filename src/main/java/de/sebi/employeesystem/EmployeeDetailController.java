package de.sebi.employeesystem;

import de.sebi.employeesystem.database.DatabaseManager;
import de.sebi.employeesystem.domain.Address;
import de.sebi.employeesystem.domain.Employee;
import de.sebi.employeesystem.domain.JobPosition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeDetailController {

    // ── Header ───────────────────────────────────────────────────
    @FXML private Label lblEmployeeName;
    @FXML private Label lblEmployeeId;

    // ── Buttons ──────────────────────────────────────────────────
    @FXML private Button btnEdit;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnDelete;

    // ── Persönliche Daten ─────────────────────────────────────────
    @FXML private TextField  txtFirstName;
    @FXML private TextField  txtLastName;
    @FXML private DatePicker dpDateOfBirth;
    @FXML private TextField  txtEmail;
    @FXML private TextField  txtPhone;

    // ── Adresse ──────────────────────────────────────────────────
    @FXML private TextField txtStreet;
    @FXML private TextField txtHouseNumber;
    @FXML private TextField txtZipCode;
    @FXML private TextField txtCity;

    // ── Beschäftigung ─────────────────────────────────────────────
    @FXML private ComboBox<JobPosition> cmbPosition;
    @FXML private DatePicker            dpHireDate;
    @FXML private TextField             txtSalary;
    @FXML private TextField             txtIban;
    @FXML private CheckBox              chkActive;

    // ── Statuszeile ──────────────────────────────────────────────
    @FXML private Label lblStatus;

    // ── State ────────────────────────────────────────────────────
    private Employee        currentEmployee; // null = Neuanlage
    private DatabaseManager dbManager;
    private Runnable        onSaveCallback;  // Masterliste aktualisieren

    // ─────────────────────────────────────────────────────────────
    // Setup (vom HelloController aufgerufen)
    // ─────────────────────────────────────────────────────────────

    /**
     * Muss direkt nach dem Laden des FXMLs aufgerufen werden.
     *
     * @param employee  zu bearbeitender MA, oder null für Neuanlage
     * @param db        DatabaseManager-Instanz
     * @param callback  wird nach Speichern/Löschen aufgerufen, um die Liste zu refreshen
     */
    public void setup(Employee employee, DatabaseManager db, Runnable callback) {
        this.currentEmployee = employee;
        this.dbManager       = db;
        this.onSaveCallback  = callback;

        cmbPosition.setItems(FXCollections.observableArrayList(JobPosition.values()));

        if (employee == null) {
            // ── Neuanlage: Formular direkt im Edit-Modus öffnen ──
            lblEmployeeName.setText("Neuer Mitarbeiter");
            lblEmployeeId.setText("");
            cmbPosition.setValue(JobPosition.DEVELOPER);
            dpHireDate.setValue(LocalDate.now());
            btnDelete.setDisable(true);
            enterEditMode();
        } else {
            // ── Bestehender MA: erst Ansichtsmodus ──
            populateFields(employee);
            enterViewMode();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Felder befüllen
    // ─────────────────────────────────────────────────────────────

    private void populateFields(Employee emp) {
        lblEmployeeName.setText(emp.getFirstName() + " " + emp.getLastName());
        lblEmployeeId.setText("ID: " + emp.getId());

        txtFirstName.setText(emp.getFirstName());
        txtLastName.setText(emp.getLastName());
        dpDateOfBirth.setValue(emp.getDateOfBirth());
        txtEmail.setText(emp.getEmail());
        txtPhone.setText(emp.getPhoneNumber() != null ? emp.getPhoneNumber() : "");

        txtStreet.setText(emp.getAddress().getStreet());
        txtHouseNumber.setText(emp.getAddress().getHouseNumber());
        txtZipCode.setText(emp.getAddress().getZipCode());
        txtCity.setText(emp.getAddress().getCity());

        cmbPosition.setValue(emp.getPosition());
        dpHireDate.setValue(emp.getHireDate());
        txtSalary.setText(emp.getSalary().toPlainString());
        txtIban.setText(emp.getIban());
        chkActive.setSelected(emp.isActive());
    }

    // ─────────────────────────────────────────────────────────────
    // Modus-Umschaltung: Ansicht ↔ Bearbeiten
    // ─────────────────────────────────────────────────────────────

    private void enterViewMode() {
        setFieldsEditable(false);
        btnEdit.setVisible(true);   btnEdit.setManaged(true);
        btnSave.setVisible(false);  btnSave.setManaged(false);
        btnCancel.setVisible(false); btnCancel.setManaged(false);
        lblStatus.setText("");
    }

    private void enterEditMode() {
        setFieldsEditable(true);
        btnEdit.setVisible(false);  btnEdit.setManaged(false);
        btnSave.setVisible(true);   btnSave.setManaged(true);
        btnCancel.setVisible(true); btnCancel.setManaged(true);
        lblStatus.setText("Änderungen werden erst nach „Speichern übernommen.");
        lblStatus.setStyle("-fx-text-fill: #e67e22; -fx-font-size: 12px;");
    }

    private void setFieldsEditable(boolean editable) {
        String viewStyle = "-fx-background-color: white; -fx-border-color: #ddd;";
        String editStyle = "-fx-background-color: white; -fx-border-color: #3498db;";
        String style = editable ? editStyle : viewStyle;

        for (TextField tf : new TextField[]{
                txtFirstName, txtLastName, txtEmail, txtPhone,
                txtStreet, txtHouseNumber, txtZipCode, txtCity,
                txtSalary, txtIban}) {
            tf.setEditable(editable);
            tf.setStyle(style);
        }

        dpDateOfBirth.setDisable(!editable);
        dpHireDate.setDisable(!editable);
        cmbPosition.setDisable(!editable);
        chkActive.setDisable(!editable);
    }

    // ─────────────────────────────────────────────────────────────
    // Button-Aktionen
    // ─────────────────────────────────────────────────────────────

    @FXML
    protected void onEditClick() {
        enterEditMode();
    }

    @FXML
    protected void onCancelClick() {
        if (currentEmployee == null) {
            // Neuanlage abbrechen → Fenster schließen
            closeWindow();
        } else {
            // Änderungen verwerfen → Felder zurücksetzen
            populateFields(currentEmployee);
            enterViewMode();
        }
    }

    @FXML
    protected void onSaveClick() {
        try {
            Employee emp = buildEmployeeFromFields();

            if (currentEmployee == null) {
                // Neuanlage
                dbManager.saveEmployee(emp);
                lblStatus.setText("✅ Mitarbeiter gespeichert.");
                lblStatus.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 12px;");
            } else {
                // Update
                boolean ok = dbManager.updateEmployee(emp);
                if (!ok) {
                    showError("Speichern fehlgeschlagen. Bitte erneut versuchen.");
                    return;
                }
                currentEmployee = emp; // lokalen State aktualisieren
                lblEmployeeName.setText(emp.getFirstName() + " " + emp.getLastName());
                lblStatus.setText("✅ Änderungen gespeichert.");
                lblStatus.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 12px;");
            }

            onSaveCallback.run(); // Masterliste refreshen
            enterViewMode();

        } catch (NumberFormatException e) {
            showError("Ungültiges Gehaltsformat (z. B. 3500.00 eingeben).");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Unbekannter Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void onDeleteClick() {
        if (currentEmployee == null) return;

        String name = currentEmployee.getFirstName() + " " + currentEmployee.getLastName();
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Mitarbeiter löschen");
        confirm.setHeaderText("„" + name + " wirklich löschen?");
        confirm.setContentText("Diese Aktion kann nicht rückgängig gemacht werden.");

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                dbManager.deleteEmployeeById(currentEmployee.getId());
                onSaveCallback.run();
                closeWindow();
            }
        });
    }

    @FXML
    protected void onCloseClick() {
        closeWindow();
    }

    // ─────────────────────────────────────────────────────────────
    // Hilfsmethoden
    // ─────────────────────────────────────────────────────────────

    private Employee buildEmployeeFromFields() {
        BigDecimal salary = new BigDecimal(
                txtSalary.getText().replace(",", ".").trim());

        Address address = new Address(
                txtStreet.getText(),
                txtHouseNumber.getText(),
                txtZipCode.getText(),
                txtCity.getText());

        String phone = txtPhone.getText().isBlank() ? null : txtPhone.getText().trim();

        if (currentEmployee != null) {
            return new Employee(
                    currentEmployee.getId(),
                    txtFirstName.getText(), txtLastName.getText(),
                    dpDateOfBirth.getValue(), dpHireDate.getValue(),
                    txtEmail.getText(), phone, address,
                    cmbPosition.getValue(), salary, txtIban.getText(),
                    chkActive.isSelected());
        } else {
            return new Employee(
                    txtFirstName.getText(), txtLastName.getText(),
                    dpDateOfBirth.getValue(), dpHireDate.getValue(),
                    txtEmail.getText(), phone, address,
                    cmbPosition.getValue(), salary, txtIban.getText(),
                    chkActive.isSelected());
        }
    }

    private void showError(String message) {
        lblStatus.setText("⚠  " + message);
        lblStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");
    }

    private void closeWindow() {
        ((Stage) btnEdit.getScene().getWindow()).close();
    }
}
