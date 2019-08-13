package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class ProductDiscountModel {
    @SerializedName("shopId")
    private String shopId;

    @SerializedName("productId")
    private String productId;

    @SerializedName("discount")
    private int discount;

    public ProductDiscountModel() {
    }

    public ProductDiscountModel(String shopId, String productId, int discount) {
        this.shopId = shopId;
        this.productId = productId;
        this.discount = discount;
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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
