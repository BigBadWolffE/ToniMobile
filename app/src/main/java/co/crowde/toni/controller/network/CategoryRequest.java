package co.crowde.toni.controller.network;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.util.Log;
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
import co.crowde.toni.view.popup.FilterProductDashboardPopup;

public class CategoryRequest {

    public static String message;
    public static ArrayList<Boolean> booleanArrayList = new ArrayList<>();

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
//                                JSONObject objDataLogin = new JSONObject(data);
//                                String shopId = objDataLogin.getString("shopId");
//                                String productId = objDataLogin.getString("productId");
//                                String categoryId = objDataLogin.getString("categoryId");
//                                String productName = objDataLogin.getString("productName");
//                                String description = objDataLogin.getString("description");
//                                String picture = objDataLogin.getString("picture");
//                                String statusProduct = objDataLogin.getString("status");
//                                int purchasePrice = objDataLogin.getInt("purchasePrice");
//                                int sellingPrice = objDataLogin.getInt("sellingPrice");
//                                String unit = objDataLogin.getString("unit");
//                                String supplierId = objDataLogin.getString("supplierId");
//                                String createdAt = objDataLogin.getString("createdAt");
//                                String lastUpdated = objDataLogin.getString("lastUpdated");
//                                String createdBy = objDataLogin.getString("createdBy");
//                                String province = objDataLogin.getString("province");
//                                String regency = objDataLogin.getString("regency");
//                                String district = objDataLogin.getString("district");
//                                String village = objDataLogin.getString("village");
//                                int stock = objDataLogin.getInt("stock");
//                                String supplierName = objDataLogin.getString("supplierName");
//                                String categoryName = objDataLogin.getString("categoryName");

                                final List<CategoryModel> categoryModels = new Gson()
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

                                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                            int tag = (int) compoundButton.getTag();
                                            booleanArrayList.set(tag, b);

                                            if(b){
                                                chip.setChipBackgroundColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorThemeGreen)));
                                                chip.setTextColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorWhite)));
                                            } else {
                                                chip.setChipBackgroundColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorThemeGreyLight)));
                                                chip.setTextColor(ColorStateList
                                                        .valueOf(activity.getResources()
                                                                .getColor(R.color.colorBlack)));
                                            }
                                            Toast.makeText(activity,
                                                    String.valueOf(booleanArrayList),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    FilterProductDashboardPopup.chipCategory.addView(chip);
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
