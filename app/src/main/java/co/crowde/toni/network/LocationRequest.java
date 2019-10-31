package co.crowde.toni.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.android.volley.toolbox.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.base.BaseActivity;
import co.crowde.toni.constant.Const;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.listener.ResponseListener;
import co.crowde.toni.model.AdminModel;
import co.crowde.toni.model.CityModel;
import co.crowde.toni.model.DistrictModel;
import co.crowde.toni.model.NewAccountModel;
import co.crowde.toni.model.NewShopModel;
import co.crowde.toni.model.OtpModel;
import co.crowde.toni.model.ProvinceModel;
import co.crowde.toni.model.VillageModel;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.auth.LoginActivity;
import co.crowde.toni.view.activity.auth.RegisterActivity;
import co.crowde.toni.view.activity.otp.SendOtpRegisterActivity;
import co.crowde.toni.view.dialog.message.network.NetworkOfflineDialog;

import static co.crowde.toni.view.activity.auth.RegisterActivity.alertInvalidOtp;

public class LocationRequest extends BaseActivity {
    public static String username = "SPA%>WTv;6u;96^&XX@\"[/q_gZT('fSrU(+ynwS?";
    public static String password = "+\"-zVc+M9E*fRhj;+~svdMZd!3P!S[<v28L_3pyt";

    public static CardView cvBtnCancelDialog;


    public static ResponseListener listener;

    public static String message = "";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public LocationRequest (ResponseListener listener){
        LocationRequest.listener = listener;
    }

    public static boolean status;

    public static boolean messageResponse;

    public static String otpCode;
    public static String staat = "phone number is already exist";
    public static HttpResponse httpResponse;

    public static String responseData;


