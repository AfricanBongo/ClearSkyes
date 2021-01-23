package com.africanbongo.clearskyes.model.weather;

/**
Contains other miscellaneous weather properties
 */
public class WeatherMisc {
    private final double pressureMB;
    private final double precipMM;
    private final double humidity;

    public WeatherMisc(double pressureMB, double precipMM, double humidity) {
        this.pressureMB = pressureMB;
        this.precipMM = precipMM;
        this.humidity = humidity;
    }

    public double getPressureMB() {
        return pressureMB;
    }

    public double getPrecipMM() {
        return precipMM;
    }

    public double getHumidity() {
        return humidity;
    }

}
