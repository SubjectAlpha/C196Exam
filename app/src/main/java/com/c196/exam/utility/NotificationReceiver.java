package com.c196.exam.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationService ns = new NotificationService(context);
        ns.displayNotification(context, intent.getStringExtra("title"), intent.getStringExtra("content"));
    }
}
