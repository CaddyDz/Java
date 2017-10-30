package com.englishdz.salim.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context context, Intent intent) {

        createNotification(context, "Times Up", "5 Seconds Has Passed", "Alert");

    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert) {

        // Define an Intent and an action to perform with it by another application
        PendingIntent notifIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        // Builds a notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.toy2).setContentTitle(msg).setTicker(msgAlert).setContentText(msgText);

        // Defines the Intent to fire when the notification is clicked
        mBuilder.setContentIntent(notifIntent);

        // Set the default notification option
        // DEFAULT_SOUND : Make sound
        // DEFAULT_VIBRATE : Vibrate
        // DEFAULT_LIGHTS : Use the default light notification
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        // Auto cancels the notification when clicked on in the task bar
        mBuilder.setAutoCancel(true);

        // Gets a NotificationManager which is used to notify the user of the background event
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Post the notification
        notificationManager.notify(1, mBuilder.build());

    }
}
