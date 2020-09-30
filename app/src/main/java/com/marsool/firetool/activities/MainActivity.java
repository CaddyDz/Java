package com.marsool.firetool.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.LayoutDirection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.marsool.firetool.BuildConfig;
import com.marsool.firetool.R;
import com.marsool.firetool.SharedPrefManager;
import com.marsool.firetool.assets.AssetLoader;
import com.marsool.firetool.countrycodes.CountryCode;
import com.marsool.firetool.countrycodes.CountrySelect;
import com.marsool.firetool.networking.ApiCall;
import com.marsool.firetool.networking.ConnectivityCheck;
import com.marsool.firetool.networking.Handler;
import com.marsool.firetool.networking.HttpResponse;
import com.marsool.firetool.networking.Param;
import com.marsool.firetool.ui.Loading;
import com.marsool.firetool.ui.Popup;
import com.marsool.firetool.ui.alerts.Alert;
import com.marsool.firetool.ui.alerts.AlertType;
import com.marsool.firetool.ui.alerts.ButtonType;

import java.net.UnknownHostException;

public class MainActivity extends Activity {
    private SharedPrefManager spm;

    //Connectivity
    private View conRoot;
    private ProgressBar conProg;

    //login views
    private View content;
    private EditText editTextUsername, editTextPassword;
    private Button login;
    private ProgressBar buttonLoading;
    private TextView message;
    private int animDur = 300;
    //popup Views

    private boolean ignoreChange = false;
    private boolean connectivityCheckSkipped = false;

    private Popup countrySelect = null;

    Alert inf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Settings.st = false;
        String layoutDir = getResources().getString(R.string.layout_direction);
        System.out.println(layoutDir);
        int dir = -1;
        switch (layoutDir) {
            case "ltr":
                dir = LayoutDirection.LTR;
                break;
            case "rtl":
                dir = LayoutDirection.RTL;
                break;
        }
        findViewById(R.id.root).setLayoutDirection(dir);
        findViewById(R.id.editTextUsername).setLayoutDirection(dir);

        //initialize login fields
        conRoot = findViewById(R.id.connect);
        conProg = findViewById(R.id.connect_progress);

