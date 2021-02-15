package com.africanbongo.clearskyes.weatherapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
/**
 {@link RequestQueue} for making requests for JSON to WeatherApi
 */
public class WeatherRequestQueue {
    private static WeatherRequestQueue weatherRequestQueue;
    private final RequestQueue requestQueue;
    private final Context context;

    // Used for making API requests
    public static final String API_KEY = "1092f3e10c9a4146890112852202212";


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

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * Check if the host device has an internet connection
     * @return True, if an internet connection exists
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
