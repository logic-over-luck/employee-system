package database;

import domain.Address;
import domain.Employee;
import domain.JobPosition;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:employees.db";

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS employees (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT,
                last_name TEXT,
                date_of_birth TEXT,
                hire_date TEXT,
                email TEXT,
                phone_number TEXT,
                salary NUMERIC,
                iban TEXT,
                active INTEGER,
                job_position TEXT,
                address_street TEXT,
                address_house_number TEXT,
                address_zip_code TEXT,
                address_city TEXT
            );
            """;

    private static final String INSERT_EMPLOYEE_SQL = """
            INSERT INTO employees (
                first_name,
                last_name,
                date_of_birth,
                hire_date,
                email,
                phone_number,
                salary,
                iban,
                active,
                job_position,
                address_street,
                address_house_number,
                address_zip_code,
                address_city
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_EMPLOYEE_BY_ID_SQL = """
            SELECT *
            FROM employees
            WHERE id = ?
            """;

    private static final String SELECT_EMPLOYEES_PAGE_SQL = """
            SELECT *
            FROM employees
            ORDER BY last_name ASC, first_name ASC
            LIMIT ? OFFSET ?
            """;

    private static final String COUNT_EMPLOYEES_SQL = """
            SELECT COUNT(*) AS total
            FROM employees
            """;

    private static final String UPDATE_EMPLOYEE_SQL = """
            UPDATE employees
            SET first_name = ?,
                last_name = ?,
                date_of_birth = ?,
                hire_date = ?,
                email = ?,
                phone_number = ?,
                salary = ?,
                iban = ?,
                active = ?,
                job_position = ?,
                address_street = ?,
                address_house_number = ?,
                address_zip_code = ?,
                address_city = ?
            WHERE id = ?
            """;

    private static final String DELETE_EMPLOYEE_BY_ID_SQL = """
            DELETE FROM employees
            WHERE id = ?
            """;

    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(CREATE_TABLE_SQL);
            System.out.println("Database and employees table initialized successfully.");

        } catch (SQLException e) {
            System.out.println("Error while initializing the database: " + e.getMessage());
        }
    }

    public void saveEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee must not be null.");
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(INSERT_EMPLOYEE_SQL)) {

            fillEmployeeStatement(pstmt, employee, false);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Employee saved successfully.");
            } else {
                System.out.println("Employee could not be saved.");
            }

        } catch (SQLException e) {
            System.out.println("Error while saving employee: " + e.getMessage());
        }
    }

    public Employee getEmployeeById(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(SELECT_EMPLOYEE_BY_ID_SQL)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToEmployee(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            System.out.println("Error while loading employee by id: " + e.getMessage());
            return null;
        }
    }

    public List<Employee> getEmployeesPage(int limit, int offset) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0.");
        }

        if (offset < 0) {
            throw new IllegalArgumentException("Offset must not be negative.");
        }

        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(SELECT_EMPLOYEES_PAGE_SQL)) {

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapRowToEmployee(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while loading employee page: " + e.getMessage());
        }

        return employees;
    }

    public int countEmployees() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(COUNT_EMPLOYEES_SQL);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

            return 0;

        } catch (SQLException e) {
            System.out.println("Error while counting employees: " + e.getMessage());
            return 0;
        }
    }

    public boolean updateEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee must not be null.");
        }

        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee id must not be null for update.");
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_EMPLOYEE_SQL)) {

            fillEmployeeStatement(pstmt, employee, true);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Employee updated successfully.");
                return true;
            } else {
                System.out.println("No employee was updated.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error while updating employee: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployeeById(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(DELETE_EMPLOYEE_BY_ID_SQL)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Employee deleted successfully.");
                return true;
            } else {
                System.out.println("No employee was deleted.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error while deleting employee: " + e.getMessage());
            return false;
        }
    }

    private void fillEmployeeStatement(PreparedStatement pstmt, Employee employee, boolean includeIdAtEnd) throws SQLException {
        pstmt.setString(1, employee.getFirstName());
        pstmt.setString(2, employee.getLastName());
        pstmt.setString(3, employee.getDateOfBirth().toString());
        pstmt.setString(4, employee.getHireDate().toString());
        pstmt.setString(5, employee.getEmail());
        pstmt.setString(6, employee.getPhoneNumber());
        pstmt.setBigDecimal(7, employee.getSalary());
        pstmt.setString(8, employee.getIban());
        pstmt.setInt(9, employee.isActive() ? 1 : 0);
        pstmt.setString(10, employee.getPosition().name());
        pstmt.setString(11, employee.getAddress().getStreet());
        pstmt.setString(12, employee.getAddress().getHouseNumber());
        pstmt.setString(13, employee.getAddress().getZipCode());
        pstmt.setString(14, employee.getAddress().getCity());

        if (includeIdAtEnd) {
            pstmt.setInt(15, employee.getId());
        }
    }

    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        int employeeId = rs.getInt("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        LocalDate dateOfBirth = LocalDate.parse(rs.getString("date_of_birth"));
        LocalDate hireDate = LocalDate.parse(rs.getString("hire_date"));
        String email = rs.getString("email");
        String phoneNumber = rs.getString("phone_number");

        Address address = new Address(
                rs.getString("address_street"),
                rs.getString("address_house_number"),
                rs.getString("address_zip_code"),
                rs.getString("address_city")
        );

        BigDecimal salary = rs.getBigDecimal("salary");
        JobPosition position = JobPosition.valueOf(rs.getString("job_position"));
        String iban = rs.getString("iban");
        boolean active = rs.getInt("active") == 1;

        return new Employee(
                employeeId,
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
}