package com.africanbongo.clearskyes.model.weather;

import java.util.LinkedHashMap;

/**
 * Implemented by weather classes that can represent their members
 * as key-value pairs
 */
public interface WeatherMappable {
    /**
     * Returns the members of a class as ordered key-value pairs
     * @param degree {@link com.africanbongo.clearskyes.model.weather.WeatherTemp.Degree}
     *                symbol to use for temperature measurements
     * @param measurement {@link com.africanbongo.clearskyes.model.weather.WeatherMisc.Measurement}
     *                    to be metric or imperial measurement that is used
     * @return {@link LinkedHashMap}<{@link String}, {@link String}> of key-value pairs of class members
     */
    LinkedHashMap<String, String> getMapping(WeatherTemp.Degree degree, WeatherMisc.Measurement measurement);

    /**
     * How many grouped categories are within the {@link LinkedHashMap}
     * returned from {@link WeatherMappable#getMapping(WeatherTemp.Degree, WeatherMisc.Measurement)}
     * @return
     */
    int categorySize();
}
