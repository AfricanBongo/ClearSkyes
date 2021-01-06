package com.africanbongo.clearskyes.model.weatherobjects;

/**
Base class for holding temperatures celsius and fahrenheit
 */
public class WeatherTemp {
    private final float tempC;
    private final float tempF;

    public WeatherTemp(float tempC, float tempF) {
        this.tempC = tempC;
        this.tempF = tempF;
    }

    public float getTempC() {
        return tempC;
    }

    public float getTempF() {
        return tempF;
    }
}
