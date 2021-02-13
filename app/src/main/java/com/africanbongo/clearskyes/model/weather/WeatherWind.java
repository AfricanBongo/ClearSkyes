package com.africanbongo.clearskyes.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
Contains wind properties of the weather
 */
public class WeatherWind implements Parcelable {
    private final double windSpeedMPH;
    private final double windSpeedKPH;
    private final String windDirection;

    public WeatherWind(double windSpeedMPH, double windSpeedKPH, String windDirection) {
        this.windSpeedMPH = windSpeedMPH;
        this.windSpeedKPH = windSpeedKPH;
        this.windDirection = windDirection;
    }

    protected WeatherWind(Parcel in) {
        windSpeedMPH = in.readDouble();
        windSpeedKPH = in.readDouble();
        windDirection = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(windSpeedMPH);
        dest.writeDouble(windSpeedKPH);
        dest.writeString(windDirection);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherWind> CREATOR = new Creator<WeatherWind>() {
        @Override
        public WeatherWind createFromParcel(Parcel in) {
            return new WeatherWind(in);
        }

        @Override
        public WeatherWind[] newArray(int size) {
            return new WeatherWind[size];
        }
    };

    public double getWindSpeed(WeatherMisc.Measurement measurement) {
        if (measurement == WeatherMisc.Measurement.METRIC) {
            return windSpeedKPH;
        }

        return windSpeedMPH;
    }

    public String getWindDirection() {
        return windDirection;
    }
}
