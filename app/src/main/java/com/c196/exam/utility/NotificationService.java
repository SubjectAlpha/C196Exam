package com.c196.exam.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationService extends BroadcastReceiver {
    public static final String CHANNEL_ID = "c196";
    public static final String CHANNEL_NAME = "C196 Notification";
    public static final String CHANNEL_DESC = "C196 Exam Notifications";

    public void scheduleNotification(Context c, AlarmManager a, long triggerTime, String title, String content) {
        Intent i = new Intent(c, this.getClass());
        i.putExtra("title", title);
        i.putExtra("content", content);
        PendingIntent pi = PendingIntent.getBroadcast(c, 0, i, PendingIntent.FLAG_IMMUTABLE);
        a.set(AlarmManager.RTC_WAKEUP, triggerTime, pi);
    }

    public void displayNotification(Context context, String title, String content) {
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mNotificationManager.notify(Integer.MAX_VALUE, notification.build());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        displayNotification(context, intent.getStringExtra("title"), intent.getStringExtra("content"));
    }
}
