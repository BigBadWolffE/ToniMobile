package co.crowde.toni.view.activity.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import co.crowde.toni.R;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.network.LoginRequest;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.view.dialog.message.app.CloseAppsDialog;



public class LoginActivity extends AppCompatActivity {
    public static TextView tvLoginHeader, tvClosedLabel, tvClosedTime,
            tvForgetPass, tvRegister;
    public static EditText et_username, et_password;
    public static CardView btnLogin;
    public static ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_login);

        tvLoginHeader = findViewById(R.id.tv_login_header);
        tvClosedLabel = findViewById(R.id.tv_closed_header);
        tvClosedTime = findViewById(R.id.tv_closed_time);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.cv_btn_login);
        btnLogin.setEnabled(false);
        tvForgetPass = findViewById(R.id.tv_forgot_password);
        tvRegister = findViewById(R.id.tv_register);

        et_username.addTextChangedListener(loginWatcher);
        et_password.addTextChangedListener(loginWatcher);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if(et_username.getText().toString().equals("admin")){
                    Intent wrongUser = new Intent(LoginActivity.this, ForgotUserActivity.class);
                    startActivity(wrongUser);
                } else {
                    String username = et_username.getText().toString();
                    String pass = et_password.getText().toString();
                    LoginRequest.postLogin(LoginActivity.this, username, pass);
                }
            }
        });

        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wrongPass = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(wrongPass);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wrongPass = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(wrongPass);
            }
        });

        setShopClosedTime(LoginActivity.this);
        hideKeyboard(LoginActivity.this);
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
        CloseAppsDialog.showDialog(LoginActivity.this);
    }
}
