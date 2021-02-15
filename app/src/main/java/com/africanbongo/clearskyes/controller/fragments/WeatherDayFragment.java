package com.africanbongo.clearskyes.controller.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.MainActivity;
import com.africanbongo.clearskyes.controller.activities.WeatherDetailActivity;
import com.africanbongo.clearskyes.controller.animations.LoadingLayoutAnimation;
import com.africanbongo.clearskyes.controller.customviews.AstroView;
import com.africanbongo.clearskyes.controller.customviews.DayWeatherViewUp;
import com.africanbongo.clearskyes.model.AstroElement;
import com.africanbongo.clearskyes.model.WeatherDay;
import com.africanbongo.clearskyes.model.WeatherHour;
import com.africanbongo.clearskyes.model.WeatherTemp;
import com.africanbongo.clearskyes.weatherapi.ErrorPageListener;
import com.africanbongo.clearskyes.weatherapi.RefreshDataBroadcastReceiver;
import com.africanbongo.clearskyes.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.util.BackgroundTaskUtil;
import com.africanbongo.clearskyes.util.WeatherJsonUtil;
import com.africanbongo.clearskyes.util.WeatherLocationUtil;
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
        args.putString(WeatherLocationUtil.class.getSimpleName(), location);

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
                new LoadingLayoutAnimation(loadingLayout, layout);

        viewUp = view.findViewById(R.id.day_weatherview_up);
        astroView = view.findViewById(R.id.day_astro_view);

        Bundle bundle = getArguments();

        if (bundle != null) {

            int daysOffset = bundle.getInt("daysOffset");
            String degreesType = bundle.getString(WeatherTemp.Degree.class.getSimpleName());
            String location = bundle.getString(WeatherLocationUtil.class.getSimpleName());

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


    public void scheduleRefreshCache(String url) {
        SharedPreferences preferences
                = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Set auto-refresh data cache process
        String refreshDataCacheKey = getString(R.string.update_data_key);
        String refreshDataDefaultTime = getString(R.string.update_data_default_value);
        int refreshTime = Integer.parseInt(preferences.getString(refreshDataCacheKey, refreshDataDefaultTime));

        RefreshDataBroadcastReceiver.scheduleCacheRefresh(getContext(), url, refreshTime);
    }

    public String generateURL(String location, String date) {
        return "https://api.weatherapi.com/v1/forecast.json?key=" +
                WeatherRequestQueue.API_KEY + "&q=" + location +"&dt=" + date;
    }

    public void requestData(String date, String location, WeatherTemp.Degree degree) {
        String requestURL = generateURL(location, date);
        WeatherRequestQueue requestQueue = WeatherRequestQueue
                .getWeatherRequestQueue(getContext());
        loadingLayoutAnimation.start();

        scheduleRefreshCache(requestURL);

        // Listener for JSON Data
        Response.Listener<JSONObject> dayListener = response -> {
            try {

                JSONObject json = response
                        .getJSONObject("forecast")
                        .getJSONArray("forecastday")
                        .getJSONObject(0);

                Callable<WeatherDay> getWeatherDay = () -> WeatherJsonUtil.parseIntoWeatherDay(json);
                Callable<AstroElement> getAstroElement = () -> WeatherJsonUtil.parseIntoAstroElement(json);
                Callable<WeatherHour[]> getWeatherHours = () -> WeatherJsonUtil.parseIntoWeatherHourArray(json);

                // Callbacks to load the objects into the UI components
                BackgroundTaskUtil.Callback<WeatherDay> consumeWeatherDay =
                        weatherDay -> {
                    viewUp.loadData(weatherDay, degree);
                    // When the view is clicked open the WeatherDetailActivity
                    viewUp.setOnClickListener(l -> {
                        if (weatherDay != null) {
                            String imageTransitionName =
                                    getResources().getString(R.string.weather_detail_image_transition);
                            String tempTransitionName =
                                    getResources().getString(R.string.temp_text_transition);
                            String timeTransitionName =
                                    getResources().getString(R.string.time_text_transition);
                            String time = getResources().getString(R.string.summary);
                            Intent intent = new Intent(getContext(), WeatherDetailActivity.class);
                            intent.setAction(WeatherDay.INTENT_ACTION);
                            intent.putExtra(WeatherJsonUtil.INTENT_EXTRA, weatherDay);
                            intent.putExtra(WeatherTimeUtil.INTENT_EXTRA, time);
                            ActivityOptionsCompat optionsCompat =
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                                            getActivity(),
                                            Pair.create(viewUp.getWeatherImageView(), imageTransitionName),
                                            Pair.create(viewUp.getDayAvgTempView(), tempTransitionName),
                                            Pair.create(viewUp.getDayTextView(), timeTransitionName)
                                    );
                            ActivityCompat.startActivity(getContext(), intent, optionsCompat.toBundle());
                        }
                    });
                };
                BackgroundTaskUtil.Callback<AstroElement> consumeAstroElement =
                        astroElement -> astroView.loadData(astroElement);
                BackgroundTaskUtil.Callback<WeatherHour[]> consumeWeatherHours =
                        weatherHours -> {
                            getChildFragmentManager().beginTransaction()
                                    .replace(R.id.day_weather_hours, WeatherHoursFragment.newInstance(weatherHours, degree))
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();

                            // Stop loading animation and show the weather view
                            loadingLayoutAnimation.stop();
                        };

                // Run the tasks on background threads and load the objects
                BackgroundTaskUtil.runAndUpdateUI(getWeatherDay, consumeWeatherDay);
                BackgroundTaskUtil.runAndUpdateUI(getAstroElement, consumeAstroElement);
                BackgroundTaskUtil.runAndUpdateUI(getWeatherHours, consumeWeatherHours);

            } catch (JSONException e) {
                Log.e("WeatherDayFragment", "Failed to parse JSON object", e);
            }
        };

        // Shows error page if an error occurs whilst fetching data
        ErrorPageListener errorListener =
                new ErrorPageListener((MainActivity) getActivity(), loadingLayoutAnimation);

        // Create JSONObject request
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

    }
}