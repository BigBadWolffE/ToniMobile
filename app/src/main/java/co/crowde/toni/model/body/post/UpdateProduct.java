package co.crowde.toni.model.body.post;

import com.google.gson.annotations.SerializedName;

public class UpdateProduct {

    @SerializedName("shopId")
    private String shopId;

    @SerializedName("productId")
    private String productId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("purchasePrice")
    private int purchasePrice;

    @SerializedName("sellingPrice")
    private int sellingPrice;

    @SerializedName("type")
    private String type;

    public UpdateProduct() {
    }

    public UpdateProduct(String shopId, String productId, int quantity, int purchasePrice, int sellingPrice, String type) {
        this.shopId = shopId;
        this.productId = productId;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.type = type;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
