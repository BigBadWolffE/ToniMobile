package co.crowde.toni.controller.network;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
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

import co.crowde.toni.R;
import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CategoryModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.fragment.Dashboard;
import co.crowde.toni.view.popup.FilterInventoryPopup;
import co.crowde.toni.view.popup.FilterProductDashboardPopup;

public class CategoryRequest {

    public static String message;
    public static ArrayList<Boolean> booleanArrayList = new ArrayList<>();
    public static ArrayList<String> categoryList = new ArrayList<>();
    public static ArrayList<CategoryModel> categoryModels = new ArrayList<>();

    public static void getCategoryList(final Activity activity){
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
                                Log.e("ProductModels", new Gson().toJson(categoryModels));

                                for (int i=0;i<categoryModels.size();i++){
                                    final Chip chip = new Chip(activity);
                                    chip.setId(i);
                                    chip.setTag(i);

                                    chip.setText(categoryModels.get(i).getCategoryName());
                                    chip.setCheckable(true);
                                    booleanArrayList.add(false);

                                    final int finalI = i;
                                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                            int tag = (int) compoundButton.getTag();
                                            booleanArrayList.set(tag, b);

                                            if(b){
                                                categoryList.add(categoryModels.get(finalI).getCategoryName());
                                                chip.setChipBackgroundColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorThemeGreen)));
                                                chip.setTextColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorWhite)));
                                            } else {
                                                categoryList.remove(categoryModels.get(finalI).getCategoryName());
                                                chip.setChipBackgroundColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorThemeGreyLight)));
                                                chip.setTextColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorBlack)));
                                            }
                                        }
                                    });
                                    FilterProductDashboardPopup.chipCategory.addView(chip);

                                    FilterProductDashboardPopup.tvHeaderFilter.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(activity, new Gson().toJson(categoryList), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

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
                                Log.e("ProductModels", new Gson().toJson(categoryModels));

                                for (int i=0;i<categoryModels.size();i++){
                                    final Chip chip = new Chip(activity);
                                    chip.setId(i);
                                    chip.setTag(i);

                                    chip.setText(categoryModels.get(i).getCategoryName());
                                    chip.setCheckable(true);
                                    booleanArrayList.add(false);

                                    final int finalI = i;
                                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                            int tag = (int) compoundButton.getTag();
                                            booleanArrayList.set(tag, b);

                                            if(b){
                                                categoryList.add(categoryModels.get(finalI).getCategoryName());
                                                chip.setChipBackgroundColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorThemeGreen)));
                                                chip.setTextColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorWhite)));
                                            } else {
                                                categoryList.remove(categoryModels.get(finalI).getCategoryName());
                                                chip.setChipBackgroundColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorThemeGreyLight)));
                                                chip.setTextColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorBlack)));
                                            }
                                        }
                                    });
                                    FilterInventoryPopup.chipCategory.addView(chip);

                                    FilterInventoryPopup.tvHeaderFilter.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(activity, new Gson().toJson(categoryList), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

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
