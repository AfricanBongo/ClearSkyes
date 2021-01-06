package com.africanbongo.clearskyes.controller.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.model.weatherobjects.CollectionWeatherDay;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherCondition;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherMisc;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherObject;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherTemp;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherToday;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherWind;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;

/*
Fragment containing today's weather
 */
public class WeatherTodayFragment extends Fragment {

    private WeatherToday today = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        requestData(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_today, container, false);
    }

    public void requestData(Context context) {
        Response.Listener<JSONObject> currentListener = (JSONObject response) -> {
            try {
                JSONObject currentWeatherJSON = response.getJSONObject("current");

                // WeatherTemp objects
                double temp_c = currentWeatherJSON.getDouble("temp_c");
                double temp_f = currentWeatherJSON.getDouble("temp_f");
                double feelslike_c = currentWeatherJSON.getDouble("feelslike_c");
                double feelslike_f = currentWeatherJSON.getDouble("feelslike_f");

                WeatherTemp temps = new WeatherTemp(temp_c, temp_f);
                WeatherTemp feelsLike = new WeatherTemp(feelslike_c, feelslike_f);

                // WeatherWind object
                double wind_mph = currentWeatherJSON.getDouble("wind_mph");
                double wind_kph = currentWeatherJSON.getDouble("wind_kph");
                String wind_dir = currentWeatherJSON.getString("wind_dir");

                WeatherWind wind = new WeatherWind(wind_mph, wind_kph, wind_dir);

                // WeatherMisc object
                double pressure_mb = currentWeatherJSON.getDouble("pressure_mb");
                double precip_mm = currentWeatherJSON.getDouble("precip_mm");
                int humidity = currentWeatherJSON.getInt("humidity");

                WeatherMisc misc = new WeatherMisc(pressure_mb, precip_mm, humidity);

                // WeatherCondition object
                JSONObject conditions = currentWeatherJSON.getJSONObject("condition");
                String conditionText = conditions.getString("text");
                String conditionIcon = conditions.getString("icon");
                int conditionCode = conditions.getInt("code");
                boolean day = currentWeatherJSON.getInt("is_day") == 1;

                WeatherCondition weatherCondition =
                        new WeatherCondition(conditionText, conditionIcon, conditionCode, day);


                // WeatherObject object
                WeatherObject weatherObject =
                        new WeatherObject(temps, feelsLike, weatherCondition, misc, wind);

                // TODO Write for the WeatherHourObjects

            } catch (JSONException e) {
                Log.e("WeatherTodayFragment", "Failed to parse JSONObject");
            }
        };
    }
}