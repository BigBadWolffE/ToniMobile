package co.crowde.toni.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import co.crowde.toni.constant.Const;
import co.crowde.toni.controller.user.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.model.body.post.UpdateProductModel;
import co.crowde.toni.view.activity.notification.SuccessUpdateProductActivity;
import co.crowde.toni.view.dialog.message.network.NetworkOfflineDialog;
import co.crowde.toni.view.dialog.message.product.UpdateProductDialog;
import co.crowde.toni.view.dialog.popup.product.InventoryDetailPopup;
import co.crowde.toni.view.fragment.modul.DashboardFragment;
import co.crowde.toni.view.fragment.modul.InventoryFragment;

import static co.crowde.toni.utils.print.PrinterNetwork.printText;

public class ProductRequest {

    public static String message;
    public static int page;
    public static String productName, categoryId, status, supplierId;

    public static void getProductList(final Activity activity){

        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Product+API.Slash
                        +SavePref.readShopId(activity)
                        +"?limit=10&page="+page
                        +"&filter="+productName
                        +"&categoryId="+categoryId
                        +"&status="+status
                        +"&supplierId="+supplierId)
                .build();

        Log.e("URL",API.Product+API.Slash
                +SavePref.readShopId(activity)
                +"?limit=10&page="+page
                +"&filter="+productName
                +"&categoryId="+categoryId
                +"&status="+status
                +"&supplierId="+supplierId);

        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(
//                                activity, "HTTP Request Failure", Toast.LENGTH_SHORT).show();
                        NetworkOfflineDialog.showDialog(activity);
                        DashboardFragment.progressDialog.dismiss();
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
                                DashboardFragment.updateDataProduct(productModels, page);
                                DashboardFragment.progressDialog.dismiss();
                                DashboardFragment.showListField(activity);
                            } else if(message.equals("Data produk tidak ditemukan!")){
                                if (DashboardFragment.productModels.size() != 0){
                                    DashboardFragment.productModels.remove(DashboardFragment.productModels.size() - 1);
                                    int scrollPosition = DashboardFragment.productModels.size();
                                    DashboardFragment.productDashboardAdapter.notifyItemRemoved(scrollPosition);
                                    DashboardFragment.progressDialog.dismiss();
                                } else {
                                    DashboardFragment.productModels.clear();
                                    DashboardFragment.productDashboardAdapter.replaceItemFiltered(DashboardFragment.productModels);
                                    DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                                    DashboardFragment.progressDialog.dismiss();
                                    DashboardFragment.showListField(activity);
                                }
                            } else{
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
                .url(API.Product+API.Slash
                        +SavePref.readShopId(activity)
                        +"?limit=20&page="+page
                        +"&filter="+productName
                        +"&categoryId="+categoryId
                        +"&status="+status
                        +"&supplierId="+supplierId)
                .build();

        Log.e("URL",API.Product+API.Slash
                +SavePref.readShopId(activity)
                +"?limit=20&page="+page
                +"&filter="+productName
                +"&categoryId="+categoryId
                +"&status="+status
                +"&supplierId="+supplierId);

        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(
//                                activity, "HTTP Request Failure", Toast.LENGTH_SHORT).show();
                        NetworkOfflineDialog.showDialog(activity);
                        InventoryFragment.progressDialog.dismiss();
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
                                InventoryFragment.updateDataProduct(productModels, page);
                                InventoryFragment.progressDialog.dismiss();
                                InventoryFragment.showListField(activity);
                            } else if(message.equals("Data produk tidak ditemukan!")){
                                if (InventoryFragment.productModels.size() != 0){
                                    InventoryFragment.productModels.remove(InventoryFragment.productModels.size() - 1);
                                    int scrollPosition = InventoryFragment.productModels.size();
                                    InventoryFragment.inventoryAdapter.notifyItemRemoved(scrollPosition);
                                    InventoryFragment.progressDialog.dismiss();
                                } else {
                                    InventoryFragment.productModels.clear();
                                    InventoryFragment.inventoryAdapter.replaceItemFiltered(InventoryFragment.productModels);
                                    InventoryFragment.inventoryAdapter.notifyDataSetChanged();
                                    InventoryFragment.progressDialog.dismiss();
                                    InventoryFragment.showListField(activity);
                                }
                            } else{
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

    public static void postUpdateProduct(final Activity activity, String productId, int qty, int purchase, int selling, ProgressDialog progressDialog){
//        int qty = 0;
//        if(InventoryDetailPopup.etQty.getText().length()>0){
//            qty = Integer.parseInt(InventoryDetailPopup
//                    .etQty.getText().toString());
//        }
//
//        int purchase = 0;
//        if(InventoryDetailPopup.etPurchase.getText().length()>0){
//            purchase = Integer.parseInt(InventoryDetailPopup
//                    .etPurchase.getText().toString()
//                    .replaceAll(",",""));
//        }
//
//        int selling = 0;
//        if(InventoryDetailPopup.etSelling.getText().length()>0){
//            selling = Integer.parseInt(InventoryDetailPopup
//                    .etSelling.getText().toString()
//                    .replaceAll(",",""));
//        }

        final UpdateProductModel update = new UpdateProductModel();
        update.setShopId(SavePref.readShopId(activity));
        update.setProductId(productId);
        update.setQuantity(qty);
        update.setPurchasePrice(purchase);
        update.setSellingPrice(selling);
        update.setType("Increase");

        String postBody = new Gson().toJson(update);
        Log.e("POST BODY", postBody);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(Const.JSON, postBody);
        Log.e("REQUEST BODY",body.toString());

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Warehouse)
                .post(body)
                .build();

        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(
//                                activity, "HTTP Request Failure", Toast.LENGTH_SHORT).show();
                        NetworkOfflineDialog.showDialog(activity);
                        progressDialog.dismiss();
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
                                InventoryFragment.productModels.clear();
                                page=1;
                                getInventoryList(activity);
                                UpdateProductDialog.dialogConfirm.dismiss();
                                progressDialog.dismiss();
                                Intent success = new Intent(activity, SuccessUpdateProductActivity.class);
                                activity.startActivity(success);
                                activity.finish();

                            } else{
                                if(message.equals("Token tidak valid")){
                                    UserController.tokenExpired(activity, message);
                                } else {
                                    UpdateProductDialog.progressDialog.dismiss();
                                    UpdateProductDialog.dialogConfirm.dismiss();
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
