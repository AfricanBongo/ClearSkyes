package com.africanbongo.clearskyes.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.africanbongo.clearskyes.R;
import com.africanbongo.clearskyes.controller.activities.MainActivity;
import com.africanbongo.clearskyes.model.weather.WeatherCondition;
import com.africanbongo.clearskyes.model.weather.WeatherDay;
import com.africanbongo.clearskyes.model.weather.WeatherLocation;
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
    public static final String NOTIFICATION_DEGREE = "degree";
    public static final String NOTIFICATION_LOCATION = "location";
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

    public static NotificationCompat.Builder getNotificationBuilder(Context context,
                                                                    @NonNull WeatherDay weatherToday,
                                                                    @NonNull WeatherLocation weatherLocation,
                                                                    @NonNull WeatherTemp.Degree degree) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent openAppIntent = PendingIntent.getActivity(
                context, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Get the weather info
        WeatherCondition weatherCondition = weatherToday.getConditions();
        WeatherTemp avgWeatherTemp = weatherToday.getAvgTemp();
        WeatherTemp maxWeatherTemp = weatherToday.getMaxTemp();
        WeatherTemp minWeatherTemp = weatherToday.getMinTemp();
        int avgTemp = avgWeatherTemp.getTemp(degree);
        int maxTemp = maxWeatherTemp.getTemp(degree);
        int minTemp = minWeatherTemp.getTemp(degree);

        NotificationCompat.BigTextStyle moreWeatherInfoStyle =
                new NotificationCompat.BigTextStyle();

        String summaryText = "Today's weather";
        String contextTitle = "Today, " + weatherLocation.getCity() + "'s weather is like: ";
        StringBuilder bigText = new StringBuilder();
        String contentText = weatherCondition.getConditionText();
        bigText.append("Average temperature: ").append(avgTemp).append(degree.getSymbol()).append("\n");
        bigText.append("Condition: ").append(weatherCondition.getConditionText()).append("\n");
        bigText.append("Max: ").append(maxTemp).append(degree.getSymbol()).append("\t|\t");
        bigText.append("Min: ").append(minTemp).append(degree.getSymbol()).append("\n");
        bigText.append("UV: ").append(weatherToday.getUvLevel());

        moreWeatherInfoStyle
                .setSummaryText(summaryText)
                .bigText(bigText);

        builder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(contextTitle)
                .setContentText(contentText)
                .setContentIntent(openAppIntent)
                .setStyle(moreWeatherInfoStyle)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        return builder;
    }

    public static void cancelNotification(NotificationManager manager) {
        manager.cancel(NOTIFICATION_ID);
    }
}
