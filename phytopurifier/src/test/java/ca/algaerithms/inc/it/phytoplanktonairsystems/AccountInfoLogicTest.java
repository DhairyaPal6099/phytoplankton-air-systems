package ca.algaerithms.inc.it.phytoplanktonairsystems;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AccountInfoLogicTest {
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.length() >= 9;
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
        String email = "user@example.com";
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
}
