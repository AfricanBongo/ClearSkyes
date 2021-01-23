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

                        today.addHour(i,
                                new WeatherHour(hourWeatherObject, time, chance_of_snow, chance_of_rain));
                    }

                    loadData();
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
    // Load the data into the views
    public void loadData() {
        if (today != null && viewUp != null && astroView != null) {
            getActivity().runOnUiThread(() -> {
                // Load the weather hours fragment
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.now_weather_hours, WeatherHoursFragment.newInstance(today.getWeatherHours()))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

                // Load ViewUp elements
                viewUp.setUvIndex(today.getUvLevel());
                viewUp.setFeelsLikeTemp((int) today.getNowWeather().getFeelsLikeTemp().getTempC());
                viewUp.setNowTemp((int) today.getNowWeather().getActualTemp().getTempC());

                String conditionText = today.getNowWeather().getConditions().getConditionText();

                viewUp.setConditionText(conditionText);
                viewUp.setChanceOfRain(today.getHour(getCurrentHourAsIndex()).getChanceOfRain());

                // Load weather icon
                today.getNowWeather().getConditions().loadConditionImage(viewUp.getIconImageView());
                viewUp.getIconImageView().setContentDescription(conditionText);

                // Load astronomy elements
                AstroElement astroElement = today.getAstronomy();

                astroView.setMoonRise(astroElement.getMoonRise());
                astroView.setMoonSet(astroElement.getMoonSet());
                astroView.setSunRise(astroElement.getSunRise());
                astroView.setSunSet(astroElement.getSunSet());

                // Stop loading animation and show the weather view
                stopLoadingAnimation();
            });
        }
    }
}