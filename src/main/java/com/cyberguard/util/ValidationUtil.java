package com.cyberguard.util;

import com.cyberguard.exception.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/** Server-side input validation (never trust the browser). */
public final class ValidationUtil {
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^[0-9+\\-\\s]{7,15}$");
    private static final BigDecimal MAX_MONEY = new BigDecimal("9999999999.99");

    private ValidationUtil() { }

    public static String requireText(String v, String field, int min, int max) throws ValidationException {
        String t = v == null ? "" : v.trim();
        if (t.isEmpty()) throw new ValidationException(field + " is required.");
        if (t.length() < min || t.length() > max)
            throw new ValidationException(field + " must be between " + min + " and " + max + " characters.");
        return t;
    }

    /** Overload: only checks that the field is present (max 255). */
    public static String requireText(String v, String field) throws ValidationException {
        return requireText(v, field, 1, 255);
    }

    public static String optionalText(String v, String field, int max) throws ValidationException {
        String t = v == null ? "" : v.trim();
        if (t.length() > max) throw new ValidationException(field + " must be at most " + max + " characters.");
        return t.isEmpty() ? null : t;
    }

    public static String email(String v) throws ValidationException {
        String t = requireText(v, "Email", 5, 150).toLowerCase();
        if (!EMAIL.matcher(t).matches()) throw new ValidationException("Please enter a valid email address.");
        return t;
    }

    public static String phone(String v) throws ValidationException {
        String t = requireText(v, "Phone number");
        if (!PHONE.matcher(t).matches()) throw new ValidationException("Please enter a valid phone number (7-15 digits).");
        return t;
    }

    public static String password(String v) throws ValidationException {
        if (v == null || v.length() < 8 || v.length() > 72 || !v.matches(".*[A-Za-z].*") || !v.matches(".*[0-9].*"))
            throw new ValidationException("Password must be 8-72 characters and contain at least one letter and one number.");
        return v;
    }

    public static String optionalUrl(String v) throws ValidationException {
        String t = optionalText(v, "Suspicious URL", 500);
        if (t == null) return null;
        try {
            URI u = new URI(t);
            String s = u.getScheme();
            if (u.getHost() == null || s == null || !(s.equalsIgnoreCase("http") || s.equalsIgnoreCase("https")))
                throw new ValidationException("Suspicious URL must start with http:// or https://");
        } catch (java.net.URISyntaxException e) {
            throw new ValidationException("Suspicious URL is not a valid web address.");
        }
        return t;
    }

    public static Date incidentDate(String v) throws ValidationException {
        String t = requireText(v, "Date of incident");
        try {
            LocalDate d = LocalDate.parse(t);
            if (d.isAfter(LocalDate.now())) throw new ValidationException("Date of incident cannot be in the future.");
            if (d.isBefore(LocalDate.now().minusYears(10))) throw new ValidationException("Date of incident is too far in the past.");
            return Date.valueOf(d);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Please enter a valid date of incident.");
        }
    }

    public static BigDecimal money(String v) throws ValidationException {
        if (v == null || v.trim().isEmpty()) return BigDecimal.ZERO.setScale(2);
        try {
            BigDecimal m = new BigDecimal(v.trim()).setScale(2, RoundingMode.HALF_UP);
            if (m.signum() < 0 || m.compareTo(MAX_MONEY) > 0) throw new ValidationException("Financial loss must be a positive amount.");
            return m;
        } catch (NumberFormatException e) {
            throw new ValidationException("Financial loss must be a number.");
        }
    }
}
