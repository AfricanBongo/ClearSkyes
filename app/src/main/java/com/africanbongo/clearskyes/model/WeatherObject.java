package com.africanbongo.clearskyes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedHashMap;

/**
Class that holds common base information about weather
 */
public class WeatherObject implements WeatherMappable, Parcelable {

    // Mapping keys
    public static final String ACTUAL_TEMP_KEY = "Actual temperature";
    public static final String FEELS_LIKE_KEY = "Feels-like temperature";
    public static final String  CONDITION_KEY = "Condition description";
    public static final String  PRECIPITATION_KEY = "Precipitation";
    public static final String  HUMIDITY_KEY = "Humidity";
    public static final String  PRESSURE_KEY = "Pressure";

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

    protected WeatherObject(Parcel in) {
        actualTemp = in.readParcelable(WeatherTemp.class.getClassLoader());
        feelsLikeTemp = in.readParcelable(WeatherTemp.class.getClassLoader());
        conditions = in.readParcelable(WeatherCondition.class.getClassLoader());
        miscellaneous = in.readParcelable(WeatherMisc.class.getClassLoader());
        wind = in.readParcelable(WeatherWind.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(actualTemp, flags);
        dest.writeParcelable(feelsLikeTemp, flags);
        dest.writeParcelable(conditions, flags);
        dest.writeParcelable(miscellaneous, flags);
        dest.writeParcelable(wind, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherObject> CREATOR = new Creator<WeatherObject>() {
        @Override
        public WeatherObject createFromParcel(Parcel in) {
            return new WeatherObject(in);
        }

        @Override
        public WeatherObject[] newArray(int size) {
            return new WeatherObject[size];
        }
    };

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

    @Override
    public LinkedHashMap<String, String> getMapping(WeatherTemp.Degree degree, WeatherMisc.Measurement measurement) {
        LinkedHashMap<String, String> classMembersMap = new LinkedHashMap<>();
        classMembersMap.put(ACTUAL_TEMP_KEY, actualTemp.getTemp(degree) + WeatherTemp.Degree.DEGREE_SIGN);
        classMembersMap.put(FEELS_LIKE_KEY, feelsLikeTemp.getTemp(degree) + WeatherTemp.Degree.DEGREE_SIGN);
        classMembersMap.put(CONDITION_KEY, conditions.getConditionText());
        classMembersMap.put(PRECIPITATION_KEY, miscellaneous.getPrecip(measurement) + measurement.precipNotation);
        classMembersMap.put(HUMIDITY_KEY, miscellaneous.getHumidity() + "%");
        classMembersMap.put(PRESSURE_KEY, miscellaneous.getPressure(measurement) + measurement.pressureNotation);
        return classMembersMap;
    }

    @Override
    public int categorySize() {
        return 2;
    }
}
