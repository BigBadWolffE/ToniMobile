package co.crowde.toni.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

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
import co.crowde.toni.controller.user.UserController;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.TransactionProductModel;
import co.crowde.toni.model.body.post.AddTransactionModel;
import co.crowde.toni.model.response.list.TransactionModel;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.notification.SuccessPaymentTransactionActivity;
import co.crowde.toni.view.activity.transaction.DetailTransactionActivity;
import co.crowde.toni.view.dialog.message.network.NetworkOfflineDialog;
import co.crowde.toni.view.dialog.message.transaction.ConfirmPaymentDialog;
import co.crowde.toni.view.fragment.transaction.ListTransactionReportFragment;

import static co.crowde.toni.view.fragment.modul.ReportFragment.progressDialog;
import static co.crowde.toni.view.fragment.transaction.ListTransactionReportFragment.reportListTransactionAdapter;
import static co.crowde.toni.view.fragment.transaction.ListTransactionReportFragment.transactionModels;
import static co.crowde.toni.view.fragment.transaction.ListTransactionReportFragment.updateDataTransaction;

public class TransactionRequest {

    public static int page;
    public static String message;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void postNewTransaction(final Activity activity, AddTransactionModel add, int saldo, int credit, ProgressDialog progressDialog) {

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
                                ConfirmPaymentDialog.dialogConfirm.dismiss();
                                progressDialog.dismiss();

                                Intent print = new Intent(activity, SuccessPaymentTransactionActivity.class);
                                print.putExtra("data", data);

                                switch (add.getPaymentType()) {
                                    case "Cash":
                                        if(credit>0){
                                            CustomerRequest.payCredit(activity, credit, data);
                                            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION, Const.MODUL_TRANSACTION, Const.LABEL_TRANSACTION_CASH_CREDIT_SUCCESS);
                                            print.putExtra("payment_type", "CashCredit");
                                        } else {
                                            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION, Const.MODUL_TRANSACTION, Const.LABEL_TRANSACTION_CASH_SUCCESS);
                                            print.putExtra("payment_type", add.getPaymentType());
                                        }
                                        break;
                                    case "Credit":
                                        AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION, Const.MODUL_TRANSACTION, Const.LABEL_TRANSACTION_CASH_CREDIT_SUCCESS);
                                        print.putExtra("payment_type", add.getPaymentType());
                                        break;
                                }

                                print.putExtra("credit", ""+credit);
                                print.putExtra("saldo", ""+saldo);
                                activity.startActivityForResult(print, Const.KEY_SUCCESS_PAYMENT);
                                activity.finish();

                            } else{
                                if(message.equals("Token tidak valid")){
                                    UserController.tokenExpired(activity, message);
                                } else {
                                    ConfirmPaymentDialog.dialogConfirm.dismiss();
                                    progressDialog.dismiss();
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

    public static void getTransaction(final Activity activity, String strDate, String endDate){

        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Transaction+API.Slash
                        +SavePref.readShopId(activity)
                        +"?limit=10&page="+page
                        +"&startDate="+strDate
                        +"&endDate="+endDate)
                .build();

        Log.e("URL",API.Transaction+API.Slash
                +SavePref.readShopId(activity)
                +"?limit=10&page="+page
                +"&startDate="+strDate
                +"&endDate="+endDate);

        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                                List<TransactionModel> transactionModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<TransactionModel>>() {
                                                }.getType());
                                updateDataTransaction(transactionModels, page);
                                progressDialog.dismiss();
                                ListTransactionReportFragment.showListField(activity);
                            } else if(message.equals("Data transaksi tidak ditemukan!")){
                                if (transactionModels.size() != 0){
                                    transactionModels.remove(transactionModels.size() - 1);
                                    int scrollPosition = transactionModels.size();
                                    reportListTransactionAdapter.notifyItemRemoved(scrollPosition);
                                    progressDialog.dismiss();
                                } else {
                                    transactionModels.clear();
                                    reportListTransactionAdapter.replaceItemFiltered(transactionModels);
                                    reportListTransactionAdapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                    ListTransactionReportFragment.showListField(activity);
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

    public static void getTransactionProductList(final Activity activity, String transactionId){
        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Transaction+API.Slash+"detail/"
                        +SavePref.readShopId(activity)
                        +"/"+transactionId)
                .build();

        Log.e("URL",API.Transaction+API.Slash+"detail/"
                +SavePref.readShopId(activity)
                +"/"+transactionId);

        client.newCall(requestHttp).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkOfflineDialog.showDialog(activity);
                        DetailTransactionActivity.progressDialog.dismiss();
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
                                List<TransactionProductModel> productModels = new Gson()
                                        .fromJson(data,
                                                new TypeToken<List<TransactionProductModel>>() {
                                                }.getType());

                                DetailTransactionActivity.productModelList.addAll(productModels);
                                DetailTransactionActivity.progressDialog.dismiss();
                                DetailTransactionActivity.productListAdapter.replaceItemFiltered(DetailTransactionActivity.productModelList);

                            }else{
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
