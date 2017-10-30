package com.englishdz.salim.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

// Called when the notification is clicked on in the task bar
public class MoreInfoNotification extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info_notif);
    }
}
