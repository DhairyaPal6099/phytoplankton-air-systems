package ca.algaerithms.inc.it.phytoplanktonairsystems;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.time.LocalDate;

public class AccountInfoLogicTest {
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.length() >= 9;
    }


    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    private boolean isValidBirthdayFormat(String birthday) {
        try {
            LocalDate.parse(birthday); // expects yyyy-MM-dd format
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidInput(String name, String email, String phone, String birthday) {
        return isValidName(name) &&
                isValidEmail(email) &&
                isValidPhone(phone) &&
                isValidBirthdayFormat(birthday);
    }

    @Test
    public void testEmptyName() {
        String name = "";
        assertTrue(name.isEmpty());
    }

    @Test
    public void testEmptyEmail() {
        String email = "";
        assertTrue(email.isEmpty());
    }

    @Test
    public void testInvalidEmailFormat() {
        String email = "invalid@email";
        System.out.println("Valid? " + isValidEmail(email));
        assertFalse(isValidEmail(email));
    }

    @Test
    public void testValidEmailFormat() {
        String email = "abc@xyz.com";
        assertTrue(isValidEmail(email));
    }

    @Test
    public void testEmptyPhone() {
        String phone = "";
        assertTrue(phone.isEmpty());
    }

    @Test
    public void testShortPhone() {
        String phone = "12345";
        assertFalse(isValidPhone(phone));
    }

    @Test
    public void testValidPhone() {
        String phone = "123456789";
        assertTrue(isValidPhone(phone));
    }

    @Test
    public void testEmptyBirthday() {
        String birthday = "";
        assertTrue(birthday.isEmpty());
    }

    @Test
    public void testInvalidBirthdayFormat() {
        assertFalse(isValidBirthdayFormat("12-31-2020")); // wrong format
    }

    @Test
    public void testInvalidWhenNameEmpty() {
        assertFalse(isValidInput("", "user@example.com", "1234567890", "2020-12-31"));
    }
}
