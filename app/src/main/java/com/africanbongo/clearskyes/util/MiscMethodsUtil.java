package com.africanbongo.clearskyes.util;

/**
Miscellaneous methods used through out the app
 */
public final class MiscMethodsUtil {

    private MiscMethodsUtil() {

    }
    /**
     * Calculates if an integer value is within a range inclusively
     * @param x value
     * @param lower lower bound
     * @param upper upper bound
     * @return True, if integer is enclosed within the range
     */
    public static boolean isBetween(double x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    /**
     * Derives the corresponding String value of a UV Index
     * // Credit to https://en.wikipedia.org/wiki/Ultraviolet_index
     * @param uvIndex integer UV index
     * @return {@link String} equivalent of UV index, eg. "Moderate" for integer range from 3 to 5
     */
    public static String getUVLevel(double uvIndex) {
        if (isBetween(uvIndex, 0, 2)) return "Low";
        else if (isBetween(uvIndex, 3, 5)) return "Moderate";
        else if (isBetween(uvIndex, 6, 7)) return "High";
        else if (isBetween(uvIndex, 8, 10)) return "Very High";

        else if (uvIndex >= 11) return "Extreme";

        return "Low";
    }

    /**
     * Uses the unicode representation to retrieve the corresponding emoticon
     * @param unicode The hexadecimal representation of the emoticon
     * @return {@link String} representing the emotion
     */
    public static String getEmoticonByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}
