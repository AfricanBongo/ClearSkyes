package com.africanbongo.clearskyes.model.weatherobjects;

import static com.africanbongo.clearskyes.model.MiscMethods.isBetween;

/**
Contains other miscellaneous weather properties
 */
public class WeatherMisc {
    private final float pressureMB;
    private final float precipMM;
    private final float humidity;
    private final String uvIndex;

    public WeatherMisc(float pressureMB, float precipMM, float humidity, int uvIndex) {
        this.pressureMB = pressureMB;
        this.precipMM = precipMM;
        this.humidity = humidity;
        this.uvIndex = getUVLevel(uvIndex);
    }

    public float getPressureMB() {
        return pressureMB;
    }

    public float getPrecipMM() {
        return precipMM;
    }

    public float getHumidity() {
        return humidity;
    }

    public String getUvIndex() {
        return uvIndex;
    }

    /**
     * Derives the corresponding String value of a UV Index
     * // Credit to https://en.wikipedia.org/wiki/Ultraviolet_index
     * @param uvIndex integer UV index
     * @return {@link String} equivalent of UV index, eg. "Moderate" for integer range from 3 to 5
     */
    public static String getUVLevel(int uvIndex) {
        if (isBetween(uvIndex, 0, 2)) return "Low";
        else if (isBetween(uvIndex, 3, 5)) return "Moderate";
        else if (isBetween(uvIndex, 6, 7)) return "High";
        else if (isBetween(uvIndex, 8, 10)) return "Very High";

        else if (uvIndex >= 11) return "Extreme";

        return "Low";
    }
}
