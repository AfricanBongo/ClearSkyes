package com.africanbongo.clearskyes.model.weather;

import androidx.annotation.NonNull;

import static com.africanbongo.clearskyes.model.util.MiscMethodsUtil.getUVLevel;
import static com.africanbongo.clearskyes.model.util.WeatherTimeUtil.getRelativeDayAndProperTime;

/**
 * Contains all the weather information of the current day
 */
public class WeatherToday {

    // The weather at the current moment
    private final WeatherObject nowWeather;
    private final AstroElement astronomy;
    private final String uvLevel;

    // Data structure to hold the WeatherHour Objects
    private final WeatherHour[] hours = new WeatherHour[WeatherHour.HOURS_IN_A_DAY];

    String lastUpdatedTime;

    public WeatherToday(String lastUpdatedTime, WeatherObject nowWeather, AstroElement astronomy, double uvLevel) {
        this.lastUpdatedTime = getRelativeDayAndProperTime(lastUpdatedTime);
        this.nowWeather = nowWeather;
        this.astronomy = astronomy;
        this.uvLevel = getUVLevel(uvLevel);
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public String getUvLevel() {
        return uvLevel;
    }

    /**
     * Add a WeatherHour object to the enclosing data structure
     * @param index The starting hour of the WeatherHour object
     * @param hour {@link WeatherHour} object to be added
     */
    public void addHour(int index, @NonNull WeatherHour hour) {
        hours[index] = hour;
    }

    public WeatherHour getHour(int index) {
        return hours[index];
    }

    public final WeatherHour[] getWeatherHours() {return hours;}

    public WeatherObject getNowWeather() {
        return nowWeather;
    }

    public AstroElement getAstronomy() {
        return astronomy;
    }
}
