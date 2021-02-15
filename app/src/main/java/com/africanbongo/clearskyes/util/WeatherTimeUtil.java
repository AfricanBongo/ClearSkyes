package com.africanbongo.clearskyes.util;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Class that holds all methods and formats to derive Date and Time strings
 */
public final class WeatherTimeUtil {
    // Formats for date and time
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String LONG_DATE_FORMAT = " dd MMMM yyyy";
    public static final String DATE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

    // Intent variables
    public static final String INTENT_EXTRA = "weather_time";
    public static final String REFRESH_EXTRA = "refresh_cache_time";

    private WeatherTimeUtil() {

    }
    /**
     * Derives the date from a String
     * @param dateString Date as a String, in the format of "yyyy-MM-dd"
     * @return {@link String} as formatted date string, eg. "Today, 02 January 2018"
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getRelativeDate(String dateString) {
        // Parse the string given into a LocalDate object
        LocalDate newDate = LocalDate.parse(dateString);

        StringBuilder date = new StringBuilder();

        // Build of a string of the date
        LocalDate now = LocalDate.now();

        // Relative day
        date.append(getRelativeDay(newDate));
        date.append(",");
        // Day of the month and year, eg. 02 January 2018
        date.append(" " + newDate.format(DateTimeFormatter.ofPattern(LONG_DATE_FORMAT)));

        return date.toString();
    }

    /**
     * Get day relative to today, i.e. LocalDate.now()
     * @param date {@link LocalDate} used for comparison
     * @return {@link String} of relative day to now, eg. "Today"
     */
    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getRelativeDay(LocalDate date) {
        int intNow = LocalDate.now().getDayOfYear();
        int intDate = date.getDayOfYear();

        if (intNow == intDate) {
            return "Today";
        } else if (intNow == date.getDayOfYear() - 1) {
            return "Tomorrow";
        }

        // Even if the date's day is the next day of today but of the next year
        // it is run this condition
        return titleString(date.getDayOfWeek().toString());
    }

    /**
     * Derive the date and time from a String
     * @param dateTimeString Date and time a String, in the format of "yyyy-MM-dd hh:mm"
     * @return {@link String} as formatted date and time string, eg. "Today, 19:18"
     */
    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getRelativeDayAndProperTime(String dateTimeString) {
        // Format used to derive the date and time
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        // Parse the string given into a LocalDateTime object
        LocalDateTime newDateTime = LocalDateTime.parse(dateTimeString, dateTimeFormatter);

        StringBuilder dateTime = new StringBuilder();

        // Build of a string of the date
        dateTime.append(getRelativeDay(newDateTime.toLocalDate()));

        // Hour of day
        dateTime.append(", ");
        dateTime.append(newDateTime
                .toLocalTime()
                .format(DateTimeFormatter.ofPattern(TIME_FORMAT)));

        return dateTime.toString();
    }

    /**
     * Get hour interval, eg. "01:00 - 02:00"
     * @param dateTimeString Date and time a String, in the format of "yyyy-MM-dd hh:mm"
     * @return {@link String} as an hour interval with the hour ahead, eg. "01:00 - 02:00"
     */
    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getHourPeriod(String dateTimeString) {
        // Get hour and minutes as first element of parsed array
        return getRelativeDayAndProperTime(dateTimeString).split(", ")[1];
    }

    /**
     * Returns a string with the first letter capitalized
     * @param string
     * @return First letter capitalized {@link String}
     */
    public static String titleString(String string) {
        return string.substring(0, 1).toUpperCase()
                .concat(string.substring(1).toLowerCase());
    }

    /**
     * Derives the 24 hour notation String from a normal AM/PM String, i.e. "12.15 AM"
     * @param timeString AM/PM String
     * @return A 24 hour notation String {@link String}
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String get24HourNotation(String timeString) {

        try {
            Date time = new SimpleDateFormat("hh:mm a").parse(timeString);
            return new SimpleDateFormat(TIME_FORMAT)
                    .format(time);
        } catch (DateTimeParseException | ParseException e) {
            return timeString;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getCurrentHourAsIndex() {
        return LocalTime.now().getHour();
    }
}
