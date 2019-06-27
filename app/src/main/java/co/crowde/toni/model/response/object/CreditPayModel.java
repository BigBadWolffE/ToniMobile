package co.crowde.toni.model.response.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CreditPayModel implements Parcelable {

    @SerializedName("shopId")
    private String shopId;

    @SerializedName("creditPaidId")
    private String creditPaidId;

    @SerializedName("customerId")
    private String customerId;

    @SerializedName("amount")
    private String amount;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("saldo")
    private int saldo;

    public CreditPayModel() {
    }

    public CreditPayModel(String shopId, String creditPaidId, String customerId, String amount, String createdAt, String lastUpdated, String createdBy, String customerName, int saldo) {
        this.shopId = shopId;
        this.creditPaidId = creditPaidId;
        this.customerId = customerId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.createdBy = createdBy;
        this.customerName = customerName;
        this.saldo = saldo;
    }

    protected CreditPayModel(Parcel in) {
        shopId = in.readString();
        creditPaidId = in.readString();
        customerId = in.readString();
        amount = in.readString();
        createdAt = in.readString();
        lastUpdated = in.readString();
        createdBy = in.readString();
        customerName = in.readString();
        saldo = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopId);
        dest.writeString(creditPaidId);
        dest.writeString(customerId);
        dest.writeString(amount);
        dest.writeString(createdAt);
        dest.writeString(lastUpdated);
        dest.writeString(createdBy);
        dest.writeString(customerName);
        dest.writeInt(saldo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CreditPayModel> CREATOR = new Creator<CreditPayModel>() {
        @Override
        public CreditPayModel createFromParcel(Parcel in) {
            return new CreditPayModel(in);
        }

        @Override
        public CreditPayModel[] newArray(int size) {
            return new CreditPayModel[size];
        }
    };

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCreditPaidId() {
        return creditPaidId;
    }

    public void setCreditPaidId(String creditPaidId) {
        this.creditPaidId = creditPaidId;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}
