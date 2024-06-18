package com.verygoodbank.tes.web.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for date-related operations.
 */
public class DateUtil {
    /**
     * A {@link DateTimeFormatter} for formatting and parsing dates in the pattern "yyyyMMdd".
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Validates whether a given string is a valid date according to the {@link #DATE_FORMATTER} pattern.
     *
     * @param dateStr the date string to validate
     * @return {@code true} if the date string is valid; {@code false} otherwise
     */
    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
