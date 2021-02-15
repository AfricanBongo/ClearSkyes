package com.africanbongo.clearskyes.weatherapi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.africanbongo.clearskyes.util.BackgroundTaskUtil;
import com.africanbongo.clearskyes.util.WeatherTimeUtil;
import com.android.volley.RequestQueue;

import java.util.Calendar;

public class RefreshDataBroadcastReceiver extends BroadcastReceiver {

    public static final String INTENT_ACTION = "refresh_cache";
    public static final String REFRESH_URL = "refresh_cache_url";

    @Override
    public void onReceive(Context context, Intent intent) {
        BackgroundTaskUtil.runTask(() -> {
            if (intent.getAction().equals(INTENT_ACTION)) {
                String refreshURL = intent.getStringExtra(REFRESH_URL);

                if (refreshURL != null) {
                    int refreshTime = intent.getIntExtra(WeatherTimeUtil.REFRESH_EXTRA, 60);
                    WeatherRequestQueue
                            .getWeatherRequestQueue(context)
                            .getRequestQueue()
                            .getCache()
                            .invalidate(refreshURL, true);

                    // Start the refresh data cache process again
                    scheduleCacheRefresh(context, refreshURL, refreshTime);
                }
            }
        });
    }

    /**
     * Schedule for {@link RequestQueue} cache for the url to be refreshed at a certain time interval.
     * This method is executed asynchronously.
     * @param context {@link Context} of the application
     * @param refreshURL used to identify the url's cache to refresh
     * @param refreshTime interval period in minutes when the data should be refreshed
     */
    public static void scheduleCacheRefresh(Context context, String refreshURL, int refreshTime) {
        BackgroundTaskUtil.runTask(() -> {
            Intent intent = new Intent(context, RefreshDataBroadcastReceiver.class);
            intent.putExtra(WeatherTimeUtil.REFRESH_EXTRA, refreshTime);
            intent.putExtra(REFRESH_URL, refreshURL);
            intent.setAction(INTENT_ACTION);
            PendingIntent pendingIntent = PendingIntent
                    .getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, refreshTime);
            AlarmManager alarmManager =
                    (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        });
    }
}
