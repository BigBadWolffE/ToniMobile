package co.crowde.toni.view.activity.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import co.crowde.toni.R;
import co.crowde.toni.base.BaseActivity;
import co.crowde.toni.constant.Const;
import co.crowde.toni.controller.auth.LoginController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.helper.analytics.AnalyticsApplication;
import co.crowde.toni.helper.analytics.AnalyticsTrackers;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.listener.ResponseListener;
import co.crowde.toni.model.UserModel;
import co.crowde.toni.network.API;
import co.crowde.toni.network.LoginRequest;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.dialog.message.app.CloseAppsDialog;
import co.crowde.toni.view.dialog.message.network.NetworkOfflineDialog;

import static co.crowde.toni.network.LoginRequest.JSON;
import static co.crowde.toni.network.LoginRequest.postLogin;
import static co.crowde.toni.network.LoginRequest.postLoginRequest;
import static co.crowde.toni.utils.ValidateEdittext.validateLogin;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    TextView tvLoginHeader, tvClosedLabel, tvClosedTime,
            tvForgetPass, tvRegister;
    EditText et_username, et_password;
    TextInputLayout textInputLayout;
    CardView btnLogin;

    boolean status;
    String username, password;
    String data, message;

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
                AnalyticsToniUtils.getEvent(
                        Const.CATEGORY_AUTHENTIFICATION,
                        Const.MODUL_PASS,
                        Const.LABEL_FORGOT_PASS);
                Intent wrongPass = new Intent(
                        LoginActivity.this,
                        ResetPasswordActivity.class);
                startActivity(wrongPass);
                break;

            case R.id.tv_register:
                AnalyticsToniUtils.getEvent(
                        Const.CATEGORY_AUTHENTIFICATION,
                        Const.MODUL_REGISTER,
                        Const.LABEL_REGISTER);
                Intent register = new Intent(
                        LoginActivity.this,
                        RegisterActivity.class);
                startActivity(register);
                break;
        }

    }

    private void btnLoginListener() {
        AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION,Const.MODUL_LOGIN,Const.LABEL_LOGIN);

        username = et_username.getText().toString();
        password = et_password.getText().toString();

        if(username.equals("admin")){
            Intent wrongUser = new Intent(LoginActivity.this, ForgotUserActivity.class);
            startActivity(wrongUser);
            et_password.setText("");
        } else {
            if(validateLogin()){
                showLoading();

                UserModel user = new UserModel();
                user.setUsername(username);
                user.setPassword(password);

                postLogin(LoginActivity.this, user);

//                postLoginRequest(this, user, new ResponseListener() {
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailed() {
//                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        }
    }

//    public void postLogin(){
//        final UserModel user = new UserModel();
//        user.setUsername(username);
//        user.setPassword(password);
//
//        String postBody = new Gson().toJson(user);
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(JSON, postBody);
//        Request requestHttp = new Request.Builder()
//                .url(API.Login)
//                .post(body)
//                .build();
//
//        client.newCall(requestHttp).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, final IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        AnalyticsToniUtils.getEvent(
//                                Const.CATEGORY_AUTHENTIFICATION,
//                                Const.MODUL_LOGIN,
//                                Const.LABEL_LOGIN_FAILED_NETWORK);
//                        NetworkOfflineDialog.showDialog(LoginActivity.this);
//                        dismissLoading();
//                        Log.e("Error",e.toString());
//                    }
//                });
//            }
//            @Override
//            public void onResponse(Response response) throws IOException {
//                final String responseData = response.body().string();
//                Log.e("RESPONSE BODY", responseData);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject json = new JSONObject(responseData);
//                            status = json.getBoolean("status");
//                            message = json.getString("message");
//                            data = json.getString("data");
//                            dismissLoading();
//
//                            if(status){
//                                Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//    }

//    private String getContent() {
//        String getString ="";
//        getString = et_username.getText().toString();
//        return getString;
//    }

    private boolean validateLogin() {
        return !et_username.getText().toString().isEmpty() && !et_password.getText().toString().isEmpty();
    }

    @Override
    public void onBackPressed() {
        CloseAppsDialog.showDialog(LoginActivity.this);
    }
}
