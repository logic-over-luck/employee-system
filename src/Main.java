import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        System.out.print("Bitte Geburtsdatum eingeben (TT.MM.JJJJ): ");
        String input = scanner.nextLine();

        try {
            LocalDate bday = LocalDate.parse(input, formatter);
            System.out.println("Erfolg! Das Datum ist: " + bday);

            Employee firstUser = new Employee("1", "first", "last", LocalDate.parse("01.01.2000", formatter), "test@gmail.com", "123456789", "Teststreet 47", "test", new BigDecimal("5000.50"), "DE123", true);

            System.out.println(firstUser.toString());

        } catch (Exception e) {
            System.out.println("Fehler: Das Format war falsch oder das Datum ungültig!");
        }
    }
}