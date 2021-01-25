package com.africanbongo.clearskyes.model.weather;

import android.os.Build;

import androidx.annotation.RequiresApi;

import static com.africanbongo.clearskyes.model.weatherapi.util.WeatherTimeUtil.get24HourNotation;

/**
Contains astronomy properties
 */
public class AstroElement {
    private final String sunRise;
    private final String sunSet;
    private final String moonRise;
    private final String moonSet;
    private final String moonPhase;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public AstroElement(String sunRise, String sunSet, String moonRise, String moonSet, String moonPhase) {
        this.sunRise = get24HourNotation(sunRise);
        this.sunSet = get24HourNotation(sunSet);
        this.moonRise = get24HourNotation(moonRise);
        this.moonSet = get24HourNotation(moonSet);
        this.moonPhase = moonPhase;
    }

    public String getSunRise() {
        return sunRise;
    }

    public String getSunSet() {
        return sunSet;
    }

    public String getMoonRise() {
        return moonRise;
    }

    public String getMoonSet() {
        return moonSet;
    }

    public String getMoonPhase() {
        return moonPhase;
    }
}
