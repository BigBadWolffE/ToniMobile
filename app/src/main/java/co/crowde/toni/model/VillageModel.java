package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class VillageModel {

    @SerializedName("id")
    private String id;

    @SerializedName("id_kecamatan")
    private String id_kecamatan;

    @SerializedName("nama")
    private String nama;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("createdBy")
    private String createdBy;

    public VillageModel() {
    }

    public VillageModel(String nama) {
        this.nama = nama;
    }

    public VillageModel(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public VillageModel(String id, String id_kecamatan, String nama) {
        this.id = id;
        this.id_kecamatan = id_kecamatan;
        this.nama = nama;
    }

    public VillageModel(String id, String id_kecamatan, String nama, String createdAt, String lastUpdated, String createdBy) {
        this.id = id;
        this.id_kecamatan = id_kecamatan;
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

    public String getId_kecamatan() {
        return id_kecamatan;
    }

    public void setId_kecamatan(String id_kecamatan) {
        this.id_kecamatan = id_kecamatan;
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
