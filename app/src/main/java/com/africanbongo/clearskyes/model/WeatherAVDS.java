package com.africanbongo.clearskyes.model;

import com.africanbongo.clearskyes.R;

/**
 * Contains {@link android.graphics.drawable.AnimatedVectorDrawable} resource ids
 */
// TODO Wire up AVDs and write enum to hold avd ids
public enum WeatherAVDS {
    PARTLY_CLOUDY(1000, R.drawable.avd_cloudy_day, R.drawable.avd_cloudy_night);

    private final int avdID;
    private final int dayResourceId;
    private final int nightResourceId;

    WeatherAVDS(int avdID, int dayResourceId, int nightResourceId) {
        this.avdID = avdID;
        this.dayResourceId = dayResourceId;
        this.nightResourceId = nightResourceId;
    }

    public int getAvdID() {
        return avdID;
    }

    public int getDayResourceId() {
        return dayResourceId;
    }

    public int getNightResourceId() {
        return nightResourceId;
    }
}
