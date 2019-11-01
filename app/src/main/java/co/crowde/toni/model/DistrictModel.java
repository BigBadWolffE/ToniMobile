package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class DistrictModel {

    @SerializedName("district_id")
    private String district_id;

    @SerializedName("city_id")
    private String city_id;

    @SerializedName("district")
    private String district;

    public DistrictModel() {
    }

    public DistrictModel(String city_id, String district) {
        this.city_id = city_id;
        this.district = district;
    }

    public DistrictModel(String district_id, String city_id, String district) {
        this.district_id = district_id;
        this.city_id = city_id;
        this.district = district;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return district;
    }
}
