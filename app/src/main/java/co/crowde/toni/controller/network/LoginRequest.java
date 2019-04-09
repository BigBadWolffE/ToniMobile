package co.crowde.toni.controller.network;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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

import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.UserModel;
import co.crowde.toni.view.login.ForgetPassword;
import co.crowde.toni.view.login.ForgetUsername;
import co.crowde.toni.view.login.Login;
import co.crowde.toni.view.login.LoginSuccess;

public class LoginRequest {

    public static String message = "";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void postLogin(final Activity activity){
        String username = Login.et_username.getText().toString();
        String password = Login.et_password.getText().toString();

        final UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);

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
                        Toast.makeText(
                                activity, "HTTP Request Failure", Toast.LENGTH_SHORT).show();
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

                                Intent loginSuccess = new Intent(activity, LoginSuccess.class);
                                activity.startActivity(loginSuccess);
                                activity.finish();

                            } else {
                                if(message.equals("Password anda salah!")){
                                    ForgetPassword.showForgetPass(activity);
                                } else if(message.equals("Username anda salah!")){
                                    ForgetUsername.showForgetUser(activity);
                                }
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
