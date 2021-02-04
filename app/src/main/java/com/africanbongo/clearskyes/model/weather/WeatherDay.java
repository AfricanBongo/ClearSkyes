package com.africanbongo.clearskyes.model.weather;

import static com.africanbongo.clearskyes.util.MiscMethodsUtil.getUVLevel;

/**
Contains generalized information about the weather on a certain day
 **/
public class WeatherDay {
    private final WeatherTemp maxTemp;
    private final WeatherTemp minTemp;
    private final WeatherTemp avgTemp;
    private final WeatherCondition conditions;
    private final WeatherMisc miscellaneous;

    private final double maxWindKPH;
    private final double maxWindMPH;
    private final String uvLevel;

    public WeatherDay(WeatherTemp maxTemp, WeatherTemp minTemp, WeatherTemp avgTemp,
                      WeatherCondition conditions, WeatherMisc miscellaneous,
                      double maxWindKPH, double maxWindMPH, double uvIndex) {

        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.avgTemp = avgTemp;
        this.conditions = conditions;
        this.miscellaneous = miscellaneous;
        this.maxWindKPH = maxWindKPH;
        this.maxWindMPH = maxWindMPH;
        this.uvLevel = getUVLevel(uvIndex);
    }

    public WeatherTemp getMaxTemp() {
        return maxTemp;
    }

    public WeatherTemp getMinTemp() {
        return minTemp;
    }

    public WeatherTemp getAvgTemp() {
        return avgTemp;
    }

    public WeatherCondition getConditions() {
        return conditions;
    }

    public WeatherMisc getMiscellaneous() {
        return miscellaneous;
    }

    public double getMaxWindKPH() {
        return maxWindKPH;
    }

    public double getMaxWindMPH() {
        return maxWindMPH;
    }

    public String getUvLevel() {
        return uvLevel;
    }
}
