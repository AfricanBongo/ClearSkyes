package com.africanbongo.clearskyes.controller.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.africanbongo.clearskyes.model.weather.WeatherLocation;
import com.africanbongo.clearskyes.model.weather.WeatherToday;
import com.africanbongo.clearskyes.model.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.util.LocationUtil;
import com.africanbongo.clearskyes.util.NotificationUtil;
import com.africanbongo.clearskyes.util.WeatherJsonUtil;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();

            if (action != null) {
                // Get the location from the SharedPreferences

                Log.e("Service", "STARTED SERVICE");
                if (intent.getAction().equals(NotificationUtil.NOTIFICATION_ACTION)) {
                    SharedPreferences preferences =
                            PreferenceManager.getDefaultSharedPreferences(context);

                    String favouriteLocation = preferences.getString(LocationUtil.SP_FAV_LOCATION, null);
                    NotificationManager manager = NotificationUtil.assembleManager(context);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();

                    // If location doesn't exist do not do anything
                    if (favouriteLocation != null) {

                        executorService.execute(() -> {
                            WeatherLocation weatherLocation = LocationUtil.deserialize(favouriteLocation);

                            // Continue if the network is available
                            if (WeatherRequestQueue
                                    .getWeatherRequestQueue(context)
                                    .isNetworkAvailable()) {


                                Response.Listener<JSONObject> weatherListener = response -> {

                                    executorService.submit(() -> {
                                        try {
                                            // Parse the weather data
                                            WeatherToday today = WeatherJsonUtil.parseIntoWeatherToday(response);

                                            // Get builder and send the notification
                                            NotificationCompat.Builder builder =
                                                    NotificationUtil.getNotificationBuilder(today, context);
                                            Notification notification = builder.build();
                                            manager.notify(NotificationUtil.NOTIFICATION_ID, notification);

                                            // Shutdown the executor service
                                            executorService.shutdown();
                                        } catch (JSONException e) {
                                            Log.e(getClass().getSimpleName(), "Error parsing weather data for notifications", e);
                                        }
                                    });


                                };

                                Response.ErrorListener errorListener = error ->
                                        Log.e(
                                                getClass().getSimpleName(),
                                                "Failed to fetch weather data for notifications",
                                                error
                                        );

                                JsonObjectRequest weatherJSONRequest = new JsonObjectRequest(
                                        Request.Method.GET,
                                        WeatherJsonUtil.generateURL(weatherLocation.getUrlLocation(), 1),
                                        null,
                                        weatherListener,
                                        errorListener
                                );

                                WeatherRequestQueue
                                        .getWeatherRequestQueue(context)
                                        .addRequest(weatherJSONRequest);

                            } else {
                                NotificationUtil.cancelNotification(manager);
                            }
                        });

                    }
                }
            }
        }
    }
}