package com.example.bears.Model;

import com.google.gson.annotations.SerializedName;

public class BeaconModel {
    @SerializedName("code")
    private String code;

    @SerializedName("BeaId")
    private String BeaId;

    @SerializedName("message")
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBeaId() {
        return BeaId;
    }

    public void setBeaId(String beaId) {
        BeaId = beaId;
    }

    public BeaconModel(String code, String beaId, String message) {
        this.code = code;
        BeaId = beaId;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}



