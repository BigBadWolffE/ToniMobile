package co.crowde.toni.network;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.adapter.CatalogProductAdapter;
import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CatalogModel;
import co.crowde.toni.model.CatalogRequestModel;
import co.crowde.toni.view.activity.catalog.CatalogProduct;
import co.crowde.toni.view.activity.notification.SuccessAddNewProduct;
import co.crowde.toni.view.dialog.message.network.NetworkOfflineDialog;
import co.crowde.toni.view.fragment.modul.Inventory;

public class CatalogRequest {

    //Add New Product
    public static int page;
    public static String message, productName, supplierId, categoryId;
    public static int totalData = 0, succed = 0, failed = 0;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void getCatalogList(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Catalog+"?limit=10&page="+page
                        +"&productName="+productName
                        +"&categoryId="+categoryId
                        +"&supplierId="+supplierId)
                .build();

        Log.e("URL",API.Catalog+"?limit=10&page="+page
                +"&productName="+productName
                +"&categoryId="+categoryId
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
                                List<CatalogModel> productModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<CatalogModel>>() {
                                                }.getType());

                                CatalogProduct.updateDataProduct(productModels);
                                CatalogProduct.progressDialog.dismiss();

                            } else if(message.equals("Data produk tidak ditemukan!")){
                                if (CatalogProduct.productModels.size() != 0){
                                    CatalogProduct.productModels.remove(CatalogProduct.productModels.size() - 1);
                                    int scrollPosition = CatalogProduct.productModels.size();
                                    CatalogProduct.catalogProductAdapter.notifyItemRemoved(scrollPosition);
                                } else {
                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                    CatalogProduct.catalogProductAdapter.notifyDataSetChanged();
                                }
                                CatalogProduct.progressDialog.dismiss();
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

    public static void addNewProduct(final Activity activity){
        ArrayList<CatalogModel> models = ((CatalogProductAdapter)
                CatalogProduct.rcProduct.getAdapter()).getSelectList();
        ArrayList<CatalogRequestModel> arrayList = new ArrayList<>();
        if(models.size()>0){
            for (int i=0;i<models.size();i++){
                String shopId = SavePref.readShopId(activity);
                String productId = models.get(i).getProductId();
                String categoriId = models.get(i).getCategoryId();
                String supplierId = models.get(i).getSupplierId();
                String productName = models.get(i).getProductName();
                String description = models.get(i).getDescription();
                String picture = models.get(i).getPicture();
                String unit = models.get(i).getUnit();
                int sellingPrice = models.get(i).getSellingPrice();
                int purchasePrice = models.get(i).getPurchasePrice();

                CatalogRequestModel requestModel =
                        new CatalogRequestModel(
                                shopId,
                                productId,
                                categoriId,
                                supplierId,
                                productName,
                                description,
                                picture,
                                unit,
                                purchasePrice,
                                sellingPrice);

                arrayList.add(requestModel);
            }
        } else{
            Toast.makeText(activity, "NULL!!!", Toast.LENGTH_SHORT).show();
        }

        String postBody = new Gson().toJson(arrayList);
        Log.e("BODY", postBody);

        OkHttpClient client = new OkHttpClient();
//                client.cache();

        RequestBody body = RequestBody.create(JSON, postBody);
        Log.e("REQUEST BODY",body.toString());

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.AddNewProduct)
                .post(body)
                .build();

        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
//                Toast.makeText(activity, "HTTP Request Failure", Toast.LENGTH_SHORT).show();
                NetworkOfflineDialog.showDialog(activity);
                e.printStackTrace();
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
                                JSONObject jsonObj = new JSONObject(data);
                                String recap = jsonObj.getString("recap");

                                JSONObject jsonObject = new JSONObject(recap);
                                totalData = jsonObject.getInt("totalData");
                                succed = jsonObject.getInt("succed");
                                failed = jsonObject.getInt("failed");

                                Toast.makeText(activity, "Total Data: " +totalData +"\n"+
                                                "Total Success: " +succed +"\n"+
                                                "Total Failed: " +failed
                                        , Toast.LENGTH_LONG).show();

                                Inventory.productModels.clear();
                                page=1;
                                ProductRequest.getInventoryList(activity);
                                Intent success = new Intent(activity, SuccessAddNewProduct.class);
                                activity.startActivity(success);
                                activity.finish();

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
