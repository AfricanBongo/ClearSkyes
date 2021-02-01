package com.africanbongo.clearskyes.model.weather;

/**
Base class for holding temperatures celsius and fahrenheit
 */
public class WeatherTemp {
    public enum Degree {
        F("Fahrenheit"),
        C("Celsius");

        private String degreesType;
        Degree(String degreesType) {
            this.degreesType = degreesType;
        }

        public String getStringDegree() {
            return degreesType;
        }
        public static Degree getDegree(String degreeString) {
            if (degreeString.toLowerCase().equals(F.getStringDegree().toLowerCase())) {
                return F;
            }

            return C;
        }
    }

    private final double tempC;
    private final double tempF;

    public WeatherTemp(double tempC, double tempF) {
        this.tempC = tempC;
        this.tempF = tempF;
    }

    public double getTemp(Degree degree) {
        switch (degree) {
            case F: return tempF;
            case C: return tempC;
        }

        return 0;
    }
}
