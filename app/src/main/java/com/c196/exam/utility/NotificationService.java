package com.c196.exam.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.c196.exam.R;

public class NotificationService extends ContextWrapper {
    public static final String CHANNEL_ID = "c196";
    public static final String CHANNEL_NAME = "C196 Notification";
    public static final String CHANNEL_DESC = "C196 Exam Notifications";

    public NotificationService(Context base) {
        super(base);
        createChannel();
    }

    public void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(NotificationService.CHANNEL_DESC);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.createNotificationChannel(channel);
    }

    public void scheduleNotification(Context c, AlarmManager a, long triggerTime, String title, String content) {
        Intent i = new Intent(c, NotificationReceiver.class);
        i.putExtra("title", title);
        i.putExtra("content", content);
        PendingIntent pi = PendingIntent.getBroadcast(c, 100, i, PendingIntent.FLAG_IMMUTABLE);
        a.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pi);
    }

    public void displayNotification(Context context, String title, String content) {
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mNotificationManager.notify(100, notification.build());
    }
}
