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
import co.crowde.toni.model.response.list.SupplierModel;
import co.crowde.toni.view.activity.filter.CatalogFilter;
import co.crowde.toni.view.dialog.message.network.NetworkOfflineDialog;

public class SupplierRequest {

    public static String message;
    public static ArrayList<SupplierModel> supplierModels = new ArrayList<>();

    public static void getSupplierCatalog(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Supplier+API.Count)
                .build();

        Log.e("URL",API.Supplier+API.Count);

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
                                supplierModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<SupplierModel>>() {
                                                }.getType());
                                for(int i=0; i<supplierModels.size();i++){
                                    String nama = supplierModels.get(i).getSupplierName();
                                    String id = supplierModels.get(i).getSupplierId();
                                    CatalogFilter.supplierModels.add(new SupplierModel(
                                            id,nama));
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
