package com.c196.exam.utility;

import android.content.Context;

import androidx.core.app.NotificationCompat;

public class NotificationService {
    public static final String CHANNEL_ID = "c196";
    public static final String CHANNEL_NAME = "C196 Notification";
    public static final String CHANNEL_DESC = "C196 Exam Notifications";

    public void displayNotification(Context context, String title, String content){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
}
