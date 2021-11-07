package com.business.findtrue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllAdsBannerResponseModel {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("vendor_id")
        @Expose
        private String vendorId;
        @SerializedName("banner_heading")
        @Expose
        private String bannerHeading;
        @SerializedName("banner_image")
        @Expose
        private String bannerImage;
        @SerializedName("uploaded_by")
        @Expose
        private String uploadedBy;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date_created")
        @Expose
        private String dateCreated;
        @SerializedName("position")
        @Expose
        private String position;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getBannerHeading() {
            return bannerHeading;
        }

        public void setBannerHeading(String bannerHeading) {
            this.bannerHeading = bannerHeading;
        }

        public String getBannerImage() {
            return bannerImage;
        }

        public void setBannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
        }

        public String getUploadedBy() {
            return uploadedBy;
        }

        public void setUploadedBy(String uploadedBy) {
            this.uploadedBy = uploadedBy;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

    }
}
