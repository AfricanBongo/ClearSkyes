package com.africanbongo.clearskyes.model.weatherobjects;

/**
Contains generalized information about the weather on a certain day
 **/
public class WeatherDay {
    private final WeatherTemp maxTemp;
    private final WeatherTemp minTemp;
    private final WeatherTemp avgTemp;
    private final WeatherCondition conditions;
    private final WeatherMisc miscellaneous;

    private final float maxWindKPH;
    private final float maxWindMPH;

    public WeatherDay(WeatherTemp maxTemp, WeatherTemp minTemp, WeatherTemp avgTemp,
                      WeatherCondition conditions, WeatherMisc miscellaneous,
                      float maxWindKPH, float maxWindMPH) {

        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.avgTemp = avgTemp;
        this.conditions = conditions;
        this.miscellaneous = miscellaneous;
        this.maxWindKPH = maxWindKPH;
        this.maxWindMPH = maxWindMPH;
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

    public float getMaxWindKPH() {
        return maxWindKPH;
    }

    public float getMaxWindMPH() {
        return maxWindMPH;
    }
}
