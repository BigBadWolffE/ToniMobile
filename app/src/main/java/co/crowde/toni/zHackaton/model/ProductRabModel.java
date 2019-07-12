package co.crowde.toni.zHackaton.model;

import com.google.gson.annotations.SerializedName;

public class ProductRabModel {

    @SerializedName("id")
    private int id;

    @SerializedName("productName")
    private String productName;

    @SerializedName("category")
    private String category;

    @SerializedName("qty")
    private int qty;

    private boolean checked;

    public ProductRabModel() {
    }

    public ProductRabModel(int id, String productName, String category, int qty, boolean checked) {
        this.id = id;
        this.productName = productName;
        this.category = category;
        this.qty = qty;
        this.checked = checked;
    }

    public ProductRabModel(String productName, String category, int qty) {
        this.productName = productName;
        this.category = category;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String unit) {
        this.category = unit;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
