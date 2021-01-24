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
    private final String uvLevel;

    String lastUpdatedTime;

    public WeatherToday(String lastUpdatedTime, WeatherObject nowWeather, double uvLevel) {
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

    public WeatherObject getNowWeather() {
        return nowWeather;
    }

}
