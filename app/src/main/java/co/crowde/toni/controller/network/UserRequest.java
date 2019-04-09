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

public class UserRequest {

    public static String message = "";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static void getShopDetail(final Activity activity){

        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.GetShopDetail+SavePref.readShopId(activity))
                .build();

        Log.e("URL",API.GetShopDetail+SavePref.readShopId(activity));

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
                            Log.e("DATA RESPONSE", data);

                            if(status){
                                JSONObject objDataLogin = new JSONObject(data);
                                String shopId = objDataLogin.getString("shopId");
                                String shopName = objDataLogin.getString("shopName");
                                String street = objDataLogin.getString("street");
                                String province = objDataLogin.getString("province");
                                String regency = objDataLogin.getString("regency");
                                String district = objDataLogin.getString("district");
                                String village = objDataLogin.getString("village");
                                String businessType = objDataLogin.getString("businessType");
                                String description = objDataLogin.getString("description");
                                String ownerName = objDataLogin.getString("ownerName");
                                String phoneNumber = objDataLogin.getString("phoneNumber");

                                SavePref.saveUserDetail(activity,data);
                                LoginSuccess.setOwnerName(activity, data);
                            } else {
                                if(message.equals("Token tidak valid!")){
                                    Toast.makeText(activity, "Token Expired!", Toast.LENGTH_SHORT).show();
                                    Intent logout = new Intent(activity, Login.class);
                                    activity.startActivity(logout);
                                    activity.finish();
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
