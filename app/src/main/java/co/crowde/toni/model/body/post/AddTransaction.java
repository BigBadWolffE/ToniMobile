package co.crowde.toni.model.body.post;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.model.CartModel;

public class AddTransaction {

    @SerializedName("shopId")
    private String shopId;

    @SerializedName("customerId")
    private String customerId;

    @SerializedName("paymentType")
    private String paymentType;

    @SerializedName("amount")
    private String amount;

    @SerializedName("paid")
    private String paid;

    @SerializedName("_change")
    private String _change;

    @SerializedName("details")
    private List<CartModel> details;

    public AddTransaction() {
    }

    public AddTransaction(String shopId, String customerId, String paymentType, String amount, String paid, String _change, List<CartModel> details) {
        this.shopId = shopId;
        this.customerId = customerId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.paid = paid;
        this._change = _change;
        this.details = details;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String get_change() {
        return _change;
    }

    public void set_change(String _change) {
        this._change = _change;
    }

    public List<CartModel> getDetails() {
        return details;
    }

    public void setDetails(List<CartModel> details) {
        this.details = details;
    }
}
