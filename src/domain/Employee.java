package domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate hireDate;

    private String email;
    private String phoneNumber;
    private Address address;

    private JobPosition position;
    private BigDecimal salary;
    private String iban;

    private boolean active;

    public Employee(
            Integer id,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            LocalDate hireDate,
            String email,
            String phoneNumber,
            Address address,
            JobPosition position,
            BigDecimal salary,
            String iban,
            boolean active
    ) {
        this.id = id;
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
        setHireDate(hireDate);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setAddress(address);
        setPosition(position);
        setSalary(salary);
        setIban(iban);
        setActive(active);
    }

    public Employee(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            LocalDate hireDate,
            String email,
            String phoneNumber,
            Address address,
            JobPosition position,
            BigDecimal salary,
            String iban,
            boolean active
    ) {
        this(
                null,
                firstName,
                lastName,
                dateOfBirth,
                hireDate,
                email,
                phoneNumber,
                address,
                position,
                salary,
                iban,
                active
        );
    }


    // ID
    public Integer getId() {
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

    //hireDate
    private void validateHireDate(LocalDate hireDate) {
        if (hireDate == null) {
            throw new IllegalArgumentException("Hire date cannot be null.");
        }
    }

    public void setHireDate(LocalDate hireDate) {
        validateHireDate(hireDate);
        this.hireDate = hireDate;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    // Email
    private void validateEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email must not be empty and must contain '@'.");
        }
        if (email.indexOf("@") == 0 || email.indexOf("@") == email.length() - 1) {
            throw new IllegalArgumentException("Email must have characters before and after '@'.");
        }

        if (email.indexOf("@") != email.lastIndexOf("@")) {
            throw new IllegalArgumentException("Email must contain only one '@' character.");
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
        if (phoneNumber == null) {
            return;
        }

        if (phoneNumber.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("Phone number must not contain letters.");
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            this.phoneNumber = null;
            return;
        }

        String trimmedPhoneNumber = phoneNumber.trim();
        validatePhoneNumber(trimmedPhoneNumber);
        this.phoneNumber = trimmedPhoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Address
    private void validateAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null.");
        }
    }

    public void setAddress(Address address) {
        validateAddress(address);
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
    private void validateIban(String iban) {
        if (iban == null || iban.isBlank()) {
            throw new IllegalArgumentException("IBAN must not be empty.");
        }

        String trimmedIban = iban.trim();

        if (trimmedIban.length() < 4 || trimmedIban.length() > 34) {
            throw new IllegalArgumentException("IBAN must be between 4 and 34 characters long.");
        }

        if (!Character.isLetter(trimmedIban.charAt(0)) || !Character.isLetter(trimmedIban.charAt(1))) {
            throw new IllegalArgumentException("IBAN must start with two letters.");
        }

        if (!trimmedIban.matches("[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("IBAN must contain only letters and digits.");
        }
    }

    public void setIban(String iban) {
        validateIban(iban);
        this.iban = iban.trim();
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
        sb.append("Einstellungsdatum :     ").append(hireDate).append("\n");
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
