package com.africanbongo.clearskyes.controller.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.africanbongo.clearskyes.util.NotificationUtil;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();

            if (action != null) {
                if (action.equals(NotificationUtil.NOTIFICATION_ACTION)) {
                    // TODO Write up notification code
                }
            }
        }
    }
}