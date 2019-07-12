package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class DistrictModel {

    @SerializedName("id")
    private String id;

    @SerializedName("id_kabupaten")
    private String id_kabupaten;

    @SerializedName("nama")
    private String nama;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("createdBy")
    private String createdBy;

    public DistrictModel() {
    }

    public DistrictModel(String nama) {
        this.nama = nama;
    }

    public DistrictModel(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public DistrictModel(String id, String id_kabupaten, String nama) {
        this.id = id;
        this.id_kabupaten = id_kabupaten;
        this.nama = nama;
    }

    public DistrictModel(String id, String id_kabupaten, String nama, String createdAt, String lastUpdated, String createdBy) {
        this.id = id;
        this.id_kabupaten = id_kabupaten;
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

    public String getId_kabupaten() {
        return id_kabupaten;
    }

    public void setId_kabupaten(String id_kabupaten) {
        this.id_kabupaten = id_kabupaten;
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
