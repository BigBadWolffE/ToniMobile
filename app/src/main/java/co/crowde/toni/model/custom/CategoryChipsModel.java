package co.crowde.toni.model.custom;

import com.google.gson.annotations.SerializedName;

public class CategoryChipsModel {

    @SerializedName("active")
    private boolean active;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("categoryName")
    private String categoryName;

    public CategoryChipsModel() {
    }

    public CategoryChipsModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryChipsModel(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public CategoryChipsModel(boolean active, String categoryId, String categoryName) {
        this.active = active;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
