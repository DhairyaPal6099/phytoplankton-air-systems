package ca.algaerithms.inc.it.phytoplanktonairsystems.utils;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class ValidationUtils {

    public static boolean isValidName(String name) {
        return !TextUtils.isEmpty(name);
    }

    public static boolean isEmailEmpty(String email) {
        return TextUtils.isEmpty(email);
    }

    public static boolean isEmailFormatValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String number) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedNumber = phoneUtil.parse(number, null);
            return phoneUtil.isValidNumber(parsedNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d\\W]).{6,}$");
    }

    public static boolean isValidConfirmPassword(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }


}
