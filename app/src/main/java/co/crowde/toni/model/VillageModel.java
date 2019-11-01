package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class VillageModel {

    @SerializedName("idkel")
    private String idkel;

    @SerializedName("idkec")
    private String idkec;

    @SerializedName("urban_village")
    private String urban_village;

    public VillageModel() {
    }

    public VillageModel(String idkel, String urban_village) {
        this.idkel = idkel;
        this.urban_village = urban_village;
    }

    public VillageModel(String idkel, String idkec, String urban_village) {
        this.idkel = idkel;
        this.idkec = idkec;
        this.urban_village = urban_village;
    }

    public String getIdkel() {
        return idkel;
    }

    public void setIdkel(String idkel) {
        this.idkel = idkel;
    }

    public String getIdkec() {
        return idkec;
    }

    public void setIdkec(String idkec) {
        this.idkec = idkec;
    }

    public String getUrban_village() {
        return urban_village;
    }

    public void setUrban_village(String urban_village) {
        this.urban_village = urban_village;
    }

    @Override
    public String toString() {
        return urban_village;
    }
}
