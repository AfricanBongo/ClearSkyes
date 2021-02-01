package com.africanbongo.clearskyes.model.weather;

/**
Contains wind properties of the weather
 */
public class WeatherWind {
    public enum Measurement {
        M("Metric"),
        F("Imperial");

        private String type;
        Measurement(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private final double windSpeedMPH;
    private final double windSpeedKPH;
    private final String windDirection;

    public WeatherWind(double windSpeedMPH, double windSpeedKPH, String windDirection) {
        this.windSpeedMPH = windSpeedMPH;
        this.windSpeedKPH = windSpeedKPH;
        this.windDirection = windDirection;
    }

    public double getWindSpeedMPH() {
        return windSpeedMPH;
    }

    public double getWindSpeedKPH() {
        return windSpeedKPH;
    }

    public String getWindDirection() {
        return windDirection;
    }
}