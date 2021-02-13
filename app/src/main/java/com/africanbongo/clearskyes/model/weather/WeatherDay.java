package com.africanbongo.clearskyes.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedHashMap;

import static com.africanbongo.clearskyes.util.MiscMethodsUtil.getUVLevel;

/**
Contains generalized information about the weather on a certain day
 **/
public class WeatherDay implements WeatherMappable, Parcelable {
    /**
     * Used for {@link android.content.Intent} action, signifying intent carries a
     * {@link WeatherDay} object as its parcel
     */
    public static final String WEATHER_DAY_ACTION = "weather_day";

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
        String avgTempKey = "Average temperature";
        String maxTempKey = "Maximum Temperature";
        String minTempKey = "Minimum Temperature";
        String conditionKey = "Condition description";
        String precipitationKey = "Total precipitation";
        String uvKey = "UV Level";
        String humidityKey = "Average humidity";
        String maxWindSpeedKey = "Maximum wind speed";

        LinkedHashMap<String, String> classMembersMap = new LinkedHashMap<>();
        classMembersMap.put(avgTempKey, avgTemp.getTemp(degree) + degree.getSymbol());
        classMembersMap.put(maxTempKey, maxTemp.getTemp(degree) + degree.getSymbol());
        classMembersMap.put(minTempKey, minTemp.getTemp(degree) + degree.getSymbol());
        classMembersMap.put(conditionKey, conditions.getConditionText());
        classMembersMap.put(precipitationKey, miscellaneous.getPrecip(measurement) + measurement.precipNotation);
        classMembersMap.put(uvKey, uvLevel);
        classMembersMap.put(humidityKey, miscellaneous.getHumidity() + "%");
        classMembersMap.put(maxWindSpeedKey, getMaxWind(measurement) + measurement.speedNotation);

        return classMembersMap;
    }

    @Override
    public int categorySize() {
        return 2;
    }
}
