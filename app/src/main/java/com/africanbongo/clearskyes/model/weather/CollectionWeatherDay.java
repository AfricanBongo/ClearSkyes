package com.africanbongo.clearskyes.model.weather;

import androidx.annotation.NonNull;

/**
Contains {@link WeatherDay}, {@link AstroElement} and all {@link WeatherHour} objects for the day
 */
public class CollectionWeatherDay {
    private final WeatherDay day;
    private final AstroElement astronomy;
    private WeatherHour[] hours = new WeatherHour[WeatherHour.HOURS_IN_A_DAY];

    public CollectionWeatherDay(WeatherDay day, AstroElement astronomy) {
        this.day = day;
        this.astronomy = astronomy;
    }

    public WeatherDay getDay() {
        return day;
    }

    public AstroElement getAstronomy() {
        return astronomy;
    }

    /**
     * @param index starts at zero
     * @param weatherHour {@link WeatherHour is added to a data structure}
     */
    public void addWeatherHour(int index, @NonNull WeatherHour weatherHour) {
        hours[index] = weatherHour;
    }

    @NonNull
    public WeatherHour getWeatherHour(int index) {
        return hours[index];
    }

    public WeatherHour[] getHours() {
        return hours;
    }
}
