package com.africanbongo.clearskyes.controller.fragments;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.MainActivity;
import com.africanbongo.clearskyes.controller.animations.SwitchFadeAnimation;
import com.africanbongo.clearskyes.controller.customviews.AstroView;
import com.africanbongo.clearskyes.controller.customviews.CustomDateView;
import com.africanbongo.clearskyes.controller.customviews.DayWeatherViewUp;
import com.africanbongo.clearskyes.model.weatherapi.util.WeatherJsonUtil;
import com.africanbongo.clearskyes.model.weatherapi.util.WeatherTimeUtil;
import com.africanbongo.clearskyes.model.weatherapi.ErrorPageListener;
import com.africanbongo.clearskyes.model.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.model.weather.AstroElement;
import com.africanbongo.clearskyes.model.weather.WeatherDay;
import com.africanbongo.clearskyes.model.weather.WeatherHour;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.view.View.GONE;

public class WeatherDayFragment extends Fragment {

    // Error Listener for fetching Weather JSON data
    private static ErrorPageListener errorListener;

    private RelativeLayout loadingLayout;
    private RelativeLayout layout;
    private CustomDateView dateView;
    private DayWeatherViewUp viewUp;
    private AstroView astroView;

    private String location;

    private static final String FRAGMENT_NAME = "WeatherDayFragment";

    // The day of the week to display
    String day;
    int daysOffset;

    public WeatherDayFragment(String location) {
        this.location = location;
    }

    public static WeatherDayFragment newInstance(MainActivity activity, String location, int daysOffset) {

        // Set up error listener first
        errorListener = new ErrorPageListener(activity);

        Bundle args = new Bundle();
        args.putInt("daysOffset", daysOffset);

        WeatherDayFragment fragment = new WeatherDayFragment(location);
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
                    DateTimeFormatter.ofPattern(WeatherTimeUtil.DATE_FORMAT);

            LocalDate localDate = LocalDate.now().plusDays(daysOffset);
            day = WeatherTimeUtil.getRelativeDay(localDate);

            requestData(dateTimeFormatter.format(localDate));
        }

        return view;
    }


    public String generateURL(String location, String date) {
        return "https://api.weatherapi.com/v1/forecast.json?key=" +
                WeatherRequestQueue.API_KEY + "&q=" + location +"&dt=" + date;
    }

    public void requestData(String date) {
        new Thread(() -> {

            String requestURL = generateURL(location, date);

            WeatherRequestQueue requestQueue = WeatherRequestQueue
                    .getWeatherRequestQueue(getContext());

            // Listener for JSON Data
            Response.Listener<JSONObject> dayListener = response -> new Thread(() -> {
                try {

                    // Only start this animation when data has been successfully received
                    // from the API
                    startLoadingAnimation();

                    JSONObject json = response
                            .getJSONObject("forecast")
                            .getJSONArray("forecastday")
                            .getJSONObject(0);

                    // Get the Weather Day details first
                    ExecutorService weatherExecutorService = Executors.newFixedThreadPool(3);

                    weatherExecutorService.submit(() -> {
                        try {
                            loadViewUp(WeatherJsonUtil.parseIntoWeatherDay(json));
                        } catch (JSONException e) {
                            Log.e(FRAGMENT_NAME, e.getMessage(), e);
                        }
                    });


                    // Parse and load astronomy information
                    weatherExecutorService.submit(() -> {
                        try {
                            loadAstroView(WeatherJsonUtil.parseIntoAstroElement(json));
                        } catch (JSONException e) {
                            Log.e(FRAGMENT_NAME, e.getMessage(), e);
                        }
                    });

                    // Parse and load weather hours information
                    weatherExecutorService.submit(() -> {
                       try {
                           loadWeatherHours(WeatherJsonUtil.parseIntoWeatherHourArray(json));
                       } catch (JSONException e) {
                           Log.e(FRAGMENT_NAME, e.getMessage(), e);
                       }
                    });

                    weatherExecutorService.shutdown();

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

    // Load data into ViewUp
    public void loadViewUp(@NonNull WeatherDay weatherDay) {
        getActivity().runOnUiThread(() -> {
            // Populate the dateView first
            dateView.setDate(day);
            // Populate viewUp
            viewUp.loadData(weatherDay);
        });
    }

    // Load data into AstroView
    public void loadAstroView(@NonNull AstroElement astroElement) {
        getActivity().runOnUiThread(() -> astroView.loadData(astroElement));
    }

    // Load data into
    public void loadWeatherHours(@NonNull WeatherHour[] hours) {
        getActivity().runOnUiThread(() -> {
            // Load the weather hours fragment
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.day_weather_hours, WeatherHoursFragment.newInstance(hours))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            // Stop loading animation and show the weather view
            stopLoadingAnimation();
        });
    }
}