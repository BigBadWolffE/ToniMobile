package co.crowde.toni.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import co.crowde.toni.controller.user.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.DistrictModel;
import co.crowde.toni.model.ProvinceModel;
import co.crowde.toni.model.RegencyModel;
import co.crowde.toni.model.VillageModel;
import co.crowde.toni.view.activity.auth.RegisterActivity;
import co.crowde.toni.view.activity.customer.SelectCustomerActivity;
import co.crowde.toni.view.dialog.message.network.NetworkOfflineDialog;

public class LocationRequest {
    public static void getProvinceList(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .url(API.PROVINCE)
                .build();

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
                            String message = json.getString("message");
                            String data = json.getString("semuaprovinsi");
                            Log.e("DATA RESPONSE", data);

                            if(message.equals("Berhasil mendapatkan data provinsi")){
                                List<ProvinceModel> provinceModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<ProvinceModel>>() {
                                                }.getType());

                                RegisterActivity.provinceModelList.addAll(provinceModels);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    public static void getRegencyList(final Activity activity, String idProvince){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .url(API.PROVINCE+"/"+idProvince+"/kabupaten")
                .build();

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
                            String message = json.getString("message");
                            String data = json.getString("kabupatens");
                            Log.e("DATA RESPONSE", data);

                            if(message.equals("Berhasil mengambil data kabupaten")){
                                List<RegencyModel> regencyModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<RegencyModel>>() {
                                                }.getType());

                                Log.e("Kabupaten", new Gson().toJson(regencyModels));

                                RegisterActivity.regencyModels.addAll(regencyModels);
                                RegisterActivity.getRegency(activity);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    public static void getDistrictList(final Activity activity, String idRegency){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .url(API.PROVINCE+"/kabupaten/"+idRegency+"/kecamatan")
                .build();

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
                            String message = json.getString("message");
                            String data = json.getString("kecamatans");
                            Log.e("DATA RESPONSE", data);

                            if(message.equals("Berhasil mengambil data kecamatan")){
                                List<DistrictModel> districtModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<DistrictModel>>() {
                                                }.getType());

                                RegisterActivity.districtModels.addAll(districtModels);
                                RegisterActivity.getDistrict(activity);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    public static void getVillageList(final Activity activity, String idDistrict){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .url(API.PROVINCE+"/kabupaten/kecamatan/"+idDistrict+"/desa")
                .build();

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
                            String message = json.getString("message");
                            String data = json.getString("desas");
                            Log.e("DATA RESPONSE", data);

                            if(message.equals("Berhasil mengambil data desa")){
                                List<VillageModel> villageModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<VillageModel>>() {
                                                }.getType());

                                RegisterActivity.villageModels.addAll(villageModels);
                                RegisterActivity.getVillage(activity);

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
