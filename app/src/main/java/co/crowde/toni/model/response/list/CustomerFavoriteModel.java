package co.crowde.toni.model.response.list;

import com.google.gson.annotations.SerializedName;

public class CustomerFavoriteModel {

    @SerializedName("customerId")
    private String customerId;

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("count")
    private int count;

    @SerializedName("totalAmount")
    private int totalAmount;

    public CustomerFavoriteModel() {
    }

    public CustomerFavoriteModel(String customerId, String customerName, String phone, int count, int totalAmount) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phone = phone;
        this.count = count;
        this.totalAmount = totalAmount;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}
