package com.africanbongo.clearskyes.util;

import com.africanbongo.clearskyes.model.weather.AstroElement;
import com.africanbongo.clearskyes.model.weather.WeatherCondition;
import com.africanbongo.clearskyes.model.weather.WeatherDay;
import com.africanbongo.clearskyes.model.weather.WeatherHour;
import com.africanbongo.clearskyes.model.weather.WeatherMisc;
import com.africanbongo.clearskyes.model.weather.WeatherObject;
import com.africanbongo.clearskyes.model.weather.WeatherTemp;
import com.africanbongo.clearskyes.model.weather.WeatherToday;
import com.africanbongo.clearskyes.model.weather.WeatherWind;
import com.africanbongo.clearskyes.model.weatherapi.WeatherRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class used to parse host server Weather {@link JSONObject}(s) into:
 * <ul>
 *     <li> {@link com.africanbongo.clearskyes.model.weather.WeatherToday} object</li>
 *     <li> {@link com.africanbongo.clearskyes.model.weather.WeatherObject} object</li>
 *     <li> {@link com.africanbongo.clearskyes.model.weather.WeatherDay} object</li>
 *     <li> {@link com.africanbongo.clearskyes.model.weather.WeatherHour} object</li>
 *     <li> {@link com.africanbongo.clearskyes.model.weather.AstroElement} object</li>
 * </ul>
 */
public final class WeatherJsonUtil {

    private static final String ASTRO_ELEMENT_ERROR = "Error parsing JSON into AstroElement object";
    private static final String WEATHER_DAY_ERROR = "Error parsing JSON into WeatherDay object";
    private static final String WEATHER_HOUR_ERROR = "Error parsing JSON into WeatherHour object";
    private static final String WEATHER_TODAY_ERROR = "Error parsing JSON into WeatherToday object";


    private WeatherJsonUtil() {}


    /**
     * Parses a {@link JSONObject} into a local {@link WeatherToday} object
     * @param json {@link JSONObject} containing the current day's weather information
     * @return {@link WeatherToday} object
     * @throws JSONException
     */
    public static WeatherToday parseIntoWeatherToday(JSONObject json) throws JSONException{
        if (json.has("current")) {
            try {
                JSONObject currentWeatherJSON = json.getJSONObject("current");

                String lastUpdatedTime = currentWeatherJSON.getString("last_updated");
                double uvIndex = currentWeatherJSON.getDouble("uv");

                WeatherObject weatherObject = parseIntoWeatherObject(currentWeatherJSON);

                return new WeatherToday(lastUpdatedTime, weatherObject, uvIndex);
            } catch (JSONException error){
                throw error;
            }
        }

        throw new JSONException(WEATHER_TODAY_ERROR);
    }


    /**
     * Parses a {@link JSONObject} object into a local {@link AstroElement} object
     * @param json {@link JSONObject} containing {@link AstroElement} values in them
     * @return {@link AstroElement} containing astronomy values
     * @throws JSONException if {@link JSONObject} doesn't contain {@link AstroElement} members, or if there's an error in parsing
     */
    public static AstroElement parseIntoAstroElement(JSONObject json) throws JSONException {
        if (json.has("astro")) {
            try {
                // AstroElement object
                JSONObject astro = json.getJSONObject("astro");

                String sunrise = astro.getString("sunrise");
                String sunset = astro.getString("sunset");
                String moonrise = astro.getString("moonrise");
                String moonset = astro.getString("moonset");
                String moonphase = astro.getString("moon_phase");

                return new AstroElement(sunrise, sunset, moonrise, moonset, moonphase);
            } catch (JSONException error) {
                throw error;
            }
        }

        throw new JSONException(ASTRO_ELEMENT_ERROR);
    }

    /**
     * Parses a {@link JSONObject} object into a {@link WeatherHour}[]
     * @param json {@link JSONObject} containing a {@link JSONArray} with {@link WeatherHour} elements
     * @return {@link WeatherHour}[] containing all the {@link WeatherHour} objects parsed
     * @throws JSONException if {@link JSONObject} doesn't contain {@link JSONArray} with {@link WeatherHour} members, or if there's an error in parsing
     */
    public static WeatherHour[] parseIntoWeatherHourArray(JSONObject json) throws JSONException {
        if (json.has("hour")) {
            try {
                WeatherHour[] weatherHours = new WeatherHour[WeatherHour.HOURS_IN_A_DAY];

                // WeatherHour objects
                JSONArray hours = json.getJSONArray("hour");

                for (int i = 0; i < hours.length(); i++) {
                    JSONObject hourWeatherJSON = hours.getJSONObject(i);

                    String time = hourWeatherJSON.getString("time");

                    // Other properties
                    int chance_of_snow = hourWeatherJSON.getInt("chance_of_snow");
                    int chance_of_rain = hourWeatherJSON.getInt("chance_of_rain");

                    // WeatherObject object
                    WeatherObject hourWeatherObject = parseIntoWeatherObject(hourWeatherJSON);

                    weatherHours[i] =
                            new WeatherHour(hourWeatherObject, time, chance_of_snow, chance_of_rain);
                }

                return weatherHours;
            } catch (JSONException error) {
                throw error;
            }
        }

        throw new JSONException(WEATHER_HOUR_ERROR);
    }

