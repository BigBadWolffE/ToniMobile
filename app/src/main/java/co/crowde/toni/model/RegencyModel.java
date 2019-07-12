package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class RegencyModel {

    @SerializedName("id")
    private String id;

    @SerializedName("id_prov")
    private String id_prov;

    @SerializedName("nama")
    private String nama;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("createdBy")
    private String createdBy;

    public RegencyModel() {
    }

    public RegencyModel(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public RegencyModel(String id, String id_prov, String nama) {
        this.id = id;
        this.id_prov = id_prov;
        this.nama = nama;
    }

    public RegencyModel(String id, String id_prov, String nama, String createdAt, String lastUpdated, String createdBy) {
        this.id = id;
        this.id_prov = id_prov;
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

    public String getId_prov() {
        return id_prov;
    }

    public void setId_prov(String id_prov) {
        this.id_prov = id_prov;
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
