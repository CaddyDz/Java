package com.englishdz.switchingscreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onGetNameClick(View view) {

        Human caddy = new Human(1.82, 66, "Caddy");

        Intent caddyIntent = new Intent(this, SecondScreen.class);

        caddyIntent.putExtra("humanCaddy", caddy);

        final int result = 1;

        startActivityForResult(caddyIntent, result);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView usersNameMessage = (TextView) findViewById(R.id.users_name_message);
        String nameSentBack = data.getStringExtra("UsersName");

        usersNameMessage.append(" " + nameSentBack);
    }
}
