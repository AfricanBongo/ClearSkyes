package com.africanbongo.clearskyes.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.MainActivity;
import com.africanbongo.clearskyes.model.weather.WeatherCondition;
import com.africanbongo.clearskyes.model.weather.WeatherObject;
import com.africanbongo.clearskyes.model.weather.WeatherTemp;
import com.africanbongo.clearskyes.model.weather.WeatherToday;

/**
 * Class containing variables used in processing notifications
 */
public final class NotificationUtil {

    public static final int NOTIFICATION_ID = 3;
    public static final String NOTIFICATION_ACTION = "Notification";
    public static final String NOTIFICATION_NAME = "Weather Update Notification";
    public static final String NOTIFICATION_CHANNEL_ID = "clearskyes_notification_channel";
    public static final String NOTIFICATION_TIME = "notification_time";
    public static final int JOB_ID = 1001;

    private NotificationUtil() {}

    /**
     * Gets the {@link NotificationManager} and sets it up accordingly. Creating a {@link NotificationChannel},
     * if the internal {@link Build.VERSION} is Oreo or higher.
     * @param context {@link Context} used to fetch the {@link NotificationManager} from the system.
     * @return {@link NotificationManager} with initialized settings
     */
    public static NotificationManager assembleManager(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(
                            NOTIFICATION_CHANNEL_ID,
                            NOTIFICATION_NAME,
                            NotificationManager.IMPORTANCE_HIGH
                            );
            channel.enableLights(true);
            channel.setLightColor(context.getColor(R.color.purple_dark));
            channel.enableVibration(true);
            channel.setDescription(NOTIFICATION_NAME);
            notificationManager.createNotificationChannel(channel);
        }

        return notificationManager;
    }

    public static NotificationCompat.Builder getNotificationBuilder(@NonNull WeatherToday weatherToday,
                                                                    Context context) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent openAppIntent = PendingIntent.getActivity(
                context, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        WeatherObject weatherObject = weatherToday.getNowWeather();
        WeatherCondition weatherCondition = weatherObject.getConditions();

        // Fetch bitmap and load as large icon
        BackgroundTaskUtil.runAndUpdateUI(
                weatherCondition::getWeatherIcon,
                (BackgroundTaskUtil.Callback<Bitmap>) result -> {
                    synchronized (builder) {
                        builder.setLargeIcon(result);
                    }
                });

        WeatherTemp weatherTemp = weatherObject.getActualTemp();
        int tempC = (int) Math.round(weatherTemp.getTemp(WeatherTemp.Degree.C));
        int tempF = (int) Math.round(weatherTemp.getTemp(WeatherTemp.Degree.F));

        String contextTitle = "Today's weather is like: ";
        StringBuilder contextText = new StringBuilder();
        contextText.append(tempC).append("°C").append("\t\\\t")
                .append(tempF).append("°F").append("\t\t\t\t\t");
        contextText.append(weatherCondition.getConditionText());

        builder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(contextTitle)
                .setContentText(contextText)
                .setContentIntent(openAppIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        return builder;
    }

    public static void sendNotification(NotificationManager manager, NotificationCompat.Builder builder) {
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void cancelNotification(NotificationManager manager) {
        manager.cancel(NOTIFICATION_ID);
    }
}
