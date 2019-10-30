package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class CityModel {

    @SerializedName("city_id")
    private String city_id;

    @SerializedName("province_id")
    private String province_id;

    @SerializedName("city")
    private String city;

    public CityModel() {
    }

    public CityModel(String city_id, String city) {
        this.city_id = city_id;
        this.city = city;
    }

    public CityModel(String city_id, String province_id, String city) {
        this.city_id = city_id;
        this.province_id = province_id;
        this.city = city;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return city;
    }
}
