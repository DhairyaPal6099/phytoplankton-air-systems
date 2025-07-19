package ca.algaerithms.inc.it.phytoplanktonairsystems.utils;

import android.text.TextUtils;
import android.util.Patterns;

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
}
