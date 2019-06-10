package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class HistoryCategoryModel {

    // table name
    public static final String TABLE_NAME = "tbCategory";

    // column tables
    public static final String KEY_ID = "id";
    public static final String KEY_CATEGORY_ID = "categoryId";
    public static final String KEY_CATEGORY_NAME = "categoryName";

    @SerializedName("id")
    private int id;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("categoryName")
    private String categoryName;

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME+ "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_CATEGORY_ID + " TEXT UNIQUE," +
            KEY_CATEGORY_NAME + " TEXT, UNIQUE("+KEY_CATEGORY_ID+") ON CONFLICT FAIL);";

    public HistoryCategoryModel() {
    }

    public HistoryCategoryModel(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public HistoryCategoryModel(int id, String categoryId, String categoryName) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
