package com.africanbongo.clearskyes.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedHashMap;

import static com.africanbongo.clearskyes.util.MiscMethodsUtil.getUVLevel;
import static com.africanbongo.clearskyes.util.WeatherTimeUtil.getRelativeDayAndProperTime;

/**
 * Contains all the weather information of the current day
 */
public class WeatherToday implements Parcelable, WeatherMappable {

    // Mapping keys
    public static final String UV_KEY = "UV Level";

    /**
     * Used for {@link android.content.Intent} action, signifying intent carries a
     * {@link WeatherToday} object as its parcel
     */
    public static final String WEATHER_TODAY_ACTION = "weather_today";

    // The weather at the current moment
    private final WeatherObject nowWeather;
    private final String uvLevel;

    String lastUpdatedTime;

    public WeatherToday(String lastUpdatedTime, WeatherObject nowWeather, double uvLevel) {
        this.lastUpdatedTime = getRelativeDayAndProperTime(lastUpdatedTime);
        this.nowWeather = nowWeather;
        this.uvLevel = getUVLevel(uvLevel);
    }

    protected WeatherToday(Parcel in) {
        nowWeather = in.readParcelable(WeatherObject.class.getClassLoader());
        uvLevel = in.readString();
        lastUpdatedTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(nowWeather, flags);
        dest.writeString(uvLevel);
        dest.writeString(lastUpdatedTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherToday> CREATOR = new Creator<WeatherToday>() {
        @Override
        public WeatherToday createFromParcel(Parcel in) {
            return new WeatherToday(in);
        }

        @Override
        public WeatherToday[] newArray(int size) {
            return new WeatherToday[size];
        }
    };

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public String getUvLevel() {
        return uvLevel;
    }

    public WeatherObject getNowWeather() {
        return nowWeather;
    }

    @Override
    public LinkedHashMap<String, String> getMapping(WeatherTemp.Degree degree, WeatherMisc.Measurement measurement) {
        LinkedHashMap<String, String> classMembersMap = nowWeather.getMapping(degree, measurement);
        classMembersMap.put(UV_KEY, uvLevel);
        return classMembersMap;
    }

    @Override
    public int categorySize() {
        return nowWeather.categorySize();
    }
}
