package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class ProvinceModel {

    @SerializedName("province_id")
    private String province_id;

    @SerializedName("province")
    private String province;

    public ProvinceModel() {
    }

    public ProvinceModel(String province_id, String province) {
        this.province_id = province_id;
        this.province = province;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return province;
    }
}
