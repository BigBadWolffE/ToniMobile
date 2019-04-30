package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class CatalogRequestModel {
    @SerializedName("shopId")
    private String shopId;

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

    @SerializedName("purchasePrice")
    private int purchasePrice;

    @SerializedName("sellingPrice")
    private int sellingPrice;

    public CatalogRequestModel() {
    }

    public CatalogRequestModel(String shopId, String productId, String categoryId, String supplierId, String productName, String description, String picture, String unit, int purchasePrice, int sellingPrice) {
        this.shopId = shopId;
        this.productId = productId;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.productName = productName;
        this.description = description;
        this.picture = picture;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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
}
