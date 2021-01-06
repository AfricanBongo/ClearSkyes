package com.africanbongo.clearskyes.model.weatherobjects;

/**
Class that holds common base information about weather
 */
public class WeatherObject {

    private final WeatherTemp actualTemp;
    private final WeatherTemp feelsLikeTemp;
    private final WeatherCondition conditions;
    private final WeatherMisc miscellaneous;
    private final WeatherWind wind;

    public WeatherObject(WeatherTemp actualTemp, WeatherTemp feelsLikeTemp,
                         WeatherCondition conditions, WeatherMisc miscellaneous, WeatherWind wind) {
        this.actualTemp = actualTemp;
        this.feelsLikeTemp = feelsLikeTemp;
        this.conditions = conditions;
        this.miscellaneous = miscellaneous;
        this.wind = wind;
    }

    public WeatherTemp getActualTemp() {
        return actualTemp;
    }

    public WeatherTemp getFeelsLikeTemp() {
        return feelsLikeTemp;
    }

    public WeatherCondition getConditions() {
        return conditions;
    }

    public WeatherMisc getMiscellaneous() {
        return miscellaneous;
    }

    public WeatherWind getWind() {
        return wind;
    }
}
