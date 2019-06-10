package co.crowde.toni.view.activity.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import co.crowde.toni.R;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.network.LoginRequest;
import co.crowde.toni.helper.SavePref;

public class Login extends AppCompatActivity {

    public static TextView tvLoginHeader, tvClosedLabel, tvClosedTime,
            tvForgetPass;
    public static EditText et_username, et_password;
    public static CardView btnLogin;

    public static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
//        if (tabletSize) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
        setContentView(R.layout.activity_login);

        //Get Id View
        tvLoginHeader = findViewById(R.id.tvLoginHeader);
        tvClosedLabel = findViewById(R.id.tvClosedHeader);
        tvClosedTime = findViewById(R.id.tvClosedTime);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        btnLogin = findViewById(R.id.cvBtnLogin);
        btnLogin.setEnabled(false);
        tvForgetPass = findViewById(R.id.tvForgetPassword);

        //Function Status Activated Button
        et_username.addTextChangedListener(loginWatcher);
        et_password.addTextChangedListener(loginWatcher);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

//                btnLogin.setEnabled(false);
                if(et_username.getText().toString().equals("admin")){
                    Intent wrongUser = new Intent(Login.this, ForgotUser.class);
                    startActivity(wrongUser);
                    finish();
                } else {
                    LoginRequest.postLogin(Login.this);
                }

            }
        });

        //Forget Password
        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wrongPass = new Intent(Login.this, ForgotPass.class);
                startActivity(wrongPass);
                finish();
            }
        });

        setShopClosedTime(Login.this);
        hideKeyboard(Login.this);

    }

    public TextWatcher loginWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_username.getText().length() > 0) {
                btnLogin.setCardBackgroundColor(
                        getResources().getColor(R.color.colorThemeGrey));
                btnLogin.setEnabled(false);

                //Button Active
                if (et_password.getText().length() > 0){
                    btnLogin.setCardBackgroundColor(
                            getResources().getColor(R.color.colorThemeOrange));
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setCardBackgroundColor(
                            getResources().getColor(R.color.colorThemeGrey));
                    btnLogin.setEnabled(false);
                }

            } else {
                btnLogin.setCardBackgroundColor(
                        getResources().getColor(R.color.colorThemeGrey));
                btnLogin.setEnabled(false);

                if (et_password.getText().length() > 0){
                    btnLogin.setCardBackgroundColor(
                            getResources().getColor(R.color.colorThemeGrey));
                    btnLogin.setEnabled(false);
                } else {
                    btnLogin.setCardBackgroundColor(
                            getResources().getColor(R.color.colorThemeGrey));
                    btnLogin.setEnabled(false);
                }

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static void setShopClosedTime(Activity activity){
        if(SavePref.readClosedTime(activity)==null){
            tvClosedTime.setText(activity.getResources().getString(R.string.strips));
        } else {
            tvClosedTime.setText(SavePref.readClosedTime(activity));
        }
    }

    public static void hideKeyboard(final Activity activity){
        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboard.hideSoftKeyboard(v, activity);
            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboard.hideSoftKeyboard(v, activity);
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Tutup Aplikasi TONI")
                .setMessage("Apakah Anda ingin menutup aplikasi TONI?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        Login.super.onBackPressed();
                    }
                }).create().show();
    }
}
