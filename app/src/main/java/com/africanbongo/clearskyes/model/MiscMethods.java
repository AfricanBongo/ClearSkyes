package com.africanbongo.clearskyes.model;

/**
Miscellaneous methods used through out the app
 */
public class MiscMethods {
    /**
     * Calculates if an integer value is within a range inclusively
     * @param x value
     * @param lower lower bound
     * @param upper upper bound
     * @return True, if integer is enclosed within the range
     */
    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
}
