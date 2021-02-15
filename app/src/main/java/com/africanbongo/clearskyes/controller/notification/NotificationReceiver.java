package com.africanbongo.clearskyes.controller.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.africanbongo.clearskyes.model.WeatherDay;
import com.africanbongo.clearskyes.model.WeatherLocation;
import com.africanbongo.clearskyes.model.WeatherTemp;
import com.africanbongo.clearskyes.weatherapi.WeatherRequestQueue;
import com.africanbongo.clearskyes.util.BackgroundTaskUtil;
import com.africanbongo.clearskyes.util.NotificationUtil;
import com.africanbongo.clearskyes.util.WeatherJsonUtil;
import com.africanbongo.clearskyes.util.WeatherLocationUtil;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        BackgroundTaskUtil.runTask(() -> {
            if (intent != null) {
                String action = intent.getAction();

                if (action != null) {
                    if (intent.getAction().equals(NotificationUtil.NOTIFICATION_ACTION)) {
                        // Get the location from the intent
                        String favouriteLocation = intent.getStringExtra(NotificationUtil.NOTIFICATION_LOCATION);

                        // If location doesn't exist do not do anything
                        if (favouriteLocation != null) {

                            NotificationManager manager = NotificationUtil.assembleManager(context);
                            WeatherLocation weatherLocation = WeatherLocationUtil.deserialize(favouriteLocation);
                            String degreeString = intent.getStringExtra(NotificationUtil.NOTIFICATION_DEGREE);
                            final WeatherTemp.Degree degree;

                            if (degreeString == null) {
                                degree = WeatherTemp.Degree.C;
                            } else {
                                degree = WeatherTemp.Degree.getDegree(degreeString);
                            }
                            // Continue if the network is available
                            if (WeatherRequestQueue
                                    .getWeatherRequestQueue(context)
                                    .isNetworkAvailable()) {


                                Response.Listener<JSONObject> weatherListener = response -> {

                                    BackgroundTaskUtil.runTask(() -> {
                                        try {

                                            JSONObject json = response
                                                    .getJSONObject("forecast")
                                                    .getJSONArray("forecastday")
                                                    .getJSONObject(0);

                                            // Parse the weather data
                                            WeatherDay today = WeatherJsonUtil.parseIntoWeatherDay(json);

                                            // Get builder and send the notification
                                            NotificationCompat.Builder builder = NotificationUtil
                                                    .getNotificationBuilder(context, today, weatherLocation, degree);
                                            Notification notification = builder.build();
                                            manager.notify(NotificationUtil.NOTIFICATION_ID, notification);
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
                        }
                    }
                }
            }
        });
    }

}