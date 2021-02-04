package com.africanbongo.clearskyes.controller.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.MainActivity;
import com.africanbongo.clearskyes.controller.animations.LoadingLayoutAnimation;
import com.africanbongo.clearskyes.controller.customviews.AstroView;
import com.africanbongo.clearskyes.controller.customviews.DayWeatherViewUp;
import com.africanbongo.clearskyes.model.weather.AstroElement;
import com.africanbongo.clearskyes.model.weather.WeatherDay;
import com.africanbongo.clearskyes.model.weather.WeatherHour;
import com.africanbongo.clearskyes.model.weather.WeatherTemp;
import com.africanbongo.clearskyes.model.weatherapi.ErrorPageListener;
import com.africanbongo.clearskyes.model.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.util.AsyncUITaskUtil;
import com.africanbongo.clearskyes.util.LocationUtil;
import com.africanbongo.clearskyes.util.WeatherJsonUtil;
import com.africanbongo.clearskyes.util.WeatherTimeUtil;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;

import static android.view.View.GONE;

public class WeatherDayFragment extends Fragment {

    private LoadingLayoutAnimation loadingLayoutAnimation;
    private DayWeatherViewUp viewUp;
    private AstroView astroView;

    private final String FRAGMENT_NAME;

    public WeatherDayFragment() {
        FRAGMENT_NAME = this.getClass().getSimpleName();
    }

    public static WeatherDayFragment newInstance(String location, int daysOffset, WeatherTemp.Degree degree) {
        Bundle args = new Bundle();
        args.putInt("daysOffset", daysOffset);
        args.putString(WeatherTemp.Degree.class.getSimpleName(), degree.getStringDegree());
        args.putString(LocationUtil.class.getSimpleName(), location);

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
        RelativeLayout layout = view.findViewById(R.id.day_layout);
        layout.setVisibility(GONE);


        // Fetch loading page
        RelativeLayout loadingLayout = view.findViewById(R.id.day_loading_layout);
        loadingLayoutAnimation =
                new LoadingLayoutAnimation(getActivity(), loadingLayout, layout);

        viewUp = view.findViewById(R.id.day_weatherview_up);
        astroView = view.findViewById(R.id.day_astro_view);

        Bundle bundle = getArguments();

        if (bundle != null) {

            int daysOffset = bundle.getInt("daysOffset");
            String degreesType = bundle.getString(WeatherTemp.Degree.class.getSimpleName());
            String location = bundle.getString(LocationUtil.class.getSimpleName());

            // Get date string formatted so as to fetch JSON data
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern(WeatherTimeUtil.DATE_FORMAT);
            LocalDate localDate = LocalDate.now().plusDays(daysOffset);
            requestData(
                    dateTimeFormatter.format(localDate),
                    location,
                    WeatherTemp.Degree.getDegree(degreesType)
            );
        }

        return view;
    }


    public String generateURL(String location, String date) {
        return "https://api.weatherapi.com/v1/forecast.json?key=" +
                WeatherRequestQueue.API_KEY + "&q=" + location +"&dt=" + date;
    }

    public void requestData(String date, String location, WeatherTemp.Degree degree) {
        String requestURL = generateURL(location, date);

        WeatherRequestQueue requestQueue = WeatherRequestQueue
                .getWeatherRequestQueue(getContext());

        // Listener for JSON Data
        Response.Listener<JSONObject> dayListener = response -> {
            try {

                // Only start this animation when data has been successfully received
                // from the API
                loadingLayoutAnimation.start();

                JSONObject json = response
                        .getJSONObject("forecast")
                        .getJSONArray("forecastday")
                        .getJSONObject(0);


                Callable<WeatherDay> getWeatherDay = () -> WeatherJsonUtil.parseIntoWeatherDay(json);
                Callable<AstroElement> getAstroElement = () -> WeatherJsonUtil.parseIntoAstroElement(json);
                Callable<WeatherHour[]> getWeatherHours = () -> WeatherJsonUtil.parseIntoWeatherHourArray(json);

                // Callbacks to load the objects into the UI components
                AsyncUITaskUtil.Callback<WeatherDay> consumeWeatherDay =
                        weatherDay -> viewUp.loadData(weatherDay, degree);
                AsyncUITaskUtil.Callback<AstroElement> consumeAstroElement =
                        astroElement -> astroView.loadData(astroElement);
                AsyncUITaskUtil.Callback<WeatherHour[]> consumeWeatherHours =
                        weatherHours -> {
                            getChildFragmentManager().beginTransaction()
                                    .replace(R.id.day_weather_hours, WeatherHoursFragment.newInstance(weatherHours, degree))
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();

                            // Stop loading animation and show the weather view
                            loadingLayoutAnimation.stop();
                        };

                // Run the tasks on background threads and load the objects
                AsyncUITaskUtil.runOnBackgroundThread(getWeatherDay, consumeWeatherDay);
                AsyncUITaskUtil.runOnBackgroundThread(getAstroElement, consumeAstroElement);
                AsyncUITaskUtil.runOnBackgroundThread(getWeatherHours, consumeWeatherHours);

            } catch (JSONException e) {
                Log.e("WeatherDayFragment", "Failed to parse JSON object", e);
            }
        };

        JsonObjectRequest requestDayWeather =
                new JsonObjectRequest(
                        Request.Method.GET,
                        requestURL,
                        null,
                        dayListener,
                        new ErrorPageListener((MainActivity) getActivity())
                );

        // Add request to queue
        requestQueue.addRequest(requestDayWeather);

    }

    // Load data into
    public void loadWeatherHours(@NonNull WeatherHour[] hours, WeatherTemp.Degree degree) {
        getActivity().runOnUiThread(() -> {
            // Load the weather hours fragment
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.day_weather_hours, WeatherHoursFragment.newInstance(hours, degree))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            // Stop loading animation and show the weather view
            loadingLayoutAnimation.stop();
        });
    }
}