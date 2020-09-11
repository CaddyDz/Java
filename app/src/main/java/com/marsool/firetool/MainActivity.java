package com.marsool.firetool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText editTextMobile;
    Button btnContinue;
    public static String text="+213";
    FirebaseUser currentUser;
    boolean click=true;
    EditText editTextUsername,editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Settings.st=false;
        final ImageButton country=(ImageButton) findViewById(R.id.country);
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               click=!click;
               if(click){ text="+213";
                   country.setImageResource(R.drawable.ksa);}
               else{ text="+20";
                   country.setImageResource(R.drawable.egypt);}
            }
        });

        //initialize fields
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        if(isNetworkConnected()) {

            //check whether the user is logged in
            if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                finish();
            } else {
                findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userLogin();
                    }
                });
            }

        }
        else {
            //if no internet app won't work
            Intent intent = new Intent(MainActivity.this, NoInternet.class);
            startActivity(intent);
        }
    }

    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String>  {




            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("phone"),
                                userJson.getBoolean("state")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", username);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onBackPressed() {
        finish();
        startActivity(getIntent());
        super.onBackPressed();
    }
}