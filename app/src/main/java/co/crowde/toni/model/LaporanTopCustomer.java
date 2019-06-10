package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LaporanTopCustomer implements Serializable {
    @SerializedName("customerId")
    private String customerId;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("phone")
    private String phone;
    @SerializedName("count")
    private String count;
    @SerializedName("totalAmount")
    private int totalAmount;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}
