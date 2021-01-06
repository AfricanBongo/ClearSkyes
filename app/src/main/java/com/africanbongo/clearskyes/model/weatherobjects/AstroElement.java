package com.africanbongo.clearskyes.model.weatherobjects;

/**
Contains astronomy properties
 */
public class AstroElement {
    private final String sunRise;
    private final String sunSet;
    private final String moonRise;
    private final String moonSet;
    private final String moonPhase;

    public AstroElement(String sunRise, String sunSet, String moonRise, String moonSet, String moonPhase) {
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.moonRise = moonRise;
        this.moonSet = moonSet;
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
