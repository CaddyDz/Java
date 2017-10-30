package com.englishdz.salim.notifications;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

// NotificationManager : Allows us to notify the user that something happened in the background
// AlarmManager : Allows you to schedule for your application to do something at a later date even if it is in the background

public class MainActivity extends AppCompatActivity {

    Button showNotificationBtn, stopNotificationBtn, alertBtn;

    // Allows us to notify the user that something happened in the background
    NotificationManager notificationManager;

    // Used to track the notifications
    int notifID = 3301;

    // Used to track if notification is active in the task bar
    boolean isNotifActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        showNotificationBtn = (Button) findViewById(R.id.showNotificationBut);
        stopNotificationBtn = (Button) findViewById(R.id.stopNotificationBut);
        alertBtn = (Button) findViewById(R.id.alertButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showNotification(View view) {

        // Builds a notification
        NotificationCompat.Builder notifBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this).setContentTitle("Heya").setContentText("What's up?").setTicker("Ping").setSmallIcon(R.drawable.toy2);

        // Define that we have the intention of opening MoreInfoNotification
        Intent moreInfoIntent = new Intent(this, MoreInfoNotification.class);

        // Used to stack tasks across activities so we go to the proper place when back
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);

        // Add all parents of this activity to the stack
        taskStackBuilder.addParentStack(MoreInfoNotification.class);

        // Add our new Intent to the stack
        taskStackBuilder.addNextIntent(moreInfoIntent);

        // Define an Intent and an action to perform with it by another application
        // FLAG_UPDATE_CURRENT : If the intent exists keep it but update it if needed
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Defines the Intent to fire when the notification is clicked
        notifBuilder.setContentIntent(pendingIntent);

        // Gets a NotificationManager which is used to notify the user of the background event
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Pot teh notification
        notificationManager.notify(notifID, notifBuilder.build());

        // Used so that we can't stop a notification that has already been stopped
        isNotifActive = true;


    }

    public void stopNotification(View view) {

        // If the notification is still active, close it
        if (isNotifActive) {
            notificationManager.cancel(notifID);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAlarm(View view) {

        // Define a time value of 5 seconds
        Long alertTime = new GregorianCalendar().getTimeInMillis()+5*1000;

        // Define our intention of executing AlertReceiver
        Intent alertIntent = new Intent(this, AlertReceiver.class);

        // Allows you to schedule for your application to do something at a later date even if it is in the background or isn't active
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // set() schedules an alarm to trigger
        // Trigger for alertIntent to fire in 5 seconds
        // FLAG_UPDATE_CURRENT : Update the Intent if active
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));

    }
}
