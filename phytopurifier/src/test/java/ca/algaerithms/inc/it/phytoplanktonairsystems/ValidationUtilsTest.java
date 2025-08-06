package ca.algaerithms.inc.it.phytoplanktonairsystems;

import org.junit.Test;
import static org.junit.Assert.*;

import ca.algaerithms.inc.it.phytoplanktonairsystems.utils.ValidationUtils;

public class ValidationUtilsTest {

    @Test
    public void testValidName() {
        assertTrue(ValidationUtils.isValidName("Alice"));
    }

    @Test
    public void testInvalidName_Empty() {
        assertFalse(ValidationUtils.isValidName(""));
    }

    @Test
    public void testEmailIsEmpty() {
        assertTrue(ValidationUtils.isEmailEmpty(""));
    }

    @Test
    public void testEmailIsNotEmpty() {
        assertFalse(ValidationUtils.isEmailEmpty("user@example.com"));
    }

    @Test
    public void testEmailFormatValid() {
        assertTrue(ValidationUtils.isEmailFormatValid("user@example.com"));
    }

    @Test
    public void testEmailFormatInvalid() {
        assertFalse(ValidationUtils.isEmailFormatValid("invalid-email"));
    }

    @Test
    public void testPhoneNumberValid() {
        assertTrue(ValidationUtils.isValidPhoneNumber("+14155552671"));
    }

    @Test
    public void testPhoneNumberInvalid() {
        assertFalse(ValidationUtils.isValidPhoneNumber("12345"));
    }

    @Test
    public void testValidPassword() {
        assertTrue(ValidationUtils.isValidPassword("Abc123!"));
    }

    @Test
    public void testConfirmPasswordMismatch() {
        assertFalse(ValidationUtils.isValidConfirmPassword("Password1", "password1"));
    }
}