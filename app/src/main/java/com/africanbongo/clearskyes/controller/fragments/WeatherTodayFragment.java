package com.africanbongo.clearskyes.controller.fragments;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.MainActivity;
import com.africanbongo.clearskyes.controller.animations.SwitchFadeAnimation;
import com.africanbongo.clearskyes.controller.customviews.AstroView;
import com.africanbongo.clearskyes.controller.customviews.CurrentWeatherViewUp;
import com.africanbongo.clearskyes.controller.customviews.CustomDateView;
import com.africanbongo.clearskyes.model.util.WeatherJsonUtil;
import com.africanbongo.clearskyes.model.util.weatherapi.ErrorPageListener;
import com.africanbongo.clearskyes.model.util.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.model.weather.AstroElement;
import com.africanbongo.clearskyes.model.weather.WeatherCondition;
import com.africanbongo.clearskyes.model.weather.WeatherHour;
import com.africanbongo.clearskyes.model.weather.WeatherMisc;
import com.africanbongo.clearskyes.model.weather.WeatherObject;
import com.africanbongo.clearskyes.model.weather.WeatherTemp;
import com.africanbongo.clearskyes.model.weather.WeatherToday;
import com.africanbongo.clearskyes.model.weather.WeatherWind;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.view.View.GONE;
import static com.africanbongo.clearskyes.model.util.WeatherTimeUtil.getCurrentHourAsIndex;
import static com.africanbongo.clearskyes.model.util.WeatherTimeUtil.getRelativeDay;

/*
Fragment containing today's weather
 */
public class WeatherTodayFragment extends Fragment {

    private static ErrorPageListener errorPageListener;

    private WeatherToday today = null;

    private RelativeLayout loadingLayout;
    private RelativeLayout layout;
    private CurrentWeatherViewUp viewUp;
    private AstroView astroView;

    private static final String FRAGMENT_NAME = "WeatherTodayFragment";
    private final String todayDate;
    private final String location;


    public WeatherTodayFragment(String location) {
        todayDate = getRelativeDay(LocalDate.now());
        this.location = location;
    }

    public static WeatherTodayFragment newInstance(MainActivity activity, String location) {
        errorPageListener = new ErrorPageListener(activity);
        return new WeatherTodayFragment(location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_today, container, false);

        // Fetch views
        layout = view.findViewById(R.id.today_layout);
        layout.setVisibility(GONE);

        // Fetch loading page
        loadingLayout = view.findViewById(R.id.loading_anim);

        CustomDateView dateView = view.findViewById(R.id.today_date_view);
        dateView.setDate(todayDate);

        viewUp = view.findViewById(R.id.now_weatherview_up);
        astroView = view.findViewById(R.id.now_weatherview_down);

        requestData();
        return view;
    }


    /**
     * Generate the url used to get data from the Weather API
     * @param location Titled {@link String} used to grab data for a specific location eg. Harare
     * @return {@link String} url
     */
    public String generateURL(String location) {
        return "https://api.weatherapi.com/v1/forecast.json?key=" +
                WeatherRequestQueue.API_KEY +"&q=" + location + "&days=1";
    }

    // Main Activity is used to display error page in the event of a failure
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void requestData() {

        new Thread(() -> {

            String url = generateURL(location);

            WeatherRequestQueue requestQueue =
                    WeatherRequestQueue.getWeatherRequestQueue(getContext());

            // Parse WeatherApi JSONObjects
            Response.Listener<JSONObject> currentListener = (JSONObject response) -> new Thread(() -> {
                try {

                    // Only start this animation when data has been successfully received
                    // from the API
                    startLoadingAnimation();

                    ExecutorService weatherExecutorService = Executors.newFixedThreadPool(3);

                    weatherExecutorService.submit(() -> {
                        try {
                            loadViewUp(WeatherJsonUtil.parseIntoWeatherToday(response));
                        } catch (JSONException e) {
                            Log.e(FRAGMENT_NAME, e.getMessage(), e);
                        }
                    });

                    JSONObject forecast = response
                            .getJSONObject("forecast")
                            .getJSONArray("forecastday")
                            .getJSONObject(0);

                    weatherExecutorService.submit(() -> {
                        try {
                            loadAstroView(WeatherJsonUtil.parseIntoAstroElement(forecast));
                        } catch (JSONException e) {
                            Log.e(FRAGMENT_NAME, e.getMessage(), e);
                        }
                    });

                    weatherExecutorService.submit(() -> {
                        try {
                            loadWeatherHours(WeatherJsonUtil.parseIntoWeatherHourArray(forecast));
                        } catch (JSONException e) {
                            Log.e(FRAGMENT_NAME, e.getMessage(), e);
                        }
                    });

                    weatherExecutorService.shutdown();

                } catch (JSONException e) {
                    Log.e("WeatherTodayFragment", "Failed to parse JSONObject");
                    e.printStackTrace();
                }
            }).start();


            // Create JSONObjectRequest
            JsonObjectRequest requestTodayWeather =
                    new JsonObjectRequest(Request.Method.GET,
                            url, null, currentListener,
                            errorPageListener);

            // Add to request queue
            requestQueue.addRequest(requestTodayWeather);
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
            switchFadeAnimation.switchViews(loadingLayout, layout, 2500L);
        }
    }

    public void loadWeatherHours(@NonNull WeatherHour[] weatherHours) {
        getActivity().runOnUiThread(() -> {
            // Load the weather hours fragment
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.now_weather_hours, WeatherHoursFragment.newInstance(weatherHours))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            // Load the chance of rain for the ViewUp
            int currentChanceOfRainFall = weatherHours[getCurrentHourAsIndex()].getChanceOfRain();

            viewUp.setChanceOfRain(currentChanceOfRainFall);
            // Stop loading animation and show the weather view
            stopLoadingAnimation();
        });
    }
    public void loadAstroView(@NonNull AstroElement astroElement) {
        // Load astronomy elements
        getActivity().runOnUiThread(() -> astroView.loadData(astroElement));
    }
    // Load the data into the views
    public void loadViewUp(@NonNull WeatherToday today) {
        getActivity().runOnUiThread(() -> viewUp.loadData(today));
    }
}