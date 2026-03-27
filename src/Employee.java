import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee {
    private static int idCounter = 1;
    private final int id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    private String email;
    private String phoneNumber;
    private Address address;

    private JobPosition position;
    private BigDecimal salary;
    private String iban;

    private boolean active;

    public Employee(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String email,
            String phoneNumber,
            Address address,
            JobPosition position,
            BigDecimal salary,
            String iban,
            boolean active
    ) {
        this.id = idCounter++;

        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setAddress(address);   // Nutzt deinen Check
        setPosition(position); // Nutzt deinen Check
        setSalary(salary);
        setIban(iban);
        setActive(active);
    }

    // ID
    public int getId() {
        return this.id;
    }

    // Firstname
    private void validateFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name must not be empty.");
        }
    }

    public void setFirstName(String firstName) {
        validateFirstName(firstName);
        this.firstName = firstName.trim();
    }

    public String getFirstName() {
        return this.firstName;
    }

    // Lastname
    private void validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name must not be empty.");
        }
    }

    public void setLastName(String lastName) {
        validateLastName(lastName);
        this.lastName = lastName.trim();
    }

    public String getLastName() {
        return this.lastName;
    }

    // Date of birth
    private void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null.");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future.");
        }
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        validateDateOfBirth(dateOfBirth);
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    // Email
    private void validateEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email must not be empty and must contain '@'.");
        }
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email.trim();
    }

    public String getEmail() {
        return this.email;
    }

    // PhoneNumber

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return; // Optional: Nichts zu tun
        }
        if (phoneNumber.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("Telefonnummer darf keine Buchstaben enthalten.");
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Address
    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    // Position
    public void setPosition(JobPosition position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null.");
        }
        this.position = position;
    }

    public JobPosition getPosition() {
        return position;
    }

    // Salary
    private void validateSalary(BigDecimal salary) {
        if (salary == null || salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Salary must not be negative.");
        }
    }

    public void setSalary(BigDecimal salary) {
        validateSalary(salary);
        this.salary = salary;
    }

    public BigDecimal getSalary() {

        return this.salary;
    }

    // IBAN
    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getIban() {
        return iban;
    }

    // Active
    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }

    //
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Mitarbeiter-Datensatz ---\n");
        sb.append("ID:          ").append(id).append("\n");
        sb.append("Name:        ").append(firstName).append(" ").append(lastName).append("\n");
        sb.append("Geburtstag:  ").append(dateOfBirth).append("\n");
        sb.append("E-Mail:      ").append(email).append("\n");
        sb.append("Telefon:     ").append(phoneNumber).append("\n");
        sb.append("Adresse:     ").append(address).append("\n");
        sb.append("-----------------------------\n");
        sb.append("Position:    ").append(position).append("\n");
        sb.append("Gehalt:      ").append(salary).append(" €\n");
        sb.append("IBAN:        ").append(iban).append("\n");
        sb.append("Status:      ").append(active ? "Aktiv" : "Inaktiv").append("\n");
        sb.append("-----------------------------");

        return sb.toString();
    }
}
