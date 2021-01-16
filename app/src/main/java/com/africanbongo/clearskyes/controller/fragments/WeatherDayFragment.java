package com.africanbongo.clearskyes.controller.fragments;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.MainActivity;
import com.africanbongo.clearskyes.controller.animations.SwitchFadeAnimation;
import com.africanbongo.clearskyes.controller.customviews.AstroView;
import com.africanbongo.clearskyes.controller.customviews.CustomDateView;
import com.africanbongo.clearskyes.controller.customviews.DayWeatherViewUp;
import com.africanbongo.clearskyes.model.WeatherTime;
import com.africanbongo.clearskyes.model.weatherapi.ErrorPageListener;
import com.africanbongo.clearskyes.model.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.model.weatherobjects.AstroElement;
import com.africanbongo.clearskyes.model.weatherobjects.CollectionWeatherDay;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherCondition;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherDay;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherHour;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherMisc;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherObject;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherTemp;
import com.africanbongo.clearskyes.model.weatherobjects.WeatherWind;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static android.view.View.GONE;

public class WeatherDayFragment extends Fragment {

    private CollectionWeatherDay collectionWeatherDay;

    // Error Listener for fetching Weather JSON data
    private static ErrorPageListener errorListener;

    private RelativeLayout loadingLayout;
    private RelativeLayout layout;
    private CustomDateView dateView;
    private DayWeatherViewUp viewUp;
    private AstroView astroView;

    // The day of the week to display
    String day;
    int daysOffset;

    public WeatherDayFragment() {
    }

    public static WeatherDayFragment newInstance(MainActivity activity, int daysOffset) {

        // Set up error listener first
        errorListener = new ErrorPageListener(activity);

        Bundle args = new Bundle();
        args.putInt("daysOffset", daysOffset);

        WeatherDayFragment fragment = new WeatherDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_day, container, false);

        // Fetch views
        layout = view.findViewById(R.id.day_layout);
        layout.setVisibility(GONE);

        // Fetch loading page
        loadingLayout = view.findViewById(R.id.day_loading_layout);

        viewUp = view.findViewById(R.id.day_weatherview_up);
        dateView = view.findViewById(R.id.day_date_view);
        astroView = view.findViewById(R.id.day_astro_view);

        if (getArguments() != null) {
            this.daysOffset = getArguments().getInt("daysOffset");

            // Get date string formatted so as to fetch JSON data
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern(WeatherTime.DATE_FORMAT);

            LocalDate localDate = LocalDate.now().plusDays(daysOffset);
            day = WeatherTime.getRelativeDay(localDate);

            requestData(dateTimeFormatter.format(localDate));
        }

