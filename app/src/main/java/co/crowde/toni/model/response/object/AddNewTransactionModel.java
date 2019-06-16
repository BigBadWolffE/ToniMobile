package co.crowde.toni.model.response.object;

import com.google.gson.annotations.SerializedName;

public class AddNewTransactionModel {

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("shopId")
    private String shopId;

    @SerializedName("transactionId")
    private String transactionId;

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

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
