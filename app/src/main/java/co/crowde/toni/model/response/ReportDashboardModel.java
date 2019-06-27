package co.crowde.toni.model.response;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReportDashboardModel {

    @SerializedName("recapTransaction")
    private String recapTransaction;

    @SerializedName("bestSellingProduct")
    private String bestSellingProduct;

    @SerializedName("bestCustomer")
    private String bestCustomer;

    public ReportDashboardModel() {
    }

    public ReportDashboardModel(String recapTransaction, String bestSellingProduct, String bestCustomer) {
        this.recapTransaction = recapTransaction;
        this.bestSellingProduct = bestSellingProduct;
        this.bestCustomer = bestCustomer;
    }

    public String getRecapTransaction() {
        return recapTransaction;
    }

    public void setRecapTransaction(String recapTransaction) {
        this.recapTransaction = recapTransaction;
    }

    public String getBestSellingProduct() {
        return bestSellingProduct;
    }

    public void setBestSellingProduct(String bestSellingProduct) {
        this.bestSellingProduct = bestSellingProduct;
    }

    public String getBestCustomer() {
        return bestCustomer;
    }

    public void setBestCustomer(String bestCustomer) {
        this.bestCustomer = bestCustomer;
    }
}
