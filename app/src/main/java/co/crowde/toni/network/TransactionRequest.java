package co.crowde.toni.network;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
import co.crowde.toni.controller.main.PrintController;
import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.body.post.AddTransaction;
import co.crowde.toni.view.activity.print.WaitingPrint;
import co.crowde.toni.view.dialog.transaction.MessageConfirmTransaction;
import co.crowde.toni.view.fragment.cart.CartPayment;
import co.crowde.toni.view.fragment.modul.Dashboard;

public class TransactionRequest {

    public static int page;
    public static String message;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void postNewTransaction(final Activity activity) {

        final CustomerModel credit = new Gson().fromJson(SavePref.readCustomer(activity), CustomerModel.class);
        String tambahKeranjang = new Gson().toJson(Dashboard.cartModels);
        Log.e("KeranjangList", tambahKeranjang);

        final AddTransaction add = new AddTransaction();
        add.setShopId(SavePref.readShopId(activity));
        add.setCustomerId(credit.getCustomerId());
        add.setPaymentType(CartPayment.paymentType);
        add.setAmount(String.valueOf(Dashboard.totalAmount));
        add.setPaid(String.valueOf(CartPayment.nominal).replaceAll(",",""));
        add.set_change(String.valueOf(CartPayment.change));
        add.setDetails(Dashboard.cartModels);

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
                                MessageConfirmTransaction.dialogConfirm.dismiss();
                                MessageConfirmTransaction.progressDialog.dismiss();

                                if(CartPayment.cash){
                                    PrintController.printCash(activity, data);
                                } else if(CartPayment.credit){
                                    PrintController.printCredit(activity, data, credit);
                                } else if(CartPayment.cashCredit){
                                    PrintController.printCashCredit(activity, data);
                                    CustomerRequest.payCredit(activity);
                                }

                                Intent print = new Intent(activity, WaitingPrint.class);
                                activity.startActivity(print);
                                activity.finish();

                            } else{
                                if(message.equals("Token tidak valid")){
                                    UserController.tokenExpired(activity, message);
                                } else {
                                    MessageConfirmTransaction.dialogConfirm.dismiss();
                                    MessageConfirmTransaction.progressDialog.dismiss();
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