        return view;
    }


    public void requestData(String date) {
        new Thread(() -> {

            String requestURL = WeatherRequestQueue.GET_DAY_WEATHER_START + date;

            WeatherRequestQueue requestQueue = WeatherRequestQueue
                    .getWeatherRequestQueue(getContext());

            // Listener for JSON Data
            Response.Listener<JSONObject> dayListener = response -> new Thread(() -> {
                try {

                    // Only start this animation when data has been successfully received
                    // from the API
                    startLoadingAnimation();

                    JSONObject dayWeatherJSON = response
                            .getJSONObject("forecast")
                            .getJSONArray("forecastday")
                            .getJSONObject(0);

                    // Get the Weather Day details first
                    // Get the temp details first

                    JSONObject dayJSON = dayWeatherJSON.getJSONObject("day");

                    double maxtemp_c = dayJSON.getDouble("maxtemp_c");
                    double maxtemp_f = dayJSON.getDouble("maxtemp_f");
                    double mintemp_c = dayJSON.getDouble("mintemp_c");
                    double mintemp_f = dayJSON.getDouble("mintemp_f");
                    double avgtemp_c = dayJSON.getDouble("avgtemp_c");
                    double avgtemp_f = dayJSON.getDouble("avgtemp_f");

                    WeatherTemp maxTemp = new WeatherTemp(maxtemp_c, maxtemp_f);
                    WeatherTemp minTemp = new WeatherTemp(mintemp_c, mintemp_f);
                    WeatherTemp avgTemp = new WeatherTemp(avgtemp_c, avgtemp_f);

                    // Get miscellaneous details
                    int avghumidity = dayJSON.getInt("avghumidity");
                    int uvIndex = dayJSON.getInt("uv");
                    double totalprecip_mm = dayJSON.getInt("totalprecip_mm");
                    double maxwind_kph = dayJSON.getDouble("maxwind_kph");
                    double maxwind_mph = dayJSON.getDouble("maxwind_mph");


                    WeatherMisc misc = new WeatherMisc(0, totalprecip_mm, avghumidity);

                    // Get weather conditions
                    JSONObject conditionJSON = dayJSON.getJSONObject("condition");

                    String conditionString = conditionJSON.getString("text");
                    String icon = conditionJSON.getString("icon");
                    int code = conditionJSON.getInt("code");

                    WeatherCondition condition =
                            new WeatherCondition(conditionString, icon, code, true);

                    WeatherDay dayObject =
                            new WeatherDay(maxTemp, minTemp, avgTemp, condition,
                                    misc, maxwind_kph, maxwind_mph, uvIndex);

                    // AstroElement object
                    JSONObject astro = dayWeatherJSON.getJSONObject("astro");

                    String sunrise = astro.getString("sunrise");
                    String sunset = astro.getString("sunset");
                    String moonrise = astro.getString("moonrise");
                    String moonset = astro.getString("moonset");
                    String moonphase = astro.getString("moon_phase");

                    AstroElement astroElement =
                            new AstroElement(sunrise, sunset, moonrise, moonset, moonphase);

                    // Instantiate collectionWeatherDay object
                    collectionWeatherDay = new CollectionWeatherDay(dayObject, astroElement);

                    // WeatherHour objects
                    JSONArray hours = dayWeatherJSON.getJSONArray("hour");

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
                        JSONObject hconditions = hourWeatherJSON.getJSONObject("condition");
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

                        collectionWeatherDay.addWeatherHour(i,
                                new WeatherHour(hourWeatherObject, time, chance_of_snow, chance_of_rain));
                    }

                    loadData();

                } catch (JSONException e) {
                    Log.e("WeatherDayFragment", "Failed to parse JSON object", e);
                }
            }).start();

            JsonObjectRequest requestDayWeather =
                    new JsonObjectRequest(
                            Request.Method.GET,
                            requestURL,
                            null,
                            dayListener,
                            errorListener
                    );

            // Add request to queue
            requestQueue.addRequest(requestDayWeather);

        }).start();

    }

    private void startLoadingAnimation() {
        if (loadingLayout != null) {

            getActivity().runOnUiThread(() -> {

                // Get image  view and start the animation
                ImageView loadingImage = loadingLayout.findViewById(R.id.loading_image);

                AnimatedVectorDrawable vectorDrawable = (AnimatedVectorDrawable)
                        ResourcesCompat.getDrawable(getResources(), R.drawable.avd_loading, null);

                if (vectorDrawable != null) {
                    loadingImage.setImageDrawable(vectorDrawable);
                    vectorDrawable.start();
                }
            });
        }
    }

    private void stopLoadingAnimation() {
        if (loadingLayout != null) {
            // Get image  view and start the animation
            ImageView loadingImage = loadingLayout.findViewById(R.id.loading_image);

            AnimatedVectorDrawable vectorDrawable =
                    (AnimatedVectorDrawable) loadingImage.getDrawable();

            if (vectorDrawable != null) {vectorDrawable.stop();}

            // Switch the loading view with the normal weather view
            SwitchFadeAnimation switchFadeAnimation = new SwitchFadeAnimation();
            switchFadeAnimation.switchViews(loadingLayout, layout, 4000L);
        }
    }

    // Load data into respective views
    public void loadData() {
        if (collectionWeatherDay != null && viewUp != null && astroView != null) {

            getActivity().runOnUiThread(() -> {
                // Load the weather hours fragment
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.day_weather_hours, WeatherHoursFragment.newInstance(collectionWeatherDay.getHours()))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

                // Populate the dateView first
                dateView.setDate(day);

                // Load astronomy elements
                AstroElement astroElement = collectionWeatherDay.getAstronomy();

                astroView.setMoonRise(astroElement.getMoonRise());
                astroView.setMoonSet(astroElement.getMoonSet());
                astroView.setSunRise(astroElement.getSunRise());
                astroView.setSunSet(astroElement.getSunSet());

                // Populate viewUp
                WeatherDay day = collectionWeatherDay.getDay();

                String conditionText = day.getConditions().getConditionText();

                viewUp.setAvgTemp(day.getAvgTemp().getTempC());
                viewUp.setDayUVIndex(day.getUvLevel());
                viewUp.setMaxAndMinTemp(day.getMaxTemp().getTempC(),
                        day.getMinTemp().getTempC());
                viewUp.setConditionText(conditionText);

                day.getConditions().loadConditionImage(viewUp.getDayWeatherIcon());

                // Stop loading animation and show the weather view
                stopLoadingAnimation();
            });

        }
    }
}