package co.crowde.toni.model.custom;

import com.google.gson.annotations.SerializedName;

public class StatusModel {

    @SerializedName("active")
    private boolean active;

    @SerializedName("status")
    private String status;

    @Override
    public String toString() {
        return status;
    }

    public StatusModel() {
    }

    public StatusModel(String status) {
        this.status = status;
    }

    public StatusModel(boolean active) {
        this.active = active;
    }

    public StatusModel(boolean active, String status) {
        this.active = active;
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
