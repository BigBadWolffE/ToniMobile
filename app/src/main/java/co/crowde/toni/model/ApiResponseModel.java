package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponseModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("code")
    private int code;

    @SerializedName("rowCount")
    private int rowCount;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private String data;

}
