package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class TransactionProductModel {

    @SerializedName("shopId")
    private String shopId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("productId")
    private String productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("sellingPrice")
    private int sellingPrice;

    @SerializedName("amount")
    private int amount;

    @SerializedName("picture")
    private String picture;

    @SerializedName("_order")
    private int _order;

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

    @SerializedName("supplierName")
    private String supplierName;

    @SerializedName("unit")
    private String unit;

    public TransactionProductModel() {
    }

    public TransactionProductModel(String shopId, String transactionId, String productId, String productName, int quantity, int sellingPrice, int amount, String picture, int _order, String createdAt, String lastUpdated, String createdBy, String province, String regency, String district, String village, String supplierName, String unit) {
        this.shopId = shopId;
        this.transactionId = transactionId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
        this.amount = amount;
        this.picture = picture;
        this._order = _order;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.createdBy = createdBy;
        this.province = province;
        this.regency = regency;
        this.district = district;
        this.village = village;
        this.supplierName = supplierName;
        this.unit = unit;
    }

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int get_order() {
        return _order;
    }

    public void set_order(int _order) {
        this._order = _order;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
