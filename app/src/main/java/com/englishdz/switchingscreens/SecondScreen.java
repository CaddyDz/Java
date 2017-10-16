package com.englishdz.switchingscreens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by GREY on 10/16/17.
 */

public class SecondScreen extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        Intent activityThatCalled = getIntent();

        Human caddy = (Human) activityThatCalled.getSerializableExtra("humanCaddy");

        TextView callingActivityMessage = (TextView) findViewById(R.id.calling_activity_info_text_view);
        callingActivityMessage.append(" " + caddy.getName() + " is " + caddy.getHeight() + " cm tall and weighs " + caddy.getWeight() + " KGs");
    }

    public void onSendUsersName(View view) {
        EditText usersNameET = (EditText) findViewById(R.id.users_name_edit_text);

        String usersName = String.valueOf(usersNameET.getText());

        Intent goingBack = new Intent();

        goingBack.putExtra("UsersName", usersName);

        setResult(RESULT_OK, goingBack);
        finish();
    }

}
