package co.crowde.toni.model.response.list;

import com.google.gson.annotations.SerializedName;

public class ProductFavoriteModel {

    @SerializedName("productId")
    private String productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("picture")
    private String picture;

    @SerializedName("unit")
    private String unit;

    @SerializedName("supplierName")
    private String supplierName;

    @SerializedName("count")
    private int count;

    public ProductFavoriteModel() {
    }

    public ProductFavoriteModel(String productId, String productName, String picture, String unit, String supplierName, int count) {
        this.productId = productId;
        this.productName = productName;
        this.picture = picture;
        this.unit = unit;
        this.supplierName = supplierName;
        this.count = count;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
