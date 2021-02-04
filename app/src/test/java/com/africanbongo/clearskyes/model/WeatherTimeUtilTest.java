package com.africanbongo.clearskyes.model;

import com.africanbongo.clearskyes.util.WeatherTimeUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
Test the WeatherTime class
 */
class WeatherTimeUtilTest {

    final String timeString = "2020-01-06 00:00";

    @Test
    void getRelativeDate() {
    }

    @ParameterizedTest
    @MethodSource("provideStringsForGetRelativeDay")
    void getRelativeDay(String expectedOutput, LocalDate date) {
        assertEquals(expectedOutput, WeatherTimeUtil.getRelativeDay(date));
    }

    @Test
    void getRelativeDayAndProperTime() {
        String expectedOutput = "Monday, 00:00";

        assertEquals(expectedOutput, WeatherTimeUtil.getRelativeDayAndProperTime(timeString));
    }

    @Test
    void getHourPeriod() {
        String expectedOutput = "00:00 - 01:00";

        assertEquals(expectedOutput, WeatherTimeUtil.getHourPeriod(timeString));
    }

    @ParameterizedTest
    @ValueSource(strings = {"tuesday", "mONday", "mE", "Donell"})
    void titleString(String string) {
        String titleString = WeatherTimeUtil.titleString(string);

        // Check if the first letter is capitalized
        boolean firstLetterCaps = titleString
                .substring(0, 1)
                .equals(string.substring(0, 1).toUpperCase());

        boolean checkRestIsLowerCase = titleString
                .substring(1)
                .equals(string.substring(1).toLowerCase());

        boolean titled = firstLetterCaps && checkRestIsLowerCase;

        assertTrue(titled);
    }

    @ParameterizedTest
    @ArgumentsSource(HourNotationArgumentsProvider.class)
    void get24HourNotation(String expectedOutput, String input) {
        assertEquals(expectedOutput, WeatherTimeUtil.get24HourNotation(input));
    }


    private static Stream<Arguments> provideStringsForGetRelativeDay() {
        return Stream.of(
                Arguments.of("Today", LocalDate.now()),
                Arguments.of("Yesterday", LocalDate.now().minusDays(1)),
                Arguments.of("Tomorrow", LocalDate.now().plusDays(1))
        );
    }


    // Practicing ArgumentsProvider interface
    private static class HourNotationArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of("01:00", "01:00 AM"),
                    Arguments.of("13:00", "01:00 PM"),
                    Arguments.of("16:45", "04:45 PM"),
                    Arguments.of("11:59", "11:59 AM"),
                    Arguments.of("No moonrise", "No moonrise")
            );
        }
    }
}