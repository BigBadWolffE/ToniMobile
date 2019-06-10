package co.crowde.toni.network;

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

import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CategoryModel;
import co.crowde.toni.view.activity.filter.CatalogFilter;
import co.crowde.toni.view.activity.filter.DashboardFilter;
import co.crowde.toni.view.activity.filter.InventoryFilter;

public class CategoryRequest {

    public static String message;
    public static ArrayList<CategoryModel> categoryModels = new ArrayList<>();

    public static void getCategoryList(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Category+"?count=999")
                .build();

        Log.e("URL",API.Category+API.Count);

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
                                categoryModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<CategoryModel>>() {
                                                }.getType());
                                for(int i=0; i<categoryModels.size();i++){
                                    String nama = categoryModels.get(i).getCategoryName();
                                    String id = categoryModels.get(i).getCategoryId();
                                    DashboardFilter.categoryModels.add(new CategoryModel(
                                            id,nama));
                                    DashboardFilter.progressDialog.dismiss();
                                }

                            } else {
                                if(message.equals("Token tidak valid")){
                                    DashboardFilter.progressDialog.dismiss();
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

    public static void getCategoryInventory(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Category+API.Count)
                .build();

        Log.e("URL",API.Category+API.Count);

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
                                categoryModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<CategoryModel>>() {
                                                }.getType());
                                for(int i=0; i<categoryModels.size();i++){
                                    String nama = categoryModels.get(i).getCategoryName();
                                    String id = categoryModels.get(i).getCategoryId();
                                    InventoryFilter.categoryModels.add(new CategoryModel(
                                            id,nama));
                                    InventoryFilter.progressDialog.dismiss();
                                }

                            } else {
                                if(message.equals("Token tidak valid")){
                                    InventoryFilter.progressDialog.dismiss();
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

    public static void getCategoryCatalog(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Category+API.Count)
                .build();

        Log.e("URL",API.Category+API.Count);

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
                                categoryModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<CategoryModel>>() {
                                                }.getType());
                                for(int i=0; i<categoryModels.size();i++){
                                    String nama = categoryModels.get(i).getCategoryName();
                                    String id = categoryModels.get(i).getCategoryId();
                                    CatalogFilter.categoryModels.add(new CategoryModel(
                                            id,nama));
                                    CatalogFilter.progressDialog.dismiss();
                                }

                            } else {
                                if(message.equals("Token tidak valid")){
                                    CatalogFilter.progressDialog.dismiss();
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
