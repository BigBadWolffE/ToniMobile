package co.crowde.toni.model.response.list;

import com.google.gson.annotations.SerializedName;

public class SupplierModel {

    @SerializedName("supplierId")
    private String supplierId;

    @SerializedName("supplierName")
    private String supplierName;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    public SupplierModel() {
    }

    public SupplierModel(String supplierId) {
        this.supplierId = supplierId;
    }

    public SupplierModel(String supplierId, String supplierName) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
    }

    public SupplierModel(String supplierId, String supplierName, String address, String phone, String createdBy, String createdAt, String lastUpdated) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.address = address;
        this.phone = phone;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    @Override
    public String toString() {
        return supplierName;
    }
}
