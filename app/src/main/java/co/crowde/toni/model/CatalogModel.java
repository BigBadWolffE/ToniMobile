package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class CatalogModel {

    @SerializedName("productId")
    private String productId;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("supplierId")
    private String supplierId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("description")
    private String description;

    @SerializedName("picture")
    private String picture;

    @SerializedName("unit")
    private String unit;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("supplierName")
    private String supplierName;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("purchasePrice")
    private int purchasePrice;

    @SerializedName("sellingPrice")
    private int sellingPrice;

    private boolean checked;

    public CatalogModel(String productId, String categoryId, String supplierId, String productName, String description, String picture, String unit, String createdAt, String lastUpdated, String createdBy, String supplierName, String categoryName, int purchasePrice, int sellingPrice, boolean checked) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.productName = productName;
        this.description = description;
        this.picture = picture;
        this.unit = unit;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.createdBy = createdBy;
        this.supplierName = supplierName;
        this.categoryName = categoryName;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.checked = checked;
    }

    public CatalogModel() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
