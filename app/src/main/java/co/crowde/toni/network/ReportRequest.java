package co.crowde.toni.network;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import co.crowde.toni.controller.user.UserController;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.response.ReportDashboardModel;
import co.crowde.toni.model.response.list.CustomerFavoriteModel;
import co.crowde.toni.model.response.list.ProductFavoriteModel;
import co.crowde.toni.model.response.object.RecapTransactionModel;
import co.crowde.toni.view.dialog.message.network.NetworkOfflineDialog;
import co.crowde.toni.view.fragment.transaction.DashboardReportFragment;

import static co.crowde.toni.view.fragment.modul.ReportFragment.progressDialog;
import static co.crowde.toni.view.fragment.transaction.DashboardReportFragment.customerFavoriteAdapter;
import static co.crowde.toni.view.fragment.transaction.DashboardReportFragment.customerFavoriteModels;
import static co.crowde.toni.view.fragment.transaction.DashboardReportFragment.productFavoriteAdapter;
import static co.crowde.toni.view.fragment.transaction.DashboardReportFragment.productFavoriteModels;
import static co.crowde.toni.view.fragment.transaction.DashboardReportFragment.tvCustomerEmpty;
import static co.crowde.toni.view.fragment.transaction.DashboardReportFragment.tvFrequencyTransaction;
import static co.crowde.toni.view.fragment.transaction.DashboardReportFragment.tvMeanTransaction;
import static co.crowde.toni.view.fragment.transaction.DashboardReportFragment.tvProductEmpty;
import static co.crowde.toni.view.fragment.transaction.DashboardReportFragment.tvTotalTransaction;

public class ReportRequest {
    public static String message;
    public static int page;
    public static String status;

    public static void getReport(final Activity activity, String strDate, String endDate){

        OkHttpClient client = new OkHttpClient();

        Request requestHttp = new Request.Builder()
                .header("Authorization", SavePref.readToken(activity))
                .url(API.Report+"/mobile/"
                        +SavePref.readShopId(activity)
                        +"/"+strDate
                        +"/"+endDate)
                .build();

        Log.e("URL",API.Report+API.Slash+"mobile/"
                +SavePref.readShopId(activity)
                +"/"+strDate
                +"/"+endDate);

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
                                JSONObject responseObj = new JSONObject(data);
                                String recap = responseObj.getString("recapTransaction");

                                RecapTransactionModel recapTransactionModel = new Gson()
                                        .fromJson(recap, RecapTransactionModel.class);

                                if(productFavoriteModels.size()>0){
                                    productFavoriteModels.clear();
                                    customerFavoriteModels.clear();
                                    productFavoriteAdapter.replaceItemFiltered(productFavoriteModels);
                                    customerFavoriteAdapter.replaceItemFiltered(customerFavoriteModels);
                                }

                                if(recapTransactionModel.getCount()!=0){
                                    DecimalFormatRupiah.changeFormat(activity);
                                    tvTotalTransaction
                                            .setText("Rp." +DecimalFormatRupiah.formatNumber
                                                    .format(recapTransactionModel.getAmount())+",-");
                                    tvFrequencyTransaction
                                            .setText(String.valueOf(recapTransactionModel.getCount()));

                                    int average = (int) Math.ceil((double)recapTransactionModel.getAvarage());
                                    tvMeanTransaction
                                            .setText("Rp." +DecimalFormatRupiah.formatNumber
                                                    .format(average)+",-");

                                    String bestProduct = responseObj.getString("bestSellingProduct");

                                    List<ProductFavoriteModel> product = new Gson()
                                            .fromJson(bestProduct,
                                                    new TypeToken<List<ProductFavoriteModel>>() {
                                                    }.getType());

                                    productFavoriteModels.addAll(product);
                                    productFavoriteAdapter.replaceItemFiltered(product);

                                    String bestCustomer = responseObj.getString("bestCustomer");

                                    List<CustomerFavoriteModel> customer = new Gson()
                                            .fromJson(bestCustomer,
                                                    new TypeToken<List<CustomerFavoriteModel>>() {
                                                    }.getType());
                                    customerFavoriteModels.addAll(customer);
                                    customerFavoriteAdapter.replaceItemFiltered(customer);

                                    tvProductEmpty.setVisibility(View.GONE);
                                    tvCustomerEmpty.setVisibility(View.GONE);

                                } else {
                                    tvTotalTransaction
                                            .setText("Rp. 0,-");
                                    tvFrequencyTransaction
                                            .setText("0");
                                    tvMeanTransaction
                                            .setText("Rp. 0,-");
                                    tvProductEmpty.setVisibility(View.VISIBLE);
                                    tvCustomerEmpty.setVisibility(View.VISIBLE);
                                }

                                progressDialog.dismiss();


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
