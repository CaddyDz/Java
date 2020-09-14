package com.marsool.firetool;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marsool.firetool.utils.networking.ApiCall;
import com.marsool.firetool.utils.networking.Handler;
import com.marsool.firetool.utils.networking.HttpResponse;
import com.marsool.firetool.utils.networking.Param;
import com.marsool.firetool.utils.networking.URLs;

public class MainActivity extends Activity {
    //login views
    private EditText editTextUsername, editTextPassword;
    private Button login;
    private ProgressBar buttonLoading;
    private TextView message;
    //popup Views
    private View overlay;
    private View popup;
    private View popupTit;
    private View popupTop;
    private View popupSep;
    private View popupBack;
    private View popupLoading;
    boolean popupOpen = false;
    private int loadedPopup = -1;
    private int animDur = 300;
    private int popup_size;
    private int popup_padding;

    private boolean ignoreChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Settings.st = false;
        //initialize popup fields
        popup_size = (int) getResources().getDimension(R.dimen.popup_size);
        popup_padding = (int) getResources().getDimension(R.dimen.popup_padding);
        overlay = findViewById(R.id.overlay);
        popupTit = findViewById(R.id.popup_tit);
        popupTop = findViewById(R.id.popup_top);
        popupSep = findViewById(R.id.popup_sep);
        popupLoading = findViewById(R.id.popup_loading);
        popup = findViewById(R.id.popup);
        popupBack = findViewById(R.id.popup_back);
        popupLoading.setClickable(false);
        popup.setClickable(true);

        //initialize login fields
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.buttonLogin);
        buttonLoading = findViewById(R.id.button_loading);
        message = findViewById(R.id.message);
        preparePhoneField();

        if (isNetworkConnected()) {
            //check whether the user is logged in
            if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                finish();
            } else {
                overlay.setOnClickListener(e -> hidePopup());
                popupBack.setOnClickListener(e -> hidePopup());
                overlay.setClickable(false);
                login.setOnClickListener(e -> userLogin());
            }
        } else {
            //if no internet app won't work
            Intent intent = new Intent(MainActivity.this, NoInternet.class);
            startActivity(intent);
        }
    }

    //adding event listener on the phone number to emphasise the rules
    public void preparePhoneField() {
        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int s, int before, int count) {
                if (ignoreChange) {
                    return;
                }
                int caretPos = editTextUsername.getSelectionStart();
                CharSequence text = charSequence;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    if ((i == 0 && (Character.isDigit(c) || c == '+')) || (i > 0 && Character.isDigit(c))) {
                        sb.append(c);
                    }
                }
                ignoreChange = true;
                String res = sb.toString();
                editTextUsername.setText(res);
                editTextUsername.setSelection(Math.min(caretPos, res.length()));
                ignoreChange = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your phone number");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine
        hideError();
        loading();

        ApiCall loginCall = new ApiCall(URLs.LOGIN,
                new Handler() {
                    @Override
                    public void handleResponse(HttpResponse response) {
                        if(response.getCode() == 403) {
                            showError("You're already logged somewhere else");
                        }else if(response.getCode() == 422) {
                            showError("Incorrect phone and/or password");
                        }
                        loaded();
                    }

                    @Override
                    public void handleError(String text) {
                        runOnUiThread(()-> {
                            showError(text);
                            loaded();
                        });
                    }
                },
                new Param("phone", username),
                new Param("password", password),
                new Param("device_name", "test"));
        loginCall.execute();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onBackPressed() {
        if (popupOpen) {
            hidePopup();
            return;
        }
        finish();
        startActivity(getIntent());
        super.onBackPressed();
    }

    public void loadPopupContent(View v, String title, int id) {
        if (loadedPopup == id) {
            return;
        }
        ((LinearLayout) popup).removeAllViews();
        ((TextView) popupTit).setText(title);
        ((LinearLayout) popup).addView(popupTop);
        ((LinearLayout) popup).addView(popupSep);
        ((LinearLayout) popup).addView(popupLoading);


        ((LinearLayout) popup).removeView(popupLoading);
        ((LinearLayout) popup).addView(v);
        loadedPopup = id;

    }

    public void showError(String error) {
        message.setText(error);
        ObjectAnimator show = ObjectAnimator.ofFloat(message, "alpha", 1f);
        show.setDuration(animDur);
        show.start();
    }

    public void hideError() {
        ObjectAnimator hide = ObjectAnimator.ofFloat(message, "alpha", 0f);
        hide.setDuration(animDur);
        hide.start();
    }

    public void loading() {
        ObjectAnimator button_hide = ObjectAnimator.ofFloat(login, "alpha", 0f);
        button_hide.setDuration(animDur);
        ObjectAnimator loading_show = ObjectAnimator.ofFloat(buttonLoading, "alpha", 1f);
        loading_show.setDuration(animDur);

        button_hide.start();
        loading_show.start();
    }

    public void loaded() {
        ObjectAnimator button_show = ObjectAnimator.ofFloat(login, "alpha", 1f);
        button_show.setDuration(animDur);
        ObjectAnimator loading_hide = ObjectAnimator.ofFloat(buttonLoading, "alpha", 0f);
        loading_hide.setDuration(animDur);

        button_show.start();
        loading_hide.start();
    }

    public void showPopup() {
        ObjectAnimator fade_animation = ObjectAnimator.ofFloat(overlay, "alpha", .5f);
        fade_animation.setDuration(animDur);
        ObjectAnimator slide_animation = ObjectAnimator.ofFloat(popup, "translationY", 0f);
        slide_animation.setDuration(animDur);
        popupOpen = true;
        overlay.setClickable(true);
        slide_animation.start();
        fade_animation.start();
    }

    public void hidePopup() {
        ObjectAnimator fade_animation = ObjectAnimator.ofFloat(overlay, "alpha", 0f);
        fade_animation.setDuration(animDur);
        ObjectAnimator slide_animation = ObjectAnimator.ofFloat(popup, "translationY", popup_size);
        slide_animation.setDuration(animDur);
        popupOpen = false;
        overlay.setClickable(false);
        slide_animation.start();
        fade_animation.start();
    }
}