package com.business.findtrue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VendorLogin {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private LoginVendor data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginVendor getData() {
        return data;
    }

    public void setData(LoginVendor data) {
        this.data = data;
    }
}
