package co.crowde.toni.model.response.list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TransactionModel implements Parcelable {

    @SerializedName("shopId")
    private String shopId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("transaction_code")
    private int transaction_code;

    @SerializedName("customerId")
    private String customerId;

    @SerializedName("paymentType")
    private String paymentType;

    @SerializedName("amount")
    private int amount;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("status")
    private String status;

    @SerializedName("paid")
    private int paid;

    @SerializedName("_change")
    private int _change;

    @SerializedName("province")
    private String province;

    @SerializedName("regency")
    private String regency;

    @SerializedName("district")
    private String district;

    @SerializedName("village")
    private String village;

    @SerializedName("customerName")
    private String customerName;

    public TransactionModel() {
    }

    public TransactionModel(String transactionId, int amount, String createdAt, String customerName) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.customerName = customerName;
    }

    public TransactionModel(String shopId, String transactionId, int transaction_code, String customerId, String paymentType, int amount, String createdAt, String lastUpdated, String createdBy, String status, int paid, int _change, String province, String regency, String district, String village, String customerName) {
        this.shopId = shopId;
        this.transactionId = transactionId;
        this.transaction_code = transaction_code;
        this.customerId = customerId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.createdBy = createdBy;
        this.status = status;
        this.paid = paid;
        this._change = _change;
        this.province = province;
        this.regency = regency;
        this.district = district;
        this.village = village;
        this.customerName = customerName;
    }

    protected TransactionModel(Parcel in) {
        shopId = in.readString();
        transactionId = in.readString();
        transaction_code = in.readInt();
        customerId = in.readString();
        paymentType = in.readString();
        amount = in.readInt();
        createdAt = in.readString();
        lastUpdated = in.readString();
        createdBy = in.readString();
        status = in.readString();
        paid = in.readInt();
        _change = in.readInt();
        province = in.readString();
        regency = in.readString();
        district = in.readString();
        village = in.readString();
        customerName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopId);
        dest.writeString(transactionId);
        dest.writeInt(transaction_code);
        dest.writeString(customerId);
        dest.writeString(paymentType);
        dest.writeInt(amount);
        dest.writeString(createdAt);
        dest.writeString(lastUpdated);
        dest.writeString(createdBy);
        dest.writeString(status);
        dest.writeInt(paid);
        dest.writeInt(_change);
        dest.writeString(province);
        dest.writeString(regency);
        dest.writeString(district);
        dest.writeString(village);
        dest.writeString(customerName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionModel> CREATOR = new Creator<TransactionModel>() {
        @Override
        public TransactionModel createFromParcel(Parcel in) {
            return new TransactionModel(in);
        }

        @Override
        public TransactionModel[] newArray(int size) {
            return new TransactionModel[size];
        }
    };

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

    public int getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(int transaction_code) {
        this.transaction_code = transaction_code;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int get_change() {
        return _change;
    }

    public void set_change(int _change) {
        this._change = _change;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegency() {
        return regency;
    }

    public void setRegency(String regency) {
        this.regency = regency;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


}