    public static void getProvinceList(final Activity activity) {
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .get()
                .url(API.PROVINCES)
                .build();

        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return null;
            }
        });


        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkOfflineDialog.showDialog(activity);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // Parsing Response ke bentuk String
                final String responseData = response.body().string();
                System.out.println("Response Province List: "+responseData);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Parsing Response ke bentuk JSONObject
                            JSONObject json = new JSONObject(responseData);

                            if(json.getString("data")!=null){
                                // Parsing Object "data" ke bentuk String
                                String arrayProvince = json.getString("data");

                                // Parsing String "data" ke bentuk Array
                                List<ProvinceModel> provinceModels = new Gson()
                                        .fromJson(arrayProvince,
                                                new TypeToken<List<ProvinceModel>>() {
                                                }.getType());

                                // Set ke Spinner Province
                                RegisterActivity.setProvinceAdapter(activity, provinceModels);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    public static void getCitiesList(final Activity activity, String idProvince) {
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .get()
                .url(API.CITIES+"/"+idProvince)
                .build();

        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return null;
            }
        });


        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkOfflineDialog.showDialog(activity);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String responseData = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(responseData);

                            if(json.getString("data")!=null){
                                String arrayCity = json.getString("data");

                                List<CityModel> cityModels = new Gson()
                                        .fromJson(arrayCity,
                                                new TypeToken<List<CityModel>>() {
                                                }.getType());

                                RegisterActivity.setCityAdapter(activity, cityModels);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    public static void getDistrictList(final Activity activity, String idCity) {
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .get()
                .url(API.DISTRICTS+"/"+idCity)
                .build();

        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return null;
            }
        });


        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkOfflineDialog.showDialog(activity);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String responseData = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(responseData);

                            if(json.getString("data")!=null){
                                String arrayDistrict = json.getString("data");

                                List<DistrictModel> districtModels = new Gson()
                                        .fromJson(arrayDistrict,
                                                new TypeToken<List<DistrictModel>>() {
                                                }.getType());

                                RegisterActivity.setDistrictAdapter(activity, districtModels);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    public static void getVillageList(final Activity activity, String idDistrict) {
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .get()
                .url(API.VILLAGES+"/"+idDistrict)
                .build();

        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return null;
            }
        });


        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkOfflineDialog.showDialog(activity);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String responseData = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(responseData);

                            if(json.getString("data")!=null){
                                String arrayVillage = json.getString("data");

                                List<VillageModel> villageModels = new Gson()
                                        .fromJson(arrayVillage,
                                                new TypeToken<List<VillageModel>>() {
                                                }.getType());

                                RegisterActivity.setVillageAdapter(activity, villageModels);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    public static void postShopData (final Activity activity, NewShopModel newShopModel, NewAccountModel newAccountModel){

        String ownerName = RegisterActivity.etShopOwner.getText().toString();
        String businessTyepe = RegisterActivity.etShopType.getSelectedItem().toString();
        String street = RegisterActivity.etShopAddress.getText().toString();
        String phoneNumber = RegisterActivity.etShopPhone.getText().toString();
        String description = "TOKO TANI PARTNER";
        String province = RegisterActivity.idProvince.toString();
        String regency = RegisterActivity.idCity.toString();
        String district = RegisterActivity.idDistrict.toString();
        String village = RegisterActivity.idVillage.toString();
        String shopName = RegisterActivity.etShopName.getText().toString();
        String userName = RegisterActivity.etUserName.getText().toString();
        String emailUser = RegisterActivity.etEmail.getText().toString();
        String password = RegisterActivity.etShopPass.getText().toString();
        String indentifier = "app";

        ArrayList<NewAccountModel>arrayListAccount = new ArrayList<>();
        //Set MODEL VALUE
        newAccountModel.setUsername(userName);
        newAccountModel.setPassword(password);
        newAccountModel.setEmail(emailUser);
        arrayListAccount.add(newAccountModel);
        String bodyAccount = new Gson().toJson(newAccountModel);
        newShopModel.setOwnerName(ownerName);
        newShopModel.setBusinessType(businessTyepe);
        newShopModel.setStreet(street);
        newShopModel.setPhoneNumber(phoneNumber);
        newShopModel.setDescription(description);
        newShopModel.setProvince(province);
        newShopModel.setRegency(regency);
        newShopModel.setDistrict(district);
        newShopModel.setVillage(village);
        newShopModel.setShopName(shopName);
        newShopModel.setAccount(newAccountModel);
        newShopModel.setIdentifier(indentifier);

        String postBody = new Gson().toJson(newShopModel);
        Log.e("POST BODY", postBody);

        OkHttpClient client = new OkHttpClient();


        RequestBody body = RequestBody.create(JSON, postBody);
        Log.e("REQUEST BODY",body.toString());


        Request requestHttp = new Request.Builder()
                .url(API.Register+"/")
                .post(body)
                .build();
        Log.e("URL",API.Register);




        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_LOGIN, Const.LABEL_LOGIN_FAILED_NETWORK);
                NetworkOfflineDialog.showDialog(activity);
                Log.e("Error",e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                responseData = response.body().string();
                Log.e("RESPONSE BODY", responseData);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            Response codeResponse = null;
//                            try {
//                                codeResponse = client.newCall(requestHttp).execute();
//                                Log.e("Rspo","try");
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            final int resp = codeResponse.code();
//                            Log.e("Response Code", String.valueOf(resp));
//                            Log.e("Response Code", String.valueOf(codeResponse));
                            JSONObject json = new JSONObject(responseData);

//                            int statusCode = httpResponse.getStatusCode();
                            if(response.code() == 201 ){
                                    otpCode = json.getString("otp");
                                    SavePref.saveOtpCode(activity,otpCode);
                                    Intent register = new Intent(activity, SendOtpRegisterActivity.class);
                                    activity.startActivity(register);
                                    activity.finish();
                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_REGISTER, Const.LABEL_REGISTER_SEND_OTP);

                            }else {
                                RegisterActivity.alertInvalid(activity);
                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_REGISTER, Const.LABEL_RESET_PASS_SEND_OTP_FAILED);

//                                Intent register = new Intent(activity, LoginActivity.class);
//                                activity.startActivity(register);
//                                activity.finish();
                            }
//                            else {
//                                if(responseData.contains(staat = json.getString("status"))){
//                                    Intent register = new Intent(activity, SendOtpRegisterActivity.class);
//                                    activity.startActivity(register);
//                                    activity.finish();
//                                }
//                            }
//                            if(status){
//
//
//
//                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_LOGIN, Const.LABEL_LOGIN_FAILED_NETWORK);
//
//
//
//
//
//
//                            }
//                            else if (stat.equals("phone number is already exist")){
//                                Toast.makeText(activity, "Data Gagal Tersimpan", Toast.LENGTH_SHORT).show();
//
//                            }



