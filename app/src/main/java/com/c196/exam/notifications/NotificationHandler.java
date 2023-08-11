package com.c196.exam.notifications;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.c196.exam.R;

public class NotificationHandler {
    public final String CHANNEL_ID = "C196 Notification Channel";
    private void createNotificationChannel() {

        CharSequence name = "";
        String description = "";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }

}
