package com.example.bears.Model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("code")
    private String code;

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

    public LoginModel(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