        //initialize login fields
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.buttonLogin);
        login.setOnClickListener(e -> userLogin());
        buttonLoading = findViewById(R.id.button_loading);
        message = findViewById(R.id.message);
        content = findViewById(R.id.content);
        content.setClickable(false);
        View info = findViewById(R.id.info);
        info.setOnClickListener(e -> {
            if (inf == null) {
                inf = new Alert(this, AlertType.INFORMATION);
                inf.setTitle(getResources().getString(R.string.about));
                inf.setMessage("FireTool© v" + BuildConfig.VERSION_NAME);
            }
            showAlert(inf);
        });
        preparePhoneField();

        spm = SharedPrefManager.getInstance(this);
        //check whether the user is logged in
        if (spm.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            finish();
        } else {
            boolean skip = false;
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                skip = extras.getBoolean("skip_connect");
            }
            connectivityCheckSkipped = skip;
            if (!skip) {
                ConnectivityCheck connectivityCheck = new ConnectivityCheck(this,
                        e -> conProg.setProgress(e),
                        this::hideConnect,
                        () -> {
                            Intent intent = new Intent(MainActivity.this, NoInternet.class);
                            startActivity(intent);
                        });
                connectivityCheck.execute();
            } else {
                hideConnect();
            }
        }

        CountryCode alg = new CountryCode("Algeria", "213", "DZ");

        new Thread() {
            public void run() {
                Bitmap bmp = alg.getFlag();
                runOnUiThread(() -> {
                    ((ImageView) findViewById(R.id.country_image)).setImageBitmap(bmp);
                    ((TextView) findViewById(R.id.country_code)).setText(alg.getDialPrefix());
                });
            }
        }.start();

        findViewById(R.id.country).setOnClickListener(e -> {
            if (countrySelect == null) {
                Loading loading = new Loading(MainActivity.this);
                loading.setTitle("please wait");
                loading.setMessage("Loading countries");
                showPopup(loading.getPopup());
                new Thread() {
                    public void run() {
                        countrySelect = new Popup(MainActivity.this, 400);
                        countrySelect.loadContent(new CountrySelect(MainActivity.this, c -> {
                            countrySelect.hide();
                            ((TextView) findViewById(R.id.country_code)).setText(c.getDialPrefix());
                            new Thread() {
                                public void run() {
                                    Bitmap bmp = c.getFlag();
                                    new android.os.Handler(Looper.getMainLooper()).post(() -> ((ImageView) findViewById(R.id.country_image)).setImageBitmap(bmp));
                                }
                            }.start();

                        }, 20, AssetLoader.loadCountryCodes(MainActivity.this)), getResources().getString(R.string.select_country));

                        runOnUiThread(() -> {
                            loading.hide();
                            showPopup(countrySelect);
                        });
                    }
                }.start();

            } else {
                showPopup(countrySelect);
            }
        });
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
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < charSequence.length(); i++) {
                    char c = charSequence.charAt(i);
                    if (Character.isDigit(c)) {
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
            editTextUsername.setError(getResources().getString(R.string.enter_phone));
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

        ApiCall loginCall = new ApiCall(getString(R.string.api_base) + "login",
                new Handler() {
                    @Override
                    public void handleResponse(HttpResponse response) {
                        if (response.getCode() == 403) {
                            showError(getResources().getString(R.string.already_logged));
                        } else if (response.getCode() == 422) {
                            showError(getResources().getString(R.string.incorrect_credentials));
                        } else if (response.getCode() == 200) {
                            spm.storeToken(response.getBody());
                            Intent intent = new Intent(MainActivity.this, Settings.class);
                            startActivity(intent);
                            finish();
                        }
                        loaded();
                    }

                    @Override
                    public void handleError(Exception x) {
                        runOnUiThread(() -> {
                            if (x instanceof UnknownHostException) {
                                Intent intent = new Intent(MainActivity.this, NoInternet.class);
                                startActivity(intent);
                            }
                        });
                    }
                },
                new Param("phone", ((TextView) findViewById(R.id.country_code)).getText() + username),
                new Param("password", password),
                new Param("number", (String) ((Spinner) findViewById(R.id.number)).getSelectedItem()),
                new Param("device_name", "test"));
        if (connectivityCheckSkipped) {
            new ConnectivityCheck(this, e -> {
            }, loginCall::execute, () -> {
                Intent intent = new Intent(MainActivity.this, NoInternet.class);
                startActivity(intent);
            }).execute();
        } else {
            loginCall.execute();
        }
    }

    Alert confExit = null;

    public void onBackPressed() {
        Popup openPopup = getOpenPopup();
        if (openPopup != null && openPopup.isOpen()) {
            openPopup.hide();
        } else {
            if (confExit == null) {
                confExit = new Alert(this, AlertType.CONFIRM);
                confExit.setTitle(getResources().getString(R.string.exit));
                confExit.setMessage(getResources().getString(R.string.exit_confirm));
            }
            if (confExit.isShown()) {
                confExit.hide();
            } else {
                showAlert(confExit, e -> {
                    if (e == ButtonType.YES) {
                        finishAndRemoveTask();
                    }
                });
            }
        }
    }

    public void hideConnect() {
        ObjectAnimator connectHide = ObjectAnimator.ofFloat(conRoot, "alpha", 0f);
        connectHide.setDuration(500);
        ObjectAnimator contentShow = ObjectAnimator.ofFloat(content, "alpha", 1f);
        contentShow.setDuration(500);
        conRoot.setClickable(false);
        content.setClickable(true);
        connectHide.start();
        contentShow.start();
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
}