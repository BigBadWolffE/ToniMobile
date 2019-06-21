package co.crowde.toni.network;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import co.crowde.toni.constant.Const;
import co.crowde.toni.controller.print.PrintController;
import co.crowde.toni.controller.user.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.body.post.AddTransactionModel;
import co.crowde.toni.view.activity.print.WaitingPrintActivity;
import co.crowde.toni.view.dialog.message.network.NetworkOfflineDialog;
import co.crowde.toni.view.dialog.message.transaction.ConfirmTransactionDialog;
import co.crowde.toni.view.fragment.cart.CartPaymentFragment;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class TransactionRequest {

    public static int page;
    public static String message;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void postNewTransaction(final Activity activity) {

        final CustomerModel credit = new Gson().fromJson(SavePref.readCustomer(activity), CustomerModel.class);
        String tambahKeranjang = new Gson().toJson(DashboardFragment.cartModels);
        Log.e("KeranjangList", tambahKeranjang);

        final AddTransactionModel add = new AddTransactionModel();
        add.setShopId(SavePref.readShopId(activity));
        add.setCustomerId(credit.getCustomerId());
        add.setPaymentType(CartPaymentFragment.paymentType);
        add.setAmount(String.valueOf(DashboardFragment.totalAmount));
        add.setPaid(String.valueOf(CartPaymentFragment.nominal).replaceAll(",",""));
        add.set_change(String.valueOf(CartPaymentFragment.change));
        add.setDetails(DashboardFragment.cartModels);

        String postBody = new Gson().toJson(add);
        Log.e("POST BODY", postBody);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(Const.JSON, postBody);
        Log.e("REQUEST BODY",new Gson().toJson(body));

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Transaction)
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
                                ConfirmTransactionDialog.dialogConfirm.dismiss();
                                ConfirmTransactionDialog.progressDialog.dismiss();

                                if(CartPaymentFragment.cash){
                                    PrintController.printCash(activity, data);
                                } else if(CartPaymentFragment.credit){
                                    PrintController.printCredit(activity, data, credit);
                                } else if(CartPaymentFragment.cashCredit){
                                    PrintController.printCashCredit(activity, data);
                                    CustomerRequest.payCredit(activity);
                                }

                                SavePref.saveCustomerId(activity, null);
                                SavePref.saveCustomer(activity, null);

                                Intent print = new Intent(activity, WaitingPrintActivity.class);
                                activity.startActivity(print);
                                activity.finish();

                            } else{
                                if(message.equals("Token tidak valid")){
                                    UserController.tokenExpired(activity, message);
                                } else {
                                    ConfirmTransactionDialog.dialogConfirm.dismiss();
                                    ConfirmTransactionDialog.progressDialog.dismiss();
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
