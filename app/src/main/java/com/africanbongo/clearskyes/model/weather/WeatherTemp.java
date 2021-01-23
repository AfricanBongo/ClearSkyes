package com.africanbongo.clearskyes.model.weather;

/**
Base class for holding temperatures celsius and fahrenheit
 */
public class WeatherTemp {
    private final double tempC;
    private final double tempF;

    public WeatherTemp(double tempC, double tempF) {
        this.tempC = tempC;
        this.tempF = tempF;
    }

    public double getTempC() {
        return tempC;
    }

    public double getTempF() {
        return tempF;
    }
}
