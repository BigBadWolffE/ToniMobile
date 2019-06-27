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
import java.util.List;

import co.crowde.toni.constant.Const;
import co.crowde.toni.controller.print.PrintController;
import co.crowde.toni.controller.user.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.response.object.CreditPayModel;
import co.crowde.toni.view.activity.customer.SelectCustomerActivity;
import co.crowde.toni.view.activity.notification.SuccessCreditPayActivity;
import co.crowde.toni.view.dialog.message.customer.CreditPayDialog;
import co.crowde.toni.view.dialog.message.customer.CustomerAlreadyRegisterDialog;
import co.crowde.toni.view.dialog.message.network.NetworkOfflineDialog;
import co.crowde.toni.view.fragment.cart.CartPaymentFragment;
import co.crowde.toni.view.fragment.modul.CustomerFragment;

public class CustomerRequest {

    public static int page;
    public static String message, customerName, customerId, namaPelanggan;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void getCustomerList(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Customer+API.Slash
                        +SavePref.readShopId(activity)
                        +"?limit=20&page="+page
                        +"&customerName="+customerName)
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
                        SelectCustomerActivity.progressDialog.dismiss();
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

                                SelectCustomerActivity.updateDataProduct(customerModels, page);
                                SelectCustomerActivity.progressDialog.dismiss();
                                SelectCustomerActivity.showListField(activity);

                            } else if(message.equals("Data pelanggan tidak ditemukan!")){
                                if (SelectCustomerActivity.customerModels.size() != 0){
                                    SelectCustomerActivity.customerModels.remove(SelectCustomerActivity.customerModels.size() - 1);
                                    int scrollPosition = SelectCustomerActivity.customerModels.size();
                                    SelectCustomerActivity.customerAdapter.notifyItemRemoved(scrollPosition);
                                    SelectCustomerActivity.progressDialog.dismiss();
                                } else {
                                    SelectCustomerActivity.customerModels.clear();
                                    SelectCustomerActivity.customerAdapter.replaceItemFiltered(SelectCustomerActivity.customerModels);
                                    SelectCustomerActivity.customerAdapter.notifyDataSetChanged();
                                    SelectCustomerActivity.progressDialog.dismiss();
                                    SelectCustomerActivity.showListField(activity);
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

    public static void getCustomerModulList(final Activity activity){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Customer+API.Slash
                        +SavePref.readShopId(activity)
                        +"?limit=20&page="+page
                        +"&customerName="+namaPelanggan)
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
                        CustomerFragment.progressDialog.dismiss();
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

                                CustomerFragment.updateDataCustomer(customerModels, page);
                                CustomerFragment.progressDialog.dismiss();
                                CustomerFragment.showListField(activity);

                            } else if(message.equals("Data pelanggan tidak ditemukan!")){
                                if (CustomerFragment.customerModelList.size() != 0){
                                    CustomerFragment.customerModelList.remove(CustomerFragment.customerModelList.size() - 1);
                                    int scrollPosition = CustomerFragment.customerModelList.size();
                                    CustomerFragment.transaksiBagianPelangganAdapter.notifyItemRemoved(scrollPosition);
                                    CustomerFragment.progressDialog.dismiss();
                                } else {
                                    CustomerFragment.customerModelList.clear();
                                    CustomerFragment.transaksiBagianPelangganAdapter.replaceItemFiltered(CustomerFragment.customerModelList);
                                    CustomerFragment.transaksiBagianPelangganAdapter.notifyDataSetChanged();
                                    CustomerFragment.progressDialog.dismiss();
                                    CustomerFragment.showListField(activity);
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

    public static void addNewCustomer(final Activity activity){
        String username = SelectCustomerActivity.etName.getText().toString();
        String phone = SelectCustomerActivity.etPhone.getText().toString();

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
                                Toast.makeText(activity, "Penambahan data pelanggan berhasil", Toast.LENGTH_SHORT).show();
                                SelectCustomerActivity.customerModels.clear();
                                page=1;
                                getCustomerList(activity);
                                SelectCustomerActivity.progressDialog.dismiss();
                                SelectCustomerActivity.alertDialog.dismiss();

                            } else {
                                if(message.equals("Token tidak valid")){
                                    UserController.tokenExpired(activity, message);

                                } else if(message.equals("Internal server error!")){
                                    SelectCustomerActivity.progressDialog.dismiss();
                                    CustomerAlreadyRegisterDialog.showDialog(activity);
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

    public static void addNewCustomerModul(final Activity activity){
        String username = CustomerFragment.etName.getText().toString();
        String phone = CustomerFragment.etPhone.getText().toString();

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
                                Toast.makeText(activity, "Penambahan data pelanggan berhasil", Toast.LENGTH_SHORT).show();
                                CustomerFragment.customerModelList.clear();
                                page=1;
                                getCustomerModulList(activity);
                                CustomerFragment.progressDialog.dismiss();
                                CustomerFragment.alertDialog.dismiss();

                            } else {
                                if(message.equals("Token tidak valid")){
                                    UserController.tokenExpired(activity, message);

                                } else if(message.equals("Internal server error!")){
                                    CustomerFragment.progressDialog.dismiss();
                                    CustomerAlreadyRegisterDialog.showDialog(activity);
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

    public static void payCredit(final Activity activity){

        final CreditPayModel pay = new CreditPayModel();
        pay.setShopId(SavePref.readShopId(activity));
        pay.setCustomerId(SavePref.readCustomerId(activity));
        pay.setAmount(String.valueOf(CartPaymentFragment.creditPay));

        String postBody = new Gson().toJson(pay);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);
        Log.e("REQUEST BODY",body.toString());

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.CreditPaid)
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
                        Log.e("Error",e.toString());
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
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
                                Log.e("Res", data);

                            } else {
                                if(message.equals("Token tidak valid")){
                                    UserController.tokenExpired(activity, message);

                                } else if(message.equals("Internal server error!")){
                                    SelectCustomerActivity.progressDialog.dismiss();
                                    Toast.makeText(activity, "Nama Pelanggan atau Nomor Telepon sudah terdaftar.", Toast.LENGTH_SHORT).show();
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

    public static void payCustomerCredit(final Activity activity, int credit, String customerId){

        final CreditPayModel pay = new CreditPayModel();
        pay.setShopId(SavePref.readShopId(activity));
        pay.setCustomerId(String.valueOf(customerId));
        pay.setAmount(String.valueOf(credit));

        String postBody = new Gson().toJson(pay);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);
        Log.e("REQUEST BODY",body.toString());

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.CreditPaid)
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
                        CreditPayDialog.dialogCredit.dismiss();
                        CreditPayDialog.progressDialog.dismiss();
                        Log.e("Error",e.toString());
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
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
                                Log.e("Res", data);
                                CreditPayModel model = new Gson().fromJson(data, CreditPayModel.class);
                                PrintController.printCustomerCreditPay(activity, model, credit);
                                CreditPayDialog.dialogCredit.dismiss();
                                CreditPayDialog.progressDialog.dismiss();

                                Intent print = new Intent(activity, SuccessCreditPayActivity.class);
                                print.putExtra("saldo",data);
                                activity.startActivity(print);
//                                activity.finish();

                            } else {
                                if(message.equals("Token tidak valid")){
                                    UserController.tokenExpired(activity, message);
                                    CreditPayDialog.dialogCredit.dismiss();
                                    CreditPayDialog.progressDialog.dismiss();
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
