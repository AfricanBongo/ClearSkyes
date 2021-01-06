package com.africanbongo.clearskyes.model.weatherobjects;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import static com.africanbongo.clearskyes.model.MiscMethods.getUVLevel;
import static com.africanbongo.clearskyes.model.WeatherTime.getRelativeDayAndProperTime;

/**
 * Contains all the weather information of the current day
 */
public class WeatherToday {

    // The weather at the current moment
    private final WeatherObject nowWeather;
    private final String uvLevel;
    // Data structure to hold the WeatherHour Objects
    private WeatherHour[] hours = new WeatherHour[23];

    String lastUpdatedTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public WeatherToday(String lastUpdatedTime, WeatherObject nowWeather, int uvLevel) {
        this.lastUpdatedTime = getRelativeDayAndProperTime(lastUpdatedTime);
        this.nowWeather = nowWeather;
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
}
