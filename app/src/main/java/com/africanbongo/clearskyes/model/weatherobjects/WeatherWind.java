package com.africanbongo.clearskyes.model.weatherobjects;

/**
Contains wind properties of the weather
 */
public class WeatherWind {
    private final float windSpeedMPH;
    private final float windSpeedKPH;
    private final String windDirection;

    public WeatherWind(float windSpeedMPH, float windSpeedKPH, String windDirection) {
        this.windSpeedMPH = windSpeedMPH;
        this.windSpeedKPH = windSpeedKPH;
        this.windDirection = windDirection;
    }

    public float getWindSpeedMPH() {
        return windSpeedMPH;
    }

    public float getWindSpeedKPH() {
        return windSpeedKPH;
    }

    public String getWindDirection() {
        return windDirection;
    }
}
