package co.crowde.toni.controller.network;

import android.app.Activity;
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
import java.util.List;

import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.model.UserModel;
import co.crowde.toni.view.fragment.Customer;
import co.crowde.toni.view.fragment.Dashboard;
import co.crowde.toni.view.login.Login;
import co.crowde.toni.view.main.SelectCustomer;
import co.crowde.toni.view.popup.AddNewCustomerPopup;

public class CustomerRequest {

    public static String message;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void getCustomerList(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Customer+API.Slash+SavePref.readShopId(activity)+API.Count)
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
                                List<CustomerModel> customerModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<CustomerModel>>() {
                                                }.getType());
                                Log.e("ProductModels", new Gson().toJson(customerModels));

//                                Dashboard.productModels = new ArrayList<>();
                                SelectCustomer.customerModels.clear();
                                SelectCustomer.customerModels.addAll(customerModels);
                                SelectCustomer.customerAdapter.notifyDataSetChanged();
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

    public static void addNewCustomer(final Activity activity){
        String username = SelectCustomer.etName.getText().toString();
        String phone = SelectCustomer.etPhone.getText().toString();

        final CustomerModel user = new CustomerModel();
        user.setShopId(SavePref.readShopId(activity));
        user.setCustomerName(username);
        user.setAddress("");
        user.setPhone(phone);

        String postBody = new Gson().toJson(user);
        Log.e("POST BODY", postBody);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);
        Log.e("REQUEST BODY",body.toString());

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Customer+API.Slash)
                .post(body)
                .build();

        Log.e("URL",API.Customer);

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
                                Toast.makeText(activity, "Penambahan data pelanggan berhasil", Toast.LENGTH_SHORT).show();
                                SelectCustomer.customerModels.clear();
                                SelectCustomer.customerAdapter.notifyDataSetChanged();
                                SelectCustomer.alertDialog.dismiss();
                                SelectCustomer.loadCustomerList(activity);

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
