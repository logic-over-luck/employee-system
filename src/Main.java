import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Employee> staff = new ArrayList<>();


        Address addr1 = new Address("Main St", "10", "12345", "Berlin");
        staff.add(new Employee( "Max", "Mustermann", LocalDate.of(1990,1,1), "max@test.de", "123", addr1, JobPosition.DEVELOPER, new BigDecimal("0.1"), "DE123", true));

        staff.add(new Employee( "Tom", "Dean", LocalDate.of(1990,2,1), "tom@test.de", "1234", addr1, JobPosition.DEVELOPER, new BigDecimal("0.2"), "DE1234", true));
        staff.add(new Employee( "Samy", "Geyer", LocalDate.of(1998,8,25), "samy@test.de", "12324", addr1, JobPosition.INTERN, new BigDecimal("0"), "DE1234", true));

        BigDecimal summe = BigDecimal.ZERO;

        for (Employee e : staff) {
            summe = summe.add(e.getSalary());
        }
        System.out.println("Summe aller Gehälter: " + summe);
    }
}