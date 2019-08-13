package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class CartModel {

    // table name
    public static final String TABLE_NAME = "table_cart";

    // column tables
    public static final String KEY_ID = "id";
    public static final String KEY_SHOP_ID = "shopId";
    public static final String KEY_PRODUCT_ID = "productId";
    public static final String KEY_PRODUCT_NAME = "productName";
    public static final String KEY_UNIT = "unit";
    public static final String KEY_STOK = "stok";
    public static final String KEY_PICTURE = "picture";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_SELLING_PRICE = "sellingPrice";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DISCOUNT = "discount";

    @SerializedName("id")
    private int id;

    @SerializedName("shopId")
    private String shopId;

    @SerializedName("productId")
    private String productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("unit")
    private String unit;

    @SerializedName("stok")
    private int stok;

    @SerializedName("picture")
    private String picture;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("sellingPrice")
    private int sellingPrice;

    @SerializedName("amount")
    private int amount;

    @SerializedName("discount")
    private int discount;

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME+ "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_SHOP_ID + " TEXT," +
            KEY_PRODUCT_ID + " TEXT UNIQUE," +
            KEY_PRODUCT_NAME + " TEXT," +
            KEY_UNIT + " TEXT," +
            KEY_STOK + " INTEGER," +
            KEY_PICTURE + " TEXT," +
            KEY_QUANTITY + " INTEGER," +
            KEY_SELLING_PRICE + " INTEGER," +
            KEY_AMOUNT + " INTEGER,"+
            KEY_DISCOUNT + " INTEGER, UNIQUE("+KEY_PRODUCT_ID+") ON CONFLICT FAIL);";

    public CartModel(int id, String shopId, String productId, String productName, String unit, int stok, String picture, int quantity, int sellingPrice, int amount, int discount) {
        this.id = id;
        this.shopId = shopId;
        this.productId = productId;
        this.productName = productName;
        this.unit = unit;
        this.stok = stok;
        this.picture = picture;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
        this.amount = amount;
        this.discount = discount;
    }

    public CartModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
