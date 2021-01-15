package com.africanbongo.clearskyes.model.weatherapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
/**
 {@link RequestQueue} for making requests for JSON to WeatherApi
 */
public class WeatherRequestQueue {
    private static WeatherRequestQueue weatherRequestQueue;
    private final RequestQueue requestQueue;
    private final Context context;



    // Used for making API requests
    private static final String API_KEY = "1092f3e10c9a4146890112852202212";
    public static final String GET_CURRENT_WEATHER_START = "https://api.weatherapi.com/v1/forecast.json?key=" +
            API_KEY +"&q=Harare&days=1";
    public static final String GET_DAY_WEATHER_START = "https://api.weatherapi.com/v1/forecast.json?key=" +
            API_KEY + "&q=Harare&dt=";


    private WeatherRequestQueue(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Adds a VolleyRequest to the RequestQueue
     * @param request VolleyRequest to add to queue
     */
    public void addRequest(Request request) {
        requestQueue.add(request);
    }

    /**
     * Get WeatherRequestQueue object to access mundane RequestQueue methods
     * @param context Always use application context
     * @return {@link WeatherRequestQueue}
     */
    public static WeatherRequestQueue getWeatherRequestQueue(Context context) {
        if (weatherRequestQueue == null || context == null) {
            weatherRequestQueue = new WeatherRequestQueue(context);
        }
        return weatherRequestQueue;
    }

    /**
     * Creates a VolleyResponse ErrorListener for delivering error responses
     * @param tag Preferably class name where the error should be tagged to
     * @return {@link com.android.volley.Response.ErrorListener} in case of request errors
     */
    public Response.ErrorListener createGenericErrorListener(String tag) {
        return error -> Log.e(tag, error.getMessage());
    }

    /**
     *
     * @param tag Preferably class name where the error should be tagged to
     * @param errorMessage {@link String} to display as error message in the log
     * @return {@link com.android.volley.Response.ErrorListener} used for API requests
     */
    public Response.ErrorListener createGenericErrorListener(String tag, String errorMessage) {
        return error -> Log.e(tag, errorMessage);
    }

    /**
     * Check if the host device has an internet connection
     * @return True, if an internet connection exists
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
