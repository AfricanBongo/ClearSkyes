package com.africanbongo.clearskyes.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
Contains other miscellaneous weather properties
 */
public class WeatherMisc implements Parcelable {

    public enum Measurement {
        IMPERIAL("mph", "in", "in"),
        METRIC("kph", "mm", "mb");

        final String speedNotation;
        final String precipNotation;
        final String pressureNotation;

        Measurement(String speedNotation, String precipNotation, String pressureNotation) {
            this.speedNotation = speedNotation;
            this.precipNotation = precipNotation;
            this.pressureNotation = pressureNotation;
        }


        public static Measurement getMeasurement(@NonNull String measurementString) {
            if (measurementString.toLowerCase().equals("imperial")) {
                return IMPERIAL;
            }

            return METRIC;
        }


    }

    private final double pressureIn;
    private final double pressureMB;
    private final double precipMM;
    private final double precipIn;
    private final int humidity;

    public WeatherMisc(double pressureIn, double pressureMB, double precipIn, double precipMM, int humidity) {
        this.pressureIn = pressureIn;
        this.pressureMB = pressureMB;
        this.precipMM = precipMM;
        this.precipIn = precipIn;
        this.humidity = humidity;
    }

    protected WeatherMisc(Parcel in) {
        pressureIn = in.readDouble();
        pressureMB = in.readDouble();
        precipIn = in.readDouble();
        precipMM = in.readDouble();
        humidity = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(pressureIn);
        dest.writeDouble(pressureMB);
        dest.writeDouble(precipMM);
        dest.writeDouble(precipIn);
        dest.writeInt(humidity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherMisc> CREATOR = new Creator<WeatherMisc>() {
        @Override
        public WeatherMisc createFromParcel(Parcel in) {
            return new WeatherMisc(in);
        }

        @Override
        public WeatherMisc[] newArray(int size) {
            return new WeatherMisc[size];
        }
    };

    public double getPressure(Measurement measurement) {
        if (measurement == Measurement.METRIC) {
            return pressureMB;
        }

        return pressureIn;
    }

    public double getPrecip(Measurement measurement) {
        if (measurement == Measurement.METRIC) {
            return precipMM;
        }

        return precipIn;
    }

    public int getHumidity() {
        return humidity;
    }

}
