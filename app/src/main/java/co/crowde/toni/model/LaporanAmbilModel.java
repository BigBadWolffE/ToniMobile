package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LaporanAmbilModel implements Serializable {

    @SerializedName("bestSellingProduct")
    private String bestSellingProduct;

    @SerializedName("recapTransaction")
    private String recapTransaction;

    @SerializedName("bestCustomer")
    private String bestCustomer;

    public String getBestSellingProduct() {
        return bestSellingProduct;
    }

    public void setBestSellingProduct(String bestSellingProduct) {
        this.bestSellingProduct = bestSellingProduct;
    }

    public String getRecapTransaction() {
        return recapTransaction;
    }

    public void setRecapTransaction(String recapTransaction) {
        this.recapTransaction = recapTransaction;
    }

    public String getBestCustomer() {
        return bestCustomer;
    }

    public void setBestCustomer(String bestCustomer) {
        this.bestCustomer = bestCustomer;
    }


    public LaporanAmbilModel(String bestSellingProduct, String recapTransaction, String bestCustomer) {
        this.bestSellingProduct = bestSellingProduct;
        this.recapTransaction = recapTransaction;
        this.bestCustomer = bestCustomer;
    }
}
