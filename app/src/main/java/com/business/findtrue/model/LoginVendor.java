package com.business.findtrue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginVendor {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("vendor_email")
    @Expose
    private String vendorEmail;
    @SerializedName("contactno")
    @Expose
    private String contactno;
    @SerializedName("token")
    @Expose
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
