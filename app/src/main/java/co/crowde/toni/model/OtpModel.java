package co.crowde.toni.model;

import com.google.gson.annotations.SerializedName;

public class OtpModel {
    @SerializedName("otp")
    private String otp;
    public OtpModel(){

    }

    public OtpModel(String otp) {
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
