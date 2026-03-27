public class Address {
    private String street;
    private String houseNumber;
    private String zipCode;
    private String city;

    public Address(String street, String houseNumber, String zipCode, String city) {
        setStreet(street);
        setHouseNumber(houseNumber);
        setZipCode(zipCode);
        setCity(city);
    }

    // Street
    private void validateStreet(String street) {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street must not be empty.");
        }
    }

    public void setStreet(String street) {
        validateStreet(street);
        this.street = street.trim();
    }

    public String getStreet() {
        return street;
    }

    // House number
    private void validateHouseNumber(String houseNumber) {
        if (houseNumber == null || houseNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("House number must not be empty.");
        }
    }

    public void setHouseNumber(String houseNumber) {
        validateHouseNumber(houseNumber);
        this.houseNumber = houseNumber.trim();
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    // city
    private void validateCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City must not be empty.");
        }
    }

    public void setCity(String city) {
        validateCity(city);
        this.city = city.trim();
    }

    public String getCity() {
        return city;
    }

    // ZIP Code Validation
    private void validateZipCode (String zipCode){
        if (zipCode == null || zipCode.length() != 5 || !zipCode.matches("[0-9]+")) {
            throw new IllegalArgumentException("ZIP code must be exactly 5 digits.");
        }
    }

    public void setZipCode (String zipCode){
        validateZipCode(zipCode);
        this.zipCode = zipCode;
    }

    public String getZipCode () {
        return zipCode;
    }

    @Override
    public String toString() {
        return street + " " + houseNumber + ", " + zipCode + " " + city;
    }

}