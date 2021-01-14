package com.africanbongo.clearskyes.model.weatherobjects;

import static com.africanbongo.clearskyes.model.WeatherTime.getHourPeriod;

/**
Weather properties specifically for the hour
 */
public class WeatherHour extends WeatherObject {

    private final String time;
    private final int chanceOfSnow;
    private final int chanceOfRain;

    public static int HOURS_IN_A_DAY = 24;

    public WeatherHour(WeatherObject weatherObject, String time,
                       int chanceOfSnow, int chanceOfRain) {

        super(weatherObject.getActualTemp(), weatherObject.getFeelsLikeTemp(),
                weatherObject.getConditions(), weatherObject.getMiscellaneous(),
                weatherObject.getWind());

        this.time = getHourPeriod(time);
        this.chanceOfSnow = chanceOfSnow;
        this.chanceOfRain = chanceOfRain;
    }

    public String getTime() {
        return time;
    }

    public int getChanceOfSnow() {
        return chanceOfSnow;
    }

    public int getChanceOfRain() {
        return chanceOfRain;
    }
}
