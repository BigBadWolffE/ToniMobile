package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class AdminModel {

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("role")
    private String role;


    @SerializedName("token")
    private String token;

    @SerializedName("picture")
    private String picture;

    public AdminModel(){


    }

    public AdminModel(String username, String password, String role, String token, String picture) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.token = token;
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
