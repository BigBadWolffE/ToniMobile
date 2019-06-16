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
import co.crowde.toni.view.dialog.app.CloseAppsDialog;

import static co.crowde.toni.constant.Const.KEY_PASSWORD_FROM_LOGIN;
import static co.crowde.toni.constant.Const.KEY_USERNAME_FROM_LOGIN;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tv_login_header) TextView tvLoginHeader;
    @BindView(R.id.tv_closed_header) TextView tvClosedLabel;
    @BindView(R.id.tv_closed_time) TextView tvClosedTime;
    @BindView(R.id.tv_forgot_password) TextView tvForgetPass;
    @BindView(R.id.tv_register) TextView tvRegister;
    @BindView(R.id.et_username) EditText et_username;
    @BindView(R.id.et_password) EditText et_password;
    @BindView(R.id.cv_btn_login) CardView btnLogin;

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
        ButterKnife.bind(this);

        btnLoginClicked();
        tvForgetPassClicked();
        tvRegisterClicked();

        etUsernameListener();
        etPasswordListener();

        setShopClosedTime(LoginActivity.this);
        hideKeyboard(LoginActivity.this);

    }

    @OnClick(R.id.cv_btn_login)
    void btnLoginClicked(){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String username = et_username.getText().toString();

        if(username.equals("admin")){
//            progressDialog.dismiss();
            Intent wrongUser = new Intent(LoginActivity.this, ForgotUserActivity.class);
            startActivity(wrongUser);
        } else {
            LoginRequest.postLogin(LoginActivity.this);
        }
    }

    @OnClick(R.id.tv_forgot_password)
    void tvForgetPassClicked(){
        Intent wrongPass = new Intent(LoginActivity.this, ForgotPassActivity.class);
        startActivity(wrongPass);
    }

    @OnClick(R.id.tv_register)
    void tvRegisterClicked(){
        Intent wrongPass = new Intent(LoginActivity.this, ForgotPassActivity.class);
        startActivity(wrongPass);
    }

    @OnTextChanged(R.id.et_username)
    void etUsernameListener(){
        et_username.addTextChangedListener(loginWatcher);
    }

    @OnTextChanged(R.id.et_password)
    void etPasswordListener(){
        et_password.addTextChangedListener(loginWatcher);
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

    public void setShopClosedTime(Activity activity){
        if(SavePref.readClosedTime(activity)==null){
            tvClosedTime.setText(activity.getResources().getString(R.string.strips));
        } else {
            tvClosedTime.setText(SavePref.readClosedTime(activity));
        }
    }

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String username = data.getStringExtra(KEY_USERNAME_FROM_LOGIN);
                String password = data.getStringExtra(KEY_PASSWORD_FROM_LOGIN);

                et_username.setText(username);
                et_password.setText(password);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void onBackPressed() {
        CloseAppsDialog.showDialog(LoginActivity.this);
    }
}
