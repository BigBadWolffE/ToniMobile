package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Annotation;

public class UserModel{

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("token")
    private String token;

    @SerializedName("picture")
    private String picture;

    public UserModel() {
    }

    public UserModel(String username, String password, String token, String picture) {
        this.username = username;
        this.password = password;
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
