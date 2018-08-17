package util;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    /**
     * Determines if email is valid email.
     * @param email email to be validated
     * @return true if valid email, otherwise false
     */
    public static boolean isVaild(final String email) {
        return Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE).matcher(email).find();
    }
}
