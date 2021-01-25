package com.africanbongo.clearskyes.model.weatherapi.util;

import androidx.annotation.NonNull;

import com.africanbongo.clearskyes.model.weather.WeatherLocation;
import com.africanbongo.clearskyes.model.weatherapi.WeatherRequestQueue;
import com.neovisionaries.i18n.CountryCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Utility class used to perform operations involving {@link WeatherLocation}
 * <p>
 *     Can be used for :
 *     <ul>
 *         <li>Serializing and de-serializing {@link WeatherLocation} objects</li>
 *         <li>Parsing a Location {@link org.json.JSONObject} from the API into a local {@link WeatherLocation}</li>
 *    </ul>
 * </p>
 */
public final class LocationUtil {

    public static final String NOT_APPLICABLE = "N/A";
    // Used to separate info in a WeatherInfo String
    public static final String STRING_SEPARATOR = "\t";
    // Used to identify a set contains WeatherLocation data
    public static final String WEATHER_LOCATION_IDENTIFIER = "wEATher";

    // Shared Preferences variables
    public static final String SP_LOCATION_SET = "locationSet";
    public static final String SP_LOCATIONS = "locations";

    // Search/Autocomplete JSON titles
    public static final String SEARCH_URL =
            "https://api.weatherapi.com/v1/search.json?key=" +
            WeatherRequestQueue.API_KEY + "&q=";
    private static final String NAME = "name";
    private static final String REGION = "region";
    private static final String COUNTRY = "country";
    private static final String URL = "url";

    private LocationUtil() {}

    /**
     * <p>
     *     Split {@link WeatherLocation} object into a {@link String}.
     * </p>
     * <p>
     *     Where the WeatherLocation info is separated by tabs, i.e. "\t"
     * </p>
     * <p>
     *     Maybe used for storage or transmission
     * </p>
     * @param weatherLocation {@link WeatherLocation} object to be serialized
     * @return {@link String} of all the {@link WeatherLocation} {@link String} components
     */
    public static synchronized String serialize(@NonNull WeatherLocation weatherLocation) {
        StringBuilder locationInfo = new StringBuilder(WEATHER_LOCATION_IDENTIFIER);

        locationInfo.append(STRING_SEPARATOR).append(weatherLocation.getUrlLocation());
        locationInfo.append(STRING_SEPARATOR).append(weatherLocation.getCity());
        locationInfo.append(STRING_SEPARATOR).append(weatherLocation.getCountry());
        locationInfo.append(STRING_SEPARATOR).append(weatherLocation.getCountryCode());

        if (weatherLocation.getRegion().equals(NOT_APPLICABLE)) {
            locationInfo.append(STRING_SEPARATOR + NOT_APPLICABLE);
        } else {
            locationInfo.append(STRING_SEPARATOR).append(weatherLocation.getRegion());
        }

        return locationInfo.toString();
    }


    /**
     * <p>
     *     Split {@link Collection}<{@link WeatherLocation}> into a {@link Set}<{@link String}>.
     * </p>
     * @param locations  {@link Collection}<{@link WeatherLocation}> to be serialized
     * @return {@link Set}<{@link String}> containing all {@link WeatherLocation} objects info
     */
    public static synchronized Set<String> serializeAll(@NonNull Collection<WeatherLocation> locations) {
        Set<String> locationSet = new LinkedHashSet<>();

        locations.forEach(weatherLocation ->
                locationSet.add(serialize(weatherLocation)));

        return locationSet;
    }

    /**
     * Deserializes a {@link Set}<{@link String} into a {@link WeatherLocation} object
     * @param weatherLocationInfo {@link String} to be de-serialized
     * @return null if not WeatherLocation info string provided, otherwise return {@link WeatherLocation} object
     */
    public static WeatherLocation deserialize(@NonNull String weatherLocationInfo) {

        String[] weatherLocationValues = weatherLocationInfo.split(STRING_SEPARATOR);
        boolean ifWeatherLocation = weatherLocationValues[0].equals(WEATHER_LOCATION_IDENTIFIER);

        if (ifWeatherLocation) {
            String urlLocation = weatherLocationValues[1];
            String city = weatherLocationValues[2];
            String country = weatherLocationValues[3];
            String countryCode = weatherLocationValues[4];
            String region = weatherLocationValues[5];

            return new WeatherLocation(
                    urlLocation, city, country, countryCode, region
            );
        }

        return null;
    }

    /**
     * Deserializes a {@link Set}<{@link String}> into {@link WeatherLocation} objects
     * @param locationSet {@link Set}<{@link String}> to be de-serialized
     * @return {@link Collection}<{@link WeatherLocation}>
     */
    public static Collection<WeatherLocation> deserializeAll(@NonNull Set<String> locationSet) {
        Collection<WeatherLocation> locations = new ArrayList<>();

        locationSet.forEach(string -> locations.add(deserialize(string)));

        return locations;
    }

    /**
     * <p>
     *     Parses a {@link JSONArray} into a {@link WeatherLocation}[]
     * </p>
     * Credit to <a href="https://github.com/TakahikoKawasaki/nv-i18n">Takahiko Kawasaki's nv-i18n</a> for getting 2 letter country codes
     * @param locationArray {@link JSONArray} to be parsed
     * @return {@link WeatherLocation}[]
     * @throws JSONException if error occurred whilst parsing the {@link JSONArray} object
     */
    public static WeatherLocation[] parseIntoWeatherLocations(JSONArray locationArray) throws JSONException {

        if (locationArray.length() != 0) {
            try {

                WeatherLocation[] weatherLocations =
                        new WeatherLocation[locationArray.length()];

                for (int i = 0; i < locationArray.length(); i++) {
                    JSONObject location = locationArray.getJSONObject(i);

                    String city = location.getString(NAME).split(", ")[0];
                    String urlLocation = location.getString(URL);
                    String country = location.getString(COUNTRY);
                    String region = location.getString(REGION);

                    String countryCode = null;

                    for (CountryCode code : CountryCode.values()) {
                        if (code.getName().equals(country)) {
                            countryCode = code.getAlpha2();
                        }
                    }

                    weatherLocations[i] =
                            new WeatherLocation(urlLocation, city, country, countryCode, region);
                }

                return weatherLocations;
            } catch (JSONException e) {
                throw e;
            }
        }

        return null;
    }
}
