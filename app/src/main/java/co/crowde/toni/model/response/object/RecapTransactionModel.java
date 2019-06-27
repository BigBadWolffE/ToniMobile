package co.crowde.toni.model.response.object;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class RecapTransactionModel {

    @SerializedName("amount")
    private int amount;

    @SerializedName("count")
    private int count;

    @SerializedName("avarage")
    private double avarage;

    public RecapTransactionModel() {
    }

    public RecapTransactionModel(int amount, int count, double avarage) {
        this.amount = amount;
        this.count = count;
        this.avarage = avarage;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getAvarage() {
        return avarage;
    }

    public void setAvarage(double avarage) {
        this.avarage = avarage;
    }
}
