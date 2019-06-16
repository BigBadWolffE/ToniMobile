package co.crowde.toni.network;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
import co.crowde.toni.controller.main.PrintController;
import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.model.body.post.AddTransaction;
import co.crowde.toni.model.body.post.UpdateProduct;
import co.crowde.toni.view.activity.notification.SuccessUpdateProduct;
import co.crowde.toni.view.activity.print.WaitingPrint;
import co.crowde.toni.view.dialog.product.InventoryDetailPopup;
import co.crowde.toni.view.dialog.product.MessageUpdateProduct;
import co.crowde.toni.view.dialog.transaction.MessageConfirmTransaction;
import co.crowde.toni.view.fragment.cart.CartPayment;
import co.crowde.toni.view.fragment.modul.Dashboard;
import co.crowde.toni.view.fragment.modul.Inventory;

import static co.crowde.toni.utils.PrinterNetwork.printText;

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
                                Dashboard.updateDataProduct(productModels);
                                Dashboard.progressDialog.dismiss();
                            } else if(message.equals("Data produk tidak ditemukan!")){
                                if (Dashboard.productModels.size() != 0){
                                    Dashboard.productModels.remove(Dashboard.productModels.size() - 1);
                                    int scrollPosition = Dashboard.productModels.size();
                                    Dashboard.productDashboardAdapter.notifyItemRemoved(scrollPosition);
                                } else {
                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                    Dashboard.productDashboardAdapter.notifyDataSetChanged();
                                }
                                Dashboard.progressDialog.dismiss();
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
                                Inventory.updateDataProduct(productModels);
                                Inventory.progressDialog.dismiss();
                            } else if(message.equals("Data produk tidak ditemukan!")){
                                if (Inventory.productModels.size() != 0){
                                    Inventory.productModels.remove(Inventory.productModels.size() - 1);
                                    int scrollPosition = Inventory.productModels.size();
                                    Inventory.inventoryAdapter.notifyItemRemoved(scrollPosition);
                                } else {
                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                    Inventory.inventoryAdapter.notifyDataSetChanged();
                                }
                                Inventory.progressDialog.dismiss();
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

    public static void postUdapteProduct(final Activity activity, String productId){

        int qty = Integer.parseInt(InventoryDetailPopup
                .etQty.getText().toString());
        int purchase = Integer.parseInt(InventoryDetailPopup
                .etPurchase.getText().toString()
                .replaceAll(",",""));
        int selling = Integer.parseInt(InventoryDetailPopup
                .etSelling.getText().toString()
                .replaceAll(",",""));

        final UpdateProduct update = new UpdateProduct();
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
                                Inventory.productModels.clear();
                                page=1;
                                getInventoryList(activity);
                                MessageUpdateProduct.dialogUpdate.dismiss();
                                MessageUpdateProduct.progressDialog.dismiss();
                                InventoryDetailPopup.alertDialog.dismiss();
                                Intent success = new Intent(activity, SuccessUpdateProduct.class);
                                activity.startActivity(success);

                            } else{
                                if(message.equals("Token tidak valid")){
                                    UserController.tokenExpired(activity, message);
                                } else {
                                    MessageUpdateProduct.progressDialog.dismiss();
                                    MessageUpdateProduct.dialogUpdate.dismiss();
                                    InventoryDetailPopup.alertDialog.dismiss();
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
