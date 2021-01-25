package com.africanbongo.clearskyes.model.weatherapi.util;

import com.africanbongo.clearskyes.R;

/**
 * Contains {@link android.graphics.drawable.AnimatedVectorDrawable} resource ids
 */
public enum WeatherAVDS {

    CLEAR(1000, R.drawable.avd_sunny, R.drawable.avd_moon),
    PARTLY_CLOUDY(1003, R.drawable.avd_partly_cloudy_day, R.drawable.avd_partly_cloudy_night),
    CLOUDY(1006, R.drawable.avd_cloudy_day, R.drawable.avd_cloudy_night),
    OVERCAST(1009, R.drawable.ic_overcast, R.drawable.ic_overcast),
    MIST(1030, R.drawable.avd_mist_fog_night, R.drawable.avd_mist_fog_day),

    PATCHY_RAIN_POSSIBLE(1063, R.drawable.avd_light_moderate_rain_day, R.drawable.avd_light_moderate_rain_night),
    PATCHY_SNOW_POSSIBLE(1066, R.drawable.avd_snow_day, R.drawable.avd_snow_night),
    THUNDERY_OUTBREAKS_POSSIBLE(1087, R.drawable.avd_storm_day, R.drawable.avd_storm_night),

    FOG(1135, R.drawable.avd_mist_fog_night, R.drawable.avd_mist_fog_day),
    FREEZING_FOG(1147, R.drawable.avd_mist_fog_night, R.drawable.avd_mist_fog_day),

    PATCHY_LIGHT_DRIZZLE(1150, R.drawable.avd_drizzle_day, R.drawable.avd_drizzle_night),
    LIGHT_DRIZZLE(1153, R.drawable.avd_drizzle_day, R.drawable.avd_drizzle_night),
    FREEZING_DRIZZLE(1168, R.drawable.avd_drizzle_day, R.drawable.avd_drizzle_night),
    HEAVY_FREEZING_DRIZZLE(1171, R.drawable.avd_drizzle_day, R.drawable.avd_drizzle_night),

    PATCHY_LIGHT_RAIN(1180, R.drawable.avd_light_moderate_rain_day, R.drawable.avd_light_moderate_rain_night),
    LIGHT_RAIN(1183, R.drawable.avd_light_moderate_rain_day, R.drawable.avd_light_moderate_rain_night),
    MODERATE_RAIN_AT_TIMES(1186, R.drawable.avd_light_moderate_rain_day, R.drawable.avd_light_moderate_rain_night),
    MODERATE_RAIN(1189, R.drawable.avd_light_moderate_rain_day, R.drawable.avd_light_moderate_rain_night),

    PATCHY_LIGHT_SNOW(1210, R.drawable.avd_snow_day, R.drawable.avd_snow_night),
    LIGHT_SNOW(1213, R.drawable.avd_snow_day, R.drawable.avd_snow_night),
    PATCHY_MODERATE_SNOW(1216, R.drawable.avd_snow_day, R.drawable.avd_snow_night),
    MODERATE_SNOW(1219, R.drawable.avd_snow_day, R.drawable.avd_snow_night),

    LIGHT_RAIN_SHOWER(1240, R.drawable.avd_light_moderate_rain_day, R.drawable.avd_light_moderate_rain_night),
    MODERATE_OR_HEAVY_RAIN_SHOWER(1243, R.drawable.avd_light_moderate_rain_day, R.drawable.avd_light_moderate_rain_night);


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
