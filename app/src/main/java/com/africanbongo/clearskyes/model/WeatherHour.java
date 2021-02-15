package com.africanbongo.clearskyes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedHashMap;

import static com.africanbongo.clearskyes.util.WeatherTimeUtil.getHourPeriod;

/**
Weather properties specifically for the hour
 */
public class WeatherHour extends WeatherObject implements Parcelable {

    /**
     * Used for {@link android.content.Intent} action, signifying intent carries a
     * {@link WeatherHour} object as its parcel
     */
    public static final String INTENT_ACTION = "open_weather_hour";

    // Mapping values
    public static final String SNOW_KEY = "Chance of snow";
    public static final String RAIN_KEY = "Chance of rain";

    private final String time;
    private final int chanceOfSnow;
    private final int chanceOfRain;

    public static int HOURS_IN_A_DAY = 24;

    public WeatherHour(WeatherObject weatherObject, String time,
                       int chanceOfSnow, int chanceOfRain) {

        super(weatherObject.getActualTemp(), weatherObject.getFeelsLikeTemp(),
                weatherObject.getConditions(), weatherObject.getMiscellaneous(),
                weatherObject.getWind());

        this.time = getHourPeriod(time);
        this.chanceOfSnow = chanceOfSnow;
        this.chanceOfRain = chanceOfRain;
    }

    protected WeatherHour(Parcel in) {
        super(in);
        time = in.readString();
        chanceOfSnow = in.readInt();
        chanceOfRain = in.readInt();
    }
    public String getTime() {
        return time;
    }

    public int getChanceOfSnow() {
        return chanceOfSnow;
    }

    public int getChanceOfRain() {
        return chanceOfRain;
    }

    @Override
    public LinkedHashMap<String, String> getMapping(WeatherTemp.Degree degree, WeatherMisc.Measurement measurement) {
        LinkedHashMap<String, String> classMembersMap = super.getMapping(degree, measurement);
        classMembersMap.put(RAIN_KEY, chanceOfRain + "%");
        classMembersMap.put(SNOW_KEY, chanceOfSnow + "%");
        return classMembersMap;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(time);
        dest.writeInt(chanceOfSnow);
        dest.writeInt(chanceOfRain);
    }

    public static final Creator<WeatherHour> CREATOR = new Creator<WeatherHour>() {
        @Override
        public WeatherHour createFromParcel(Parcel source) {
            return new WeatherHour(source);
        }

        @Override
        public WeatherHour[] newArray(int size) {
            return new WeatherHour[size];
        }
    };


}
