public class Employee {
    private final String id;
    private String firstName;
    private String lastName;
    private String email;
    private int salary;
    private boolean active;


    public Employee(String id, String firstName, String lastName, String email, int salary, boolean active) {
        this.id = id;
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setSalary(salary);
        setActive(active);
    }
// ID
    public String getId() {
        return this.id;
    }
// Firstname
    private void validateFirstName(String firstName){
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("Vorname darf nicht leer sein.");
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
    private void validateLastName(String lastName){
    if (lastName == null || lastName.isBlank()) {
        throw new IllegalArgumentException("Nachname darf nicht leer sein.");
        }
    }
    public void setLastName(String lastName) {
        validateLastName(lastName);
        this.lastName = lastName.trim();
    }
    public String getLastName() {
        return this.lastName;
    }
// Email
    private void validateEmail(String email){
    if (email == null || email.isBlank() || !email.contains("@")) {
        throw new IllegalArgumentException("Email darf nicht leer sein und muss @ enthalten.");
        }
    }
    public void setEmail(String email) {
        validateEmail(email);
        this.email = email.trim();
    }
    public String getEmail() {
        return this.email;
    }
// Salary
    private void validateSalary(int salary){
    if (salary < 0) {
        throw new IllegalArgumentException("Gehalt darf nicht negativ sein");
        }
    }
    public void setSalary(int salary) {
        validateSalary(salary);
        this.salary = salary;
    }
    public int getSalary() {
        return this.salary;
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
    public String toString() { // SPÄTER UI-Ausgabe
        return "ID: " + id + ", Firstname: " + firstName + ", Lastname: " + lastName + ", E-Mail: " + email + ", Salary: " + salary + ", is active: " + active;
    }
}