package com.africanbongo.clearskyes.controller.fragments;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.MainActivity;
import com.africanbongo.clearskyes.controller.customviews.CurrentWeatherViewDown;
import com.africanbongo.clearskyes.controller.customviews.CurrentWeatherViewUp;
import com.africanbongo.clearskyes.model.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.model.weatherobjects.AstroElement;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherCondition;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherHour;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherMisc;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherObject;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherTemp;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherToday;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherWind;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.africanbongo.clearskyes.model.WeatherTime.getCurrentHourAsIndex;

/*
Fragment containing today's weather
 */
public class WeatherTodayFragment extends Fragment {

    private WeatherToday today = null;
    private CurrentWeatherViewUp viewUp;
    private CurrentWeatherViewDown viewDown;
    private final MainActivity activity;

    public WeatherTodayFragment(MainActivity activity) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        requestData(context, activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_today, container, false);

        viewUp = view.findViewById(R.id.now_weatherview_up);
        viewDown = view.findViewById(R.id.now_weatherview_down);

        return view;
    }


    // Main Activity is used to display error page in the event of a failure
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void requestData(Context context, MainActivity activity) {

        // Parse WeatherApi JSONObjects
        Response.Listener<JSONObject> currentListener = (JSONObject response) -> {
            try {
                JSONObject currentWeatherJSON = response.getJSONObject("current");

                String lastUpdatedTime = currentWeatherJSON.getString("last_updated");

                double uvIndex = currentWeatherJSON.getDouble("uv");

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
                WeatherObject todayWeatherObject =
                        new WeatherObject(temps, feelsLike, weatherCondition, misc, wind);


                JSONObject forecast = response
                        .getJSONObject("forecast")
                        .getJSONArray("forecastday")
                        .getJSONObject(0);

                // AstroElement object
                JSONObject astro = forecast.getJSONObject("astro");

                String sunrise = astro.getString("sunrise");
                String sunset = astro.getString("sunset");
                String moonrise = astro.getString("moonrise");
                String moonset = astro.getString("moonset");
                String moonphase = astro.getString("moon_phase");

                AstroElement astroElement =
                        new AstroElement(sunrise, sunset, moonrise, moonset, moonphase);

                // Initialize WeatherTodayObject
                today = new WeatherToday(lastUpdatedTime, todayWeatherObject,
                        astroElement, uvIndex);

                // WeatherHour objects
                JSONArray hours = forecast.getJSONArray("hour");
                for (int i = 0; i < hours.length(); i++) {
                    JSONObject hourWeatherJSON = hours.getJSONObject(i);

                    String time = hourWeatherJSON.getString("time");

                    // WeatherTemp objects
                    double htemp_c = hourWeatherJSON.getDouble("temp_c");
                    double htemp_f = hourWeatherJSON.getDouble("temp_f");
                    double hfeelslike_c = hourWeatherJSON.getDouble("feelslike_c");
                    double hfeelslike_f = hourWeatherJSON.getDouble("feelslike_f");

                    WeatherTemp htemps = new WeatherTemp(htemp_c, htemp_f);
                    WeatherTemp hfeelsLike = new WeatherTemp(hfeelslike_c, hfeelslike_f);

                    // WeatherWind object
                    double hwind_mph = hourWeatherJSON.getDouble("wind_mph");
                    double hwind_kph = hourWeatherJSON.getDouble("wind_kph");
                    String hwind_dir = hourWeatherJSON.getString("wind_dir");

                    WeatherWind hwind = new WeatherWind(hwind_mph, hwind_kph, hwind_dir);

                    // WeatherMisc object
                    double hpressure_mb = hourWeatherJSON.getDouble("pressure_mb");
                    double hprecip_mm = hourWeatherJSON.getDouble("precip_mm");
                    int hhumidity = hourWeatherJSON.getInt("humidity");

                    WeatherMisc hmisc = new WeatherMisc(hpressure_mb, hprecip_mm, hhumidity);

                    // WeatherCondition object
                    JSONObject hconditions = currentWeatherJSON.getJSONObject("condition");
                    String hconditionText = hconditions.getString("text");
                    String hconditionIcon = hconditions.getString("icon");
                    int hconditionCode = hconditions.getInt("code");
                    boolean hday = hourWeatherJSON.getInt("is_day") == 1;

                    WeatherCondition hweatherCondition =
                            new WeatherCondition(hconditionText, hconditionIcon, hconditionCode, hday);

                    // Other properties
                    int chance_of_snow = hourWeatherJSON.getInt("chance_of_snow");
                    int chance_of_rain = hourWeatherJSON.getInt("chance_of_rain");

                    // WeatherObject object
                    WeatherObject hourWeatherObject =
                            new WeatherObject(htemps, hfeelsLike, hweatherCondition, hmisc, hwind);

                    today.addHour(i,
                            new WeatherHour(hourWeatherObject, time, chance_of_snow, chance_of_rain));
                }

                loadData();
            } catch (JSONException e) {
                Log.e("WeatherTodayFragment", "Failed to parse JSONObject");
                e.printStackTrace();
            }
        };

        // Display error page if an error is encountered
        Response.ErrorListener errorListener = error -> {

            View view = activity.showError();

            if (view != null) {
                // Get the error message views
                ImageView errorMessageImage = view.findViewById(R.id.error_message_image);
                TextView errorMessage = view.findViewById(R.id.error_message);

                String errorMessageText = null;
                int drawableToDisplay = 0;

                // If an error to the weather api
                if (error.networkResponse.statusCode == WeatherRequestQueue.API_ERROR_CODE) {
                    errorMessageText = WeatherRequestQueue.API_ERROR_MESSAGE;
                    drawableToDisplay = R.drawable.avd_error_warning;

                    // Or if there is no internet connection
                } else if (WeatherRequestQueue.getWeatherRequestQueue(context).isNetworkAvailable()){
                    errorMessageText = WeatherRequestQueue.NO_CONNECTION_MESSAGE;
                    drawableToDisplay = R.drawable.avd_no_connection;
                }

                errorMessage.setText(errorMessageText);

                // Start the AVD animation
                errorMessageImage.setImageResource(drawableToDisplay);
                AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) errorMessageImage.getDrawable();
                drawable.start();
            }

        };

        // Create JSONObjectRequest
        JsonObjectRequest requestTodayWeather =
                new JsonObjectRequest(Request.Method.GET,
                        WeatherRequestQueue.GET_CURRENT_WEATHER_START, null, currentListener,
                        errorListener);

        // Add to request queue
        WeatherRequestQueue
                .getWeatherRequestQueue(getContext())
                .addRequest(requestTodayWeather);
    }


    // Load the data into the views
    public void loadData() {
        if (today != null && viewUp != null && viewDown != null) {
            viewUp.setUvIndex(today.getUvLevel());
            viewUp.setFeelsLikeTemp((int) today.getNowWeather().getFeelsLikeTemp().getTempC());
            viewUp.setNowTemp((int) today.getNowWeather().getActualTemp().getTempC());
            viewUp.setConditionText(today.getNowWeather().getConditions().getConditionText());
            viewUp.setChanceOfRain(today.getHour(getCurrentHourAsIndex()).getChanceOfRain());

            // Load weather icon
            today.getNowWeather().getConditions().loadConditionImage(viewUp.getIconImageView());

            AstroElement astroElement = today.getAstronomy();

            viewDown.setMoonRise(astroElement.getMoonRise());
            viewDown.setMoonSet(astroElement.getMoonSet());
            viewDown.setSunRise(astroElement.getSunRise());
            viewDown.setSunSet(astroElement.getSunSet());

            // Load the weather hours fragment
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.now_weather_hours, new WeatherHoursFragment(today.getWeatherHours()))
                    .commit();
        }
    }
}