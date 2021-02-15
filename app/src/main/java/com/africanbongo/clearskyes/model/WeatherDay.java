package com.africanbongo.clearskyes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedHashMap;

import static com.africanbongo.clearskyes.util.MiscMethodsUtil.getUVLevel;

/**
Contains generalized information about the weather on a certain day
 **/
public class WeatherDay implements WeatherMappable, Parcelable {

    // Mapping keys
    public static final String AVG_TEMP_KEY = "Average temperature";
    public static final String MAX_TEMP_KEY = "Maximum Temperature";
    public static final String MIN_TEMP_KEY = "Minimum Temperature";
    public static final String CONDITION_KEY = "Condition description";
    public static final String PRECIPITATION_KEY = "Total precipitation";
    public static final String UV_KEY = "UV Level";
    public static final String HUMIDITY_KEY = "Average humidity";
    public static final String WIND_SPEED_KEY = "Maximum wind speed";

    /**
     * Used for {@link android.content.Intent} action, signifying intent carries a
     * {@link WeatherDay} object as its parcel
     */
    public static final String INTENT_ACTION = "open_weather_day";

    private final WeatherTemp maxTemp;
    private final WeatherTemp minTemp;
    private final WeatherTemp avgTemp;
    private final WeatherCondition conditions;
    private final WeatherMisc miscellaneous;
    private final double maxWindKPH;
    private final double maxWindMPH;
    private final String uvLevel;

    public WeatherDay(WeatherTemp maxTemp, WeatherTemp minTemp, WeatherTemp avgTemp,
                      WeatherCondition conditions, WeatherMisc miscellaneous,
                      double maxWindKPH, double maxWindMPH, double uvIndex) {

        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.avgTemp = avgTemp;
        this.conditions = conditions;
        this.miscellaneous = miscellaneous;
        this.maxWindKPH = maxWindKPH;
        this.maxWindMPH = maxWindMPH;
        this.uvLevel = getUVLevel(uvIndex);
    }

    protected WeatherDay(Parcel in) {
        maxTemp = in.readParcelable(WeatherTemp.class.getClassLoader());
        minTemp = in.readParcelable(WeatherTemp.class.getClassLoader());
        avgTemp = in.readParcelable(WeatherTemp.class.getClassLoader());
        conditions = in.readParcelable(WeatherCondition.class.getClassLoader());
        miscellaneous = in.readParcelable(WeatherMisc.class.getClassLoader());
        maxWindKPH = in.readDouble();
        maxWindMPH = in.readDouble();
        uvLevel = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(maxTemp, flags);
        dest.writeParcelable(minTemp, flags);
        dest.writeParcelable(avgTemp, flags);
        dest.writeParcelable(conditions, flags);
        dest.writeParcelable(miscellaneous, flags);
        dest.writeDouble(maxWindKPH);
        dest.writeDouble(maxWindMPH);
        dest.writeString(uvLevel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherDay> CREATOR = new Creator<WeatherDay>() {
        @Override
        public WeatherDay createFromParcel(Parcel in) {
            return new WeatherDay(in);
        }

        @Override
        public WeatherDay[] newArray(int size) {
            return new WeatherDay[size];
        }
    };

    public WeatherTemp getMaxTemp() {
        return maxTemp;
    }

    public WeatherTemp getMinTemp() {
        return minTemp;
    }

    public WeatherTemp getAvgTemp() {
        return avgTemp;
    }

    public WeatherCondition getConditions() {
        return conditions;
    }

    public WeatherMisc getMiscellaneous() {
        return miscellaneous;
    }

    public double getMaxWind(WeatherMisc.Measurement measurement) {
        if (measurement == WeatherMisc.Measurement.METRIC) {
            return maxWindKPH;
        }

        return maxWindMPH;
    }

    public String getUvLevel() {
        return uvLevel;
    }

    @Override
    public LinkedHashMap<String, String> getMapping(WeatherTemp.Degree degree, WeatherMisc.Measurement measurement) {
        LinkedHashMap<String, String> classMembersMap = new LinkedHashMap<>();
        classMembersMap.put(AVG_TEMP_KEY, avgTemp.getTemp(degree) + WeatherTemp.Degree.DEGREE_SIGN);
        classMembersMap.put(MAX_TEMP_KEY, maxTemp.getTemp(degree) + WeatherTemp.Degree.DEGREE_SIGN);
        classMembersMap.put(MIN_TEMP_KEY, minTemp.getTemp(degree) + WeatherTemp.Degree.DEGREE_SIGN);
        classMembersMap.put(CONDITION_KEY, conditions.getConditionText());
        classMembersMap.put(PRECIPITATION_KEY, miscellaneous.getPrecip(measurement) + measurement.precipNotation);
        classMembersMap.put(UV_KEY, uvLevel);
        classMembersMap.put(HUMIDITY_KEY, miscellaneous.getHumidity() + "%");
        classMembersMap.put(WIND_SPEED_KEY, getMaxWind(measurement) + measurement.speedNotation);

        return classMembersMap;
    }

    @Override
    public int categorySize() {
        return 2;
    }
}
