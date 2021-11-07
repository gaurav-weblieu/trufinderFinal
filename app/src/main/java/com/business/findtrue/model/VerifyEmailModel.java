package com.business.findtrue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VerifyEmailModel {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<EmailVerifyData> data = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<EmailVerifyData> getData() {
        return data;
    }

    public void setData(List<EmailVerifyData> data) {
        this.data = data;
    }

    public class EmailVerifyData{
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("fname")
        @Expose
        private String fname;
        @SerializedName("lname")
        @Expose
        private String lname;
        @SerializedName("telephoneno")
        @Expose
        private String telephoneno;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("mobileno")
        @Expose
        private String mobileno;
        @SerializedName("mobile_otp")
        @Expose
        private String mobile_otp;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("profile_image")
        @Expose
        private String profile_image;
        @SerializedName("ext")
        @Expose
        private String ext;
        @SerializedName("profile_image_partner")
        @Expose
        private String profile_image_partner;
        @SerializedName("ext_partner")
        @Expose
        private String ext_partner;
        @SerializedName("doj")
        @Expose
        private String doj;
        @SerializedName("verification")
        @Expose
        private String verification;
        @SerializedName("flagvalue")
        @Expose
        private String flagvalue;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public String getTelephoneno() {
            return telephoneno;
        }

        public void setTelephoneno(String telephoneno) {
            this.telephoneno = telephoneno;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMobileno() {
            return mobileno;
        }

        public void setMobileno(String mobileno) {
            this.mobileno = mobileno;
        }

        public String getMobile_otp() {
            return mobile_otp;
        }

        public void setMobile_otp(String mobile_otp) {
            this.mobile_otp = mobile_otp;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public String getProfile_image_partner() {
            return profile_image_partner;
        }

        public void setProfile_image_partner(String profile_image_partner) {
            this.profile_image_partner = profile_image_partner;
        }

        public String getExt_partner() {
            return ext_partner;
        }

        public void setExt_partner(String ext_partner) {
            this.ext_partner = ext_partner;
        }

        public String getDoj() {
            return doj;
        }

        public void setDoj(String doj) {
            this.doj = doj;
        }

        public String getVerification() {
            return verification;
        }

        public void setVerification(String verification) {
            this.verification = verification;
        }

        public String getFlagvalue() {
            return flagvalue;
        }

        public void setFlagvalue(String flagvalue) {
            this.flagvalue = flagvalue;
        }
    }
}
