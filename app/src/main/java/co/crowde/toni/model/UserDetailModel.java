package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Firiyah on 1/14/2019.
 */

public class UserDetailModel implements Serializable {
    @SerializedName("shopId")
    private String shopId;

    @SerializedName("shopName")
    private String shopName;

    @SerializedName("street")
    private String street;

    @SerializedName("regency")
    private String regency;

    @SerializedName("province")
    private String province;

    @SerializedName("district")
    private String district;

    @SerializedName("village")
    private String village;

    @SerializedName("businessType")
    private String businessType;

    @SerializedName("description")
    private String description;

    @SerializedName("ownerName")
    private String ownerName;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("isOpen")
    private int isOpen;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    @SerializedName("createdBy")
    private String createdBy;

    public UserDetailModel(String shopId, String shopName, String street, String regency, String province, String district, String village, String businessType, String description, String ownerName, String phoneNumber, int isOpen, String createdAt, String lastUpdated, String createdBy) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.street = street;
        this.regency = regency;
        this.province = province;
        this.district = district;
        this.village = village;
        this.businessType = businessType;
        this.description = description;
        this.ownerName = ownerName;
        this.phoneNumber = phoneNumber;
        this.isOpen = isOpen;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.createdBy = createdBy;
    }

    public UserDetailModel() {
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRegency() {
        return regency;
    }

    public void setRegency(String regency) {
        this.regency = regency;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
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
}
