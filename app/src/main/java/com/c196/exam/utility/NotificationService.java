package com.c196.exam.utility;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationService {
    public static final String CHANNEL_ID = "c196";
    public static final String CHANNEL_NAME = "C196 Notification";
    public static final String CHANNEL_DESC = "C196 Exam Notifications";

    public void displayNotification(Context context, String title, String content) {
        Notification notification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mNotificationManager.notify(1, notification);
    }
}
