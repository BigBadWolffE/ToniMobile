package co.crowde.toni.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CustomerModel implements Parcelable {
    @SerializedName("shopId")
    private String shopId;

    @SerializedName("customerId")
    private String customerId;

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("province")
    private String province;

    @SerializedName("regency")
    private String regency;

    @SerializedName("district")
    private String district;

    @SerializedName("village")
    private String village;

    @SerializedName("credit")
    private int credit;

    @SerializedName("creditPaid")
    private int creditPaid;

    @SerializedName("saldo")
    private int saldo;

    public CustomerModel(String shopId, String customerId, String customerName, String address, String phone, String createdAt, String lastUpdated, String createdBy, String province, String regency, String district, String village, int credit, int creditPaid, int saldo) {
        this.shopId = shopId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.createdBy = createdBy;
        this.province = province;
        this.regency = regency;
        this.district = district;
        this.village = village;
        this.credit = credit;
        this.creditPaid = creditPaid;
        this.saldo = saldo;
    }

    public CustomerModel() {
    }

    protected CustomerModel(Parcel in) {
        shopId = in.readString();
        customerId = in.readString();
        customerName = in.readString();
        address = in.readString();
        phone = in.readString();
        createdAt = in.readString();
        lastUpdated = in.readString();
        createdBy = in.readString();
        province = in.readString();
        regency = in.readString();
        district = in.readString();
        village = in.readString();
        credit = in.readInt();
        creditPaid = in.readInt();
        saldo = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopId);
        dest.writeString(customerId);
        dest.writeString(customerName);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(createdAt);
        dest.writeString(lastUpdated);
        dest.writeString(createdBy);
        dest.writeString(province);
        dest.writeString(regency);
        dest.writeString(district);
        dest.writeString(village);
        dest.writeInt(credit);
        dest.writeInt(creditPaid);
        dest.writeInt(saldo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomerModel> CREATOR = new Creator<CustomerModel>() {
        @Override
        public CustomerModel createFromParcel(Parcel in) {
            return new CustomerModel(in);
        }

        @Override
        public CustomerModel[] newArray(int size) {
            return new CustomerModel[size];
        }
    };

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getCreditPaid() {
        return creditPaid;
    }

    public void setCreditPaid(int creditPaid) {
        this.creditPaid = creditPaid;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
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
}

//public class CustomerModel {
//    @SerializedName("shopId")
//    private String shopId;
//
//    @SerializedName("customerId")
//    private String customerId;
//
//    @SerializedName("customerName")
//    private String customerName;
//
//    @SerializedName("address")
//    private String address;
//
//    @SerializedName("phone")
//    private String phone;
//
//    @SerializedName("createdAt")
//    private String createdAt;
//
//    @SerializedName("lastUpdated")
//    private String lastUpdated;
//
//    @SerializedName("createdBy")
//    private String createdBy;
//
//    @SerializedName("province")
//    private String province;
//
//    @SerializedName("regency")
//    private String regency;
//
//    @SerializedName("district")
//    private String district;
//
//    @SerializedName("village")
//    private String village;
//
//    @SerializedName("credit")
//    private int credit;
//
//    @SerializedName("creditPaid")
//    private int creditPaid;
//
//    @SerializedName("saldo")
//    private int saldo;
//
//    public CustomerModel(String shopId, String customerId, String customerName, String address, String phone, String createdAt, String lastUpdated, String createdBy, String province, String regency, String district, String village, int credit, int creditPaid, int saldo) {
//        this.shopId = shopId;
//        this.customerId = customerId;
//        this.customerName = customerName;
//        this.address = address;
//        this.phone = phone;
//        this.createdAt = createdAt;
//        this.lastUpdated = lastUpdated;
//        this.createdBy = createdBy;
//        this.province = province;
//        this.regency = regency;
//        this.district = district;
//        this.village = village;
//        this.credit = credit;
//        this.creditPaid = creditPaid;
//        this.saldo = saldo;
//    }
//
//    public CustomerModel() {
//    }
//
//    public String getShopId() {
//        return shopId;
//    }
//
//    public void setShopId(String shopId) {
//        this.shopId = shopId;
//    }
//
//    public String getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(String customerId) {
//        this.customerId = customerId;
//    }
//
//    public String getCustomerName() {
//        return customerName;
//    }
//
//    public void setCustomerName(String customerName) {
//        this.customerName = customerName;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(String createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public String getLastUpdated() {
//        return lastUpdated;
//    }
//
//    public void setLastUpdated(String lastUpdated) {
//        this.lastUpdated = lastUpdated;
//    }
//
//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public int getCredit() {
//        return credit;
//    }
//
//    public void setCredit(int credit) {
//        this.credit = credit;
//    }
//
//    public int getCreditPaid() {
//        return creditPaid;
//    }
//
//    public void setCreditPaid(int creditPaid) {
//        this.creditPaid = creditPaid;
//    }
//
//    public int getSaldo() {
//        return saldo;
//    }
//
//    public void setSaldo(int saldo) {
//        this.saldo = saldo;
//    }
//
//    public String getProvince() {
//        return province;
//    }
//
//    public void setProvince(String province) {
//        this.province = province;
//    }
//
//    public String getRegency() {
//        return regency;
//    }
//
//    public void setRegency(String regency) {
//        this.regency = regency;
//    }
//
//    public String getDistrict() {
//        return district;
//    }
//
//    public void setDistrict(String district) {
//        this.district = district;
//    }
//
//    public String getVillage() {
//        return village;
//    }
//
//    public void setVillage(String village) {
//        this.village = village;
//    }
//}
