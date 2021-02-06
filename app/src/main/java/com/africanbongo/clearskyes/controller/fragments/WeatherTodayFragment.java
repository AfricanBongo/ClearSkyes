package com.africanbongo.clearskyes.controller.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.MainActivity;
import com.africanbongo.clearskyes.controller.animations.LoadingLayoutAnimation;
import com.africanbongo.clearskyes.controller.customviews.AstroView;
import com.africanbongo.clearskyes.controller.customviews.CurrentWeatherViewUp;
import com.africanbongo.clearskyes.model.weather.AstroElement;
import com.africanbongo.clearskyes.model.weather.WeatherHour;
import com.africanbongo.clearskyes.model.weather.WeatherTemp;
import com.africanbongo.clearskyes.model.weather.WeatherToday;
import com.africanbongo.clearskyes.model.weatherapi.ErrorPageListener;
import com.africanbongo.clearskyes.model.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.util.BackgroundTaskUtil;
import com.africanbongo.clearskyes.util.LocationUtil;
import com.africanbongo.clearskyes.util.WeatherJsonUtil;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

import static android.view.View.GONE;
import static com.africanbongo.clearskyes.util.WeatherTimeUtil.getCurrentHourAsIndex;

/*
Fragment containing today's weather
 */
public class WeatherTodayFragment extends Fragment {

    private LoadingLayoutAnimation loadingLayoutAnimation;
    private CurrentWeatherViewUp viewUp;
    private AstroView astroView;

    /**
     * Returns a new {@link WeatherTodayFragment}
     * @param location {@link String} URL location used in fetching data
     * @param degree {@link com.africanbongo.clearskyes.model.weather.WeatherTemp.Degree} to be used, eg. Celsius
     * @return
     */
    public static WeatherTodayFragment newInstance(String location, WeatherTemp.Degree degree) {
        Bundle bundle = new Bundle();
        bundle.putString(WeatherTemp.Degree.class.getSimpleName(), degree.getStringDegree());
        bundle.putString(LocationUtil.class.getSimpleName(), location);
        WeatherTodayFragment f = new WeatherTodayFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_today, container, false);

        // Fetch views
        RelativeLayout layout = view.findViewById(R.id.today_layout);
        layout.setVisibility(GONE);

        // Fetch loading page
        RelativeLayout loadingLayout = view.findViewById(R.id.loading_anim);
        loadingLayoutAnimation =
                new LoadingLayoutAnimation(loadingLayout, layout);

        viewUp = view.findViewById(R.id.now_weatherview_up);
        astroView = view.findViewById(R.id.now_weatherview_down);

        Bundle bundle = getArguments();

        if (bundle != null) {
            // Get from the settings which types of values to load up
            String location = bundle.getString(LocationUtil.class.getSimpleName());
            String degreesType = bundle.getString(WeatherTemp.Degree.class.getSimpleName());
            WeatherTemp.Degree degree = WeatherTemp.Degree.getDegree(degreesType);
            requestData(degree, location);
        }
        return view;
    }

    // Fetch the data needed from the api
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void requestData(WeatherTemp.Degree degreesType, String location) {
        String url = WeatherJsonUtil.generateURL(location, 1);
        WeatherRequestQueue requestQueue =
                WeatherRequestQueue.getWeatherRequestQueue(getContext());
        loadingLayoutAnimation.start();

        // Parse WeatherApi JSONObjects
        Response.Listener<JSONObject> currentListener = response -> {
            try {

                JSONObject forecast = response
                        .getJSONObject("forecast")
                        .getJSONArray("forecastday")
                        .getJSONObject(0);

                Callable<WeatherToday> getWeatherToday = () -> WeatherJsonUtil.parseIntoWeatherToday(response);
                Callable<AstroElement> getAstroElement = () -> WeatherJsonUtil.parseIntoAstroElement(forecast);
                Callable<WeatherHour[]> getWeatherHours = () -> WeatherJsonUtil.parseIntoWeatherHourArray(forecast);

                // Callbacks to load the objects into the UI components
                BackgroundTaskUtil.Callback<WeatherToday> consumeWeatherToday =
                        weatherDay -> viewUp.loadData(weatherDay, degreesType);

                BackgroundTaskUtil.Callback<AstroElement> consumeAstroElement =
                        astroElement -> astroView.loadData(astroElement);

                BackgroundTaskUtil.Callback<WeatherHour[]> consumeWeatherHours =
                        weatherHours -> {
                            getChildFragmentManager().beginTransaction()
                                    .replace(R.id.now_weather_hours, WeatherHoursFragment.newInstance(weatherHours, degreesType))
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();

                            // Load the chance of rain for the ViewUp
                            int currentChanceOfRainFall = weatherHours[getCurrentHourAsIndex()].getChanceOfRain();
                            viewUp.setChanceOfRain(currentChanceOfRainFall);

                            // Stop loading animation and show the weather view
                            loadingLayoutAnimation.stop();
                        };

                // Run the tasks on background threads and load the objects
                BackgroundTaskUtil.runAndUpdateUI(getWeatherToday, consumeWeatherToday);
                BackgroundTaskUtil.runAndUpdateUI(getAstroElement, consumeAstroElement);
                BackgroundTaskUtil.runAndUpdateUI(getWeatherHours, consumeWeatherHours);

            } catch (JSONException e) {
                Log.e("WeatherTodayFragment", "Failed to parse JSONObject");
                e.printStackTrace();
            }
        };

        // Shows error page if an error occurs whilst fetching data
        ErrorPageListener errorListener =
                new ErrorPageListener((MainActivity) getActivity(), loadingLayoutAnimation);

        // Create JSONObjectRequest
        JsonObjectRequest requestTodayWeather =
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        currentListener,
                        errorListener
                );

        // Add to request queue
        requestQueue.addRequest(requestTodayWeather);
    }
}