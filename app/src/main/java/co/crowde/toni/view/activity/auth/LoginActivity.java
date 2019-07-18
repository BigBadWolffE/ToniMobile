package co.crowde.toni.view.activity.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;

import co.crowde.toni.R;
import co.crowde.toni.constant.Const;
import co.crowde.toni.helper.analytics.AnalyticsApplication;
import co.crowde.toni.helper.analytics.AnalyticsTrackers;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.network.LoginRequest;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.dialog.message.app.CloseAppsDialog;

import static co.crowde.toni.utils.ValidateEdittext.validateLogin;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    TextView tvLoginHeader, tvClosedLabel, tvClosedTime,
            tvForgetPass, tvRegister;
    EditText et_username, et_password;
    TextInputLayout textInputLayout;
    CardView btnLogin;
    ProgressDialog progressDialog;

    boolean isShown;

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

        progressDialog = new ProgressDialog(LoginActivity.this);

        isShown = false;

        textInputLayout = findViewById(R.id.layout_set_password);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.cv_btn_login);
        tvForgetPass = findViewById(R.id.tv_forgot_password);
        tvRegister = findViewById(R.id.tv_register);

//        et_username.addTextChangedListener(loginWatcher);
//        et_password.addTextChangedListener(loginWatcher);

        btnLogin.setOnClickListener(this);
        tvForgetPass.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        textInputLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(LoginActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        hideKeyboard(LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_btn_login:
                btnLoginListener();
                break;

            case R.id.tv_forgot_password:
                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION,Const.MODUL_PASS,Const.LABEL_FORGOT_PASS);
                Intent wrongPass = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(wrongPass);
                break;

            case R.id.tv_register:
                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION,Const.MODUL_REGISTER,Const.LABEL_REGISTER);
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
                break;
        }

    }

//    private void togglePasswordListener() {
//        View togglePasswordButton = findTogglePasswordButton(textInputLayout);
//        if (togglePasswordButton != null) {
//            togglePasswordButton.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    // implementation
//                    return false;
//                }
//            });
//        }
//    }
//
//    private View findTogglePasswordButton() {
//        return findViewById(R.id.text_input_password_toggle);
//    }

    private void btnLoginListener() {
        AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION,Const.MODUL_LOGIN,Const.LABEL_LOGIN);

        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(et_username.getText().toString().equals("admin")){
            progressDialog.dismiss();

            Intent wrongUser = new Intent(LoginActivity.this, ForgotUserActivity.class);
            startActivity(wrongUser);

            et_password.setText("");

        } else {
            validateLogin(et_username, et_password);

            if(validateLogin(et_username, et_password)){
                String username = et_username.getText().toString();
                String pass = et_password.getText().toString();

                LoginRequest.postLogin(LoginActivity.this, username, pass, progressDialog);
            } else {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Silahkan isi formulir dengan benar.", Toast.LENGTH_SHORT).show();
            }

        }
    }

//    public TextWatcher loginWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (et_username.getText().length() > 0) {
//                btnLogin.setCardBackgroundColor(
//                        getResources().getColor(R.color.colorThemeGrey));
//                btnLogin.setEnabled(false);
//
//                if (et_password.getText().length() > 0){
//                    btnLogin.setCardBackgroundColor(
//                            getResources().getColor(R.color.colorThemeOrange));
//                    btnLogin.setEnabled(true);
//                } else {
//                    btnLogin.setCardBackgroundColor(
//                            getResources().getColor(R.color.colorThemeGrey));
//                    btnLogin.setEnabled(false);
//                }
//            } else {
//                btnLogin.setCardBackgroundColor(
//                        getResources().getColor(R.color.colorThemeGrey));
//                btnLogin.setEnabled(false);
//                if (et_password.getText().length() > 0){
//                    btnLogin.setCardBackgroundColor(
//                            getResources().getColor(R.color.colorThemeGrey));
//                    btnLogin.setEnabled(false);
//                } else {
//                    btnLogin.setCardBackgroundColor(
//                            getResources().getColor(R.color.colorThemeGrey));
//                    btnLogin.setEnabled(false);
//                }
//            }
//        }
//        @Override
//        public void afterTextChanged(Editable s) {
//        }
//    };
//
//    public static void setShopClosedTime(Activity activity){
//        if(SavePref.readClosedTime(activity)==null){
//            tvClosedTime.setText(activity.getResources().getString(R.string.strips));
//        } else {
//            tvClosedTime.setText(SavePref.readClosedTime(activity));
//        }
//    }

    public void hideKeyboard(final Activity activity){
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
