package co.crowde.toni.model.body.post;

import com.google.gson.annotations.SerializedName;

public class CreditPayModel {

    @SerializedName("shopId")
    private String shopId;

    @SerializedName("customerId")
    private String customerId;

    @SerializedName("amount")
    private String amount;

    public CreditPayModel(String shopId, String customerId, String amount) {
        this.shopId = shopId;
        this.customerId = customerId;
        this.amount = amount;
    }

    public CreditPayModel() {
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
