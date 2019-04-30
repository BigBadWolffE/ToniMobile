package co.crowde.toni.controller.network;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
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
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.fragment.Inventory;
import co.crowde.toni.view.main.CatalogProduct;

public class CatalogRequest {

    //Add New Product
    public static String message;
    public static int totalData = 0, succed = 0, failed = 0;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void getCatalogList(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Catalog+API.Count)
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
                                List<CatalogModel> productModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<CatalogModel>>() {
                                                }.getType());
//                                Log.e("ProductModels", new Gson().toJson(productModels));

                                CatalogProduct.productModels.clear();
                                CatalogProduct.productModels.addAll(productModels);
                                CatalogProduct.catalogProductAdapter.notifyDataSetChanged();

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
                Toast.makeText(activity, "HTTP Request Failure", Toast.LENGTH_SHORT).show();
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

                                CatalogProduct.dialog.dismiss();

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