//                            if(status){
//
//
//
//                            }else{
//                                Toast.makeText(activity, "Data Gagal Tersimpan", Toast.LENGTH_SHORT).show();
//
//                            }

                        }catch (JSONException e) {
                            Log.e("Error",e.toString());
                            e.printStackTrace();
                        }
                    }
                });

            }
        });


    }
    public static void verifyOtp (final Activity activity, OtpModel otpModel){

        String otpCode = SendOtpRegisterActivity.otp1.getText().toString();
        otpModel.setOtp(otpCode);

        String postBody = new Gson().toJson(otpModel);
        Log.e("Post Otp",postBody);
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);
        Log.e("REQUEST BODY Otp",body.toString());


        Request requestHttp = new Request.Builder()
                .url(API.Otp)
                .post(body)
                .build();
        Log.e("URL",API.Otp);

        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
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
                Log.e("RESPONSE BODY TOKEN", responseData);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(responseData);
                            message = json.getString("message");

                            if (message.equals("success")){
                                String data = json.getString("data");
                                Log.e("DATA RESPONSE", data);
                                Intent beres = new Intent(activity, LoginActivity.class);
                                activity.startActivity(beres);
                                activity.finish();
                                Toast.makeText(activity,"REGISTRASI BERHASIL!!!",Toast.LENGTH_LONG).show();
                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_REGISTER, Const.LABEL_REGISTER_SUCCESS);


                            }else if (message.equals("otp is not found")){
                                SendOtpRegisterActivity.otp1.setText(" ");
                                alertInvalidOtp(activity);
                                SendOtpRegisterActivity.progressDialog.dismiss();
                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_REGISTER, Const.LABEL_REGISTER_SEND_OTP_FAILED);
                            }else{
                                resendOtp(activity);
                            }


//                            if (message.equals("success")){
//                                JSONObject objDataLogin = new JSONObject(data);
//
////                                String username = objDataLogin.getString("username");
////                                String type = objDataLogin.getString("type");
////                                String role = objDataLogin.getString("role");
////                                String picture = objDataLogin.getString("picture");
////                                String token = objDataLogin.getString("token");
//
////                                SavePref.saveTokenAdmin(activity,token);
//
//
//
//
//
//
//                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_LOGIN, Const.LABEL_LOGIN_ADMIN_SUCCESS);
//                            }else{
//                                Toast.makeText(activity,"KODE OTP SALAH",Toast.LENGTH_LONG).show();
//
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });


    }

    public static void resendOtp (final Activity activity){


        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .url(API.OtpResend)
                .get()
                .build();
        Log.e("URL",API.OtpResend);

        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
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

                responseData = response.body().string();
                Log.e("RESPONSE BODY RESEND", responseData);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(responseData);
                            Log.e("Tag Resend",responseData);
                            if (response.code()==201){
                                String otpResend = json.getString("otp");
                                Log.e("Tag Resend",otpResend);
                                RegisterActivity.alertSuccessResendOtp(activity);
                                alertViewOtp(activity);
                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_REGISTER, Const.LABEL_REGISTER_SEND_OTP);
                            }else {
                                RegisterActivity.alertInvalidOtp(activity);
                                alertViewOtp(activity);
                            }

//                            if (message.equals("success")){
//                                JSONObject objDataLogin = new JSONObject(data);
//
////                                String username = objDataLogin.getString("username");
////                                String type = objDataLogin.getString("type");
////                                String role = objDataLogin.getString("role");
////                                String picture = objDataLogin.getString("picture");
////                                String token = objDataLogin.getString("token");
//
////                                SavePref.saveTokenAdmin(activity,token);
//
//
//
//
//
//
//                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_LOGIN, Const.LABEL_LOGIN_ADMIN_SUCCESS);
//                            }else{
//                                Toast.makeText(activity,"KODE OTP SALAH",Toast.LENGTH_LONG).show();
//
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });


    }


    public static void openDialog(final Activity context) {
        final Dialog dialog = new Dialog(context); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_register_gagal);
        dialog.setTitle("Alert!");
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        cvBtnCancelDialog = dialog.findViewById(R.id.btnCancel);
        cvBtnCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public static void alertViewOtp(final Context activity ){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(responseData);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Dismiss",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }






    public static void generateToken (final Activity activity, AdminModel adminModel){

        String postBody = new Gson().toJson(adminModel);
        Log.e("Post Token",postBody);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);
        Log.e("REQUEST BODY TOKEN",body.toString());


        Request requestHttp = new Request.Builder()
                .url(API.Login)
                .post(body)
                .build();
        Log.e("URL",API.Login);

        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
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
                Log.e("RESPONSE BODY TOKEN", responseData);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(responseData);
                            boolean status = json.getBoolean("status");
                            message = json.getString("message");
                            String data = json.getString("data");
                            dismissLoading();

                            if (status){
                                JSONObject objDataLogin = new JSONObject(data);
                                String username = objDataLogin.getString("username");
                                String type = objDataLogin.getString("type");
                                String role = objDataLogin.getString("role");
                                String picture = objDataLogin.getString("picture");
                                String token = objDataLogin.getString("token");

                                SavePref.saveTokenAdmin(activity,token);


                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_LOGIN, Const.LABEL_LOGIN_ADMIN_SUCCESS);

                            }else{
                                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_LOGIN, Const.LABEL_LOGIN_ADMIN_FAILED);

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
