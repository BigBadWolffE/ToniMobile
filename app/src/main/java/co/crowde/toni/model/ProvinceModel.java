package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class ProvinceModel {

    @SerializedName("id")
    private String id;

    @SerializedName("nama")
    private String nama;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("createdBy")
    private String createdBy;

    public ProvinceModel(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public ProvinceModel(String id, String nama, String createdAt, String lastUpdated, String createdBy) {
        this.id = id;
        this.nama = nama;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    @Override
    public String toString() {
        return nama;
    }
}