    /**
     * Parses a {@link JSONObject} into a local {@link WeatherObject} object
     * @param json {@link JSONObject} to be parsed
     * @return {@link WeatherObject} object
     */
    public static WeatherObject parseIntoWeatherObject(JSONObject json) throws JSONException {

        try {
            // WeatherTemp objects
            double temp_c = json.getDouble("temp_c");
            double temp_f = json.getDouble("temp_f");
            double feelslike_c = json.getDouble("feelslike_c");
            double feelslike_f = json.getDouble("feelslike_f");

            WeatherTemp temp = new WeatherTemp(temp_c, temp_f);
            WeatherTemp feelsLike = new WeatherTemp(feelslike_c, feelslike_f);

            // WeatherWind object
            double wind_mph = json.getDouble("wind_mph");
            double wind_kph = json.getDouble("wind_kph");
            String wind_dir = json.getString("wind_dir");

            WeatherWind wind = new WeatherWind(wind_mph, wind_kph, wind_dir);

            // WeatherMisc object
            double pressure_in = json.getDouble("pressure_in");
            double pressure_mb = json.getDouble("pressure_mb");
            double precip_in = json.getDouble("precip_in");
            double precip_mm = json.getDouble("precip_mm");
            int humidity = json.getInt("humidity");

            WeatherMisc misc =
                    new WeatherMisc(pressure_in, pressure_mb, precip_in, precip_mm, humidity);

            // WeatherCondition object
            JSONObject condition = json.getJSONObject("condition");
            String conditionString = condition.getString("text");
            String icon = condition.getString("icon");
            int conditionCode = condition.getInt("code");
            boolean isDay = json.getInt("is_day") == 1;

            WeatherCondition weatherCondition =
                    new WeatherCondition(conditionString, icon, conditionCode, isDay);

            // WeatherObject object
            return new WeatherObject(temp, feelsLike, weatherCondition, misc, wind);

        } catch (JSONException e) {
            throw e;
        }
    }

    /**
     * Parses a {@link JSONObject} into a local {@link WeatherDay} object
     * @param json {@link JSONObject} to be parsed
     * @return {@link WeatherDay} object
     * @throws JSONException if {@link JSONObject} doesn't contain {@link WeatherDay} members, or if there's an error in parsing
     */
    public static WeatherDay parseIntoWeatherDay(JSONObject json) throws JSONException {
        if (json.has("day")) {
            try {
                JSONObject dayJSON = json.getJSONObject("day");

                double maxtemp_c = dayJSON.getDouble("maxtemp_c");
                double maxtemp_f = dayJSON.getDouble("maxtemp_f");
                double mintemp_c = dayJSON.getDouble("mintemp_c");
                double mintemp_f = dayJSON.getDouble("mintemp_f");
                double avgtemp_c = dayJSON.getDouble("avgtemp_c");
                double avgtemp_f = dayJSON.getDouble("avgtemp_f");

                WeatherTemp maxTemp = new WeatherTemp(maxtemp_c, maxtemp_f);
                WeatherTemp minTemp = new WeatherTemp(mintemp_c, mintemp_f);
                WeatherTemp avgTemp = new WeatherTemp(avgtemp_c, avgtemp_f);

                // Get miscellaneous details
                int avghumidity = dayJSON.getInt("avghumidity");
                int uvIndex = dayJSON.getInt("uv");
                double totalprecip_in = dayJSON.getDouble("totalprecip_in");
                double totalprecip_mm = dayJSON.getDouble("totalprecip_mm");
                double maxwind_kph = dayJSON.getDouble("maxwind_kph");
                double maxwind_mph = dayJSON.getDouble("maxwind_mph");


                WeatherMisc misc =
                        new WeatherMisc(-1, -1, totalprecip_in, totalprecip_mm, avghumidity);

                // Get weather conditions
                JSONObject conditionJSON = dayJSON.getJSONObject("condition");

                String conditionString = conditionJSON.getString("text");
                String icon = conditionJSON.getString("icon");
                int code = conditionJSON.getInt("code");

                WeatherCondition condition =
                        new WeatherCondition(conditionString, icon, code, true);

                return new WeatherDay(maxTemp, minTemp, avgTemp, condition,
                        misc, maxwind_kph, maxwind_mph, uvIndex);
            } catch (JSONException error) {
                throw error;
            }
        } else {
            throw new JSONException(WEATHER_DAY_ERROR);
        }
    }

    /**
     * Generate the url used to get data from the Weather API
     * @param location {@link String} used to grab data for a specific location eg. Harare
     * @return {@link String} url
     */
    public static String generateURL(String location, int days) {
        return "https://api.weatherapi.com/v1/forecast.json?key=" +
                WeatherRequestQueue.API_KEY +"&q=" + location + "&days=" + days;
    }
}
