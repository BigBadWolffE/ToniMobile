package co.crowde.toni.controller.network;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.adapter.ProductDashboardAdapter;
import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.fragment.Dashboard;
import co.crowde.toni.view.fragment.Inventory;
import co.crowde.toni.view.login.LoginSuccess;

public class ProductRequest {

    public static String message;

    public static void getProductList(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Product+API.Slash+SavePref.readShopId(activity)+API.Count)
                .build();

        Log.e("URL",API.Product+API.Slash+SavePref.readShopId(activity)+API.Count);

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
                                List<ProductModel> productModels = new Gson()
                                        .fromJson(data,
                                        new TypeToken<List<ProductModel>>() {
                                        }.getType());
                                Log.e("ProductModels", new Gson().toJson(productModels));

//                                Dashboard.productModels = new ArrayList<>();
                                Dashboard.productModels.clear();
                                Dashboard.productModels.addAll(productModels);
                                Dashboard.productDashboardAdapter.notifyDataSetChanged();
//                                Dashboard.productModels = new ArrayList<>();

                            } else {
                                if(message.equals("Token tidak valid")){
                                    UserController.tokenExpired(activity, message);
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

    public static void getInventoryList(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Product+API.Slash+SavePref.readShopId(activity)+API.Count)
                .build();

        Log.e("URL",API.Product+API.Slash+SavePref.readShopId(activity)+API.Count);

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
                                List<ProductModel> productModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<ProductModel>>() {
                                                }.getType());
                                Log.e("ProductModels", new Gson().toJson(productModels));

                                Inventory.productModels.clear();
                                Inventory.productModels.addAll(productModels);
                                Inventory.inventoryAdapter.notifyDataSetChanged();


                            } else {
                                if(message.equals("Token tidak valid")){
                                    UserController.tokenExpired(activity, message);
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
