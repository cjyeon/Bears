package com.example.bears.Model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    @SerializedName("busname")
    private String busNum;

    public LoginModel(String code, String message, String busNum) {
        this.code = code;
        this.message = message;
        this.busNum = busNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }
}

