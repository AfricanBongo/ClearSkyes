package com.africanbongo.clearskyes.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
Base class for holding temperatures celsius and fahrenheit
 */
public class WeatherTemp implements Parcelable {

    /**
     * Enum class to denote which weather notation to use, Fahrenheit or Celsius
     */
    public enum Degree {
        F("Fahrenheit","°F"),
        C("Celsius", "°C");

        private final String degreesType;
        private final String symbol;
        Degree(String degreesType, String symbol) {
            this.degreesType = degreesType;
            this.symbol = symbol;
        }

        public String getStringDegree() {
            return degreesType;
        }

        public String getSymbol() {
            return symbol;
        }

        public static Degree getDegree(String degreeString) {
            if (degreeString.toLowerCase().equals(F.getStringDegree().toLowerCase())) {
                return F;
            }

            return C;
        }
    }

    private final double tempC;
    private final double tempF;

    public WeatherTemp(double tempC, double tempF) {
        this.tempC = tempC;
        this.tempF = tempF;
    }

    protected WeatherTemp(Parcel in) {
        tempC = in.readDouble();
        tempF = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(tempC);
        dest.writeDouble(tempF);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherTemp> CREATOR = new Creator<WeatherTemp>() {
        @Override
        public WeatherTemp createFromParcel(Parcel in) {
            return new WeatherTemp(in);
        }

        @Override
        public WeatherTemp[] newArray(int size) {
            return new WeatherTemp[size];
        }
    };

    /**
     * Returns a rounded off temperature reading
     * @param degree {@link Degree} type to return
     * @return rounded integer denoting temperature reading
     */
    public int getTemp(Degree degree) {
        double temp = 0;

        switch (degree) {
            case F:
                temp = tempF;
                break;
            case C:
                temp = tempC;
                break;
        }

        return (int) Math.round(temp);
    }
}
