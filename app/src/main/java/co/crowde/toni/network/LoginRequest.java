package co.crowde.toni.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import co.crowde.toni.base.BaseActivity;
import co.crowde.toni.constant.Const;
import co.crowde.toni.controller.auth.LoginController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.UserModel;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.auth.LoginActivity;
import co.crowde.toni.view.activity.auth.LoginSuccessActivity;
import co.crowde.toni.view.dialog.message.network.NetworkOfflineDialog;

public class LoginRequest extends BaseActivity {

    public static String message = "";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void postLogin(final Activity activity, String username, String pass){

        final UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(pass);

        String postBody = new Gson().toJson(user);
        Log.e("POST BODY", postBody);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);
        Log.e("REQUEST BODY",body.toString());

        Request requestHttp = new Request.Builder()
                .url(API.Login)
                .post(body)
                .build();
        Log.e("URL",API.Login);

        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_LOGIN, Const.LABEL_LOGIN_FAILED_NETWORK);
                        NetworkOfflineDialog.showDialog(activity);
                        dismissLoading();
                        Log.e("Error",e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String responseData = response.body().string();
                Log.e("RESPONSE BODY", responseData);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(responseData);
                            boolean status = json.getBoolean("status");
                            message = json.getString("message");
                            String data = json.getString("data");
                            dismissLoading();

                            if(status){
                                JSONObject objDataLogin = new JSONObject(data);
                                String username = objDataLogin.getString("username");
                                String type = objDataLogin.getString("type");
                                String role = objDataLogin.getString("role");
                                String shopId = objDataLogin.getString("shopId");
                                String token = objDataLogin.getString("token");
                                String picture = objDataLogin.getString("picture");

                                SavePref.saveToken(activity,token);
                                SavePref.saveShopId(activity,shopId);
                                SavePref.savePicture(activity, picture);
                                SavePref.saveDetailToko(activity, data);

                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_LOGIN, Const.LABEL_LOGIN_SUCCESS);

                                Intent loginSuccess = new Intent(activity, LoginSuccessActivity.class);
                                activity.startActivity(loginSuccess);
                                activity.finish();

                            } else {
                                LoginController.loginResponse(activity, message);
                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_LOGIN, Const.LABEL_LOGIN_FAILED_ACCOUNT);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

}
