package co.crowde.toni.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProductModel implements Parcelable {
    @SerializedName("shopId")
    private String shopId;

    @SerializedName("productId")
    private String productId;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("description")
    private String description;

    @SerializedName("picture")
    private String picture;

    @SerializedName("status")
    private String status;

    @SerializedName("purchasePrice")
    private int purchasePrice;

    @SerializedName("sellingPrice")
    private int sellingPrice;

    @SerializedName("unit")
    private String unit;

    @SerializedName("supplierId")
    private String supplierId;

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

    @SerializedName("stock")
    private int stock;

    @SerializedName("supplierName")
    private String supplierName;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("qty")
    private int qty;

    @SerializedName("discount")
    private int discount;

    public ProductModel() {
    }

    public ProductModel(String shopId, String productId, String categoryId, String productName, String description, String picture, String status, int purchasePrice, int sellingPrice, String unit, String supplierId, String createdAt, String lastUpdated, String createdBy, String province, String regency, String district, String village, int stock, String supplierName, String categoryName, int qty, int discount) {
        this.shopId = shopId;
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.picture = picture;
        this.status = status;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.unit = unit;
        this.supplierId = supplierId;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.createdBy = createdBy;
        this.province = province;
        this.regency = regency;
        this.district = district;
        this.village = village;
        this.stock = stock;
        this.supplierName = supplierName;
        this.categoryName = categoryName;
        this.qty = qty;
        this.discount = discount;
    }

    protected ProductModel(Parcel in) {
        shopId = in.readString();
        productId = in.readString();
        categoryId = in.readString();
        productName = in.readString();
        description = in.readString();
        picture = in.readString();
        status = in.readString();
        purchasePrice = in.readInt();
        sellingPrice = in.readInt();
        unit = in.readString();
        supplierId = in.readString();
        createdAt = in.readString();
        lastUpdated = in.readString();
        createdBy = in.readString();
        province = in.readString();
        regency = in.readString();
        district = in.readString();
        village = in.readString();
        stock = in.readInt();
        supplierName = in.readString();
        categoryName = in.readString();
        qty = in.readInt();
        discount = in.readInt();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopId);
        dest.writeString(productId);
        dest.writeString(categoryId);
        dest.writeString(productName);
        dest.writeString(description);
        dest.writeString(picture);
        dest.writeString(status);
        dest.writeInt(purchasePrice);
        dest.writeInt(sellingPrice);
        dest.writeString(unit);
        dest.writeString(supplierId);
        dest.writeString(createdAt);
        dest.writeString(lastUpdated);
        dest.writeString(createdBy);
        dest.writeString(province);
        dest.writeString(regency);
        dest.writeString(district);
        dest.writeString(village);
        dest.writeInt(stock);
        dest.writeString(supplierName);
        dest.writeString(categoryName);
        dest.writeInt(qty);
        dest.writeInt(discount);
    }
}
