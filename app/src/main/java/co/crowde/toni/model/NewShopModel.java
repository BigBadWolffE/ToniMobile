package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class    NewShopModel {

    @SerializedName("ownerName")
    private String ownerName;
    @SerializedName("businessType")
    private String businessType;
    @SerializedName("street")
    private String street;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("description")
    private String description;
    @SerializedName("province")
    private String province;
    @SerializedName("regency")
    private String regency;
    @SerializedName("district")
    private String district;
    @SerializedName("village")
    private String village;
    @SerializedName("shopName")
    private String shopName;
    @SerializedName("password")
    private String password;
    @SerializedName("identifier")
    private String identifier;
    @SerializedName("account")
    private NewAccountModel account;

    public NewShopModel(){

    }

    public NewShopModel(String ownerName, String businessType, String street, String phoneNumber, String description, String province, String regency, String district, String village, String shopName, String password, String indentifier, NewAccountModel account) {
        this.ownerName = ownerName;
        this.businessType = businessType;
        this.street = street;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.province = province;
        this.regency = regency;
        this.district = district;
        this.village = village;
        this.shopName = shopName;
        this.password = password;
        this.identifier = indentifier;
        this.account = account;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegency() {
        return regency;
    }

    public void setRegency(String regency) {
        this.regency = regency;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String indentifier) {
        this.identifier = indentifier;
    }

    public NewAccountModel getAccount() {
        return account;
    }

    public void setAccount(NewAccountModel account) {
        this.account = account;
    }
}
