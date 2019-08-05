package co.crowde.toni.view.activity.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;

import co.crowde.toni.R;
import co.crowde.toni.base.BaseActivity;
import co.crowde.toni.constant.Const;
import co.crowde.toni.helper.analytics.AnalyticsApplication;
import co.crowde.toni.helper.analytics.AnalyticsTrackers;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.network.LoginRequest;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.dialog.message.app.CloseAppsDialog;

import static co.crowde.toni.utils.ValidateEdittext.validateLogin;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    TextView tvLoginHeader, tvClosedLabel, tvClosedTime,
            tvForgetPass, tvRegister;
    EditText et_username, et_password;
    TextInputLayout textInputLayout;
    CardView btnLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputLayout = findViewById(R.id.layout_set_password);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.cv_btn_login);
        tvForgetPass = findViewById(R.id.tv_forgot_password);
        tvRegister = findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(this);
        tvForgetPass.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

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

    private void btnLoginListener() {
        AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION,Const.MODUL_LOGIN,Const.LABEL_LOGIN);

        if(et_username.getText().toString().equals("admin")){

            Intent wrongUser = new Intent(LoginActivity.this, ForgotUserActivity.class);
            startActivity(wrongUser);

            et_password.setText("");

        } else {
            validateLogin(et_username, et_password);
            if(validateLogin(et_username, et_password)){
                showLoading();
                String username = et_username.getText().toString();
                String pass = et_password.getText().toString();

                LoginRequest.postLogin(LoginActivity.this, username, pass);
            }

        }
    }

    @Override
    public void onBackPressed() {
        CloseAppsDialog.showDialog(LoginActivity.this);
    }
}
