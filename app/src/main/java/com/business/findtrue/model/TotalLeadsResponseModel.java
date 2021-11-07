package com.business.findtrue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalLeadsResponseModel {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("vendor_id")
        @Expose
        private String vendorId;
        @SerializedName("last_updt_totl_leads")
        @Expose
        private String lastUpdtTotlLeads;
        @SerializedName("subscription_id")
        @Expose
        private String subscriptionId;
        @SerializedName("expiry_date")
        @Expose
        private String expiryDate;

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getLastUpdtTotlLeads() {
            return lastUpdtTotlLeads;
        }

        public void setLastUpdtTotlLeads(String lastUpdtTotlLeads) {
            this.lastUpdtTotlLeads = lastUpdtTotlLeads;
        }

        public String getSubscriptionId() {
            return subscriptionId;
        }

        public void setSubscriptionId(String subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

    }
}
