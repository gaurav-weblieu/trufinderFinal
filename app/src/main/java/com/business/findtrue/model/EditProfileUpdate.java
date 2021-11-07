package com.business.findtrue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditProfileUpdate {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private VendorData data;

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

    public VendorData getData() {
        return data;
    }

    public void setData(VendorData data) {
        this.data = data;
    }

    public class VendorData {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("vendor_name")
        @Expose
        private String vendorName;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("sub_category_id")
        @Expose
        private String subCategoryId;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("localarea")
        @Expose
        private String localarea;
        @SerializedName("city_id")
        @Expose
        private String cityId;
        @SerializedName("area_id")
        @Expose
        private String areaId;
        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("website")
        @Expose
        private String website;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("vendor_email")
        @Expose
        private String vendorEmail;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("thumb_photo")
        @Expose
        private String thumbPhoto;
        @SerializedName("contactno")
        @Expose
        private String contactno;
        @SerializedName("landline_code")
        @Expose
        private String landlineCode;
        @SerializedName("landline_no")
        @Expose
        private String landlineNo;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("contractor_status")
        @Expose
        private String contractorStatus;
        @SerializedName("deal")
        @Expose
        private String deal;
        @SerializedName("albumname")
        @Expose
        private String albumname;
        @SerializedName("albumpic")
        @Expose
        private String albumpic;
        @SerializedName("realfolder")
        @Expose
        private String realfolder;
        @SerializedName("specialmember")
        @Expose
        private String specialmember;
        @SerializedName("background_class")
        @Expose
        private String backgroundClass;
        @SerializedName("flagvalue")
        @Expose
        private String flagvalue;
        @SerializedName("times_of_view")
        @Expose
        private String timesOfView;
        @SerializedName("page_no")
        @Expose
        private String pageNo;
        @SerializedName("ip_address")
        @Expose
        private String ipAddress;
        @SerializedName("top_wedding_vendor")
        @Expose
        private String topWeddingVendor;
        @SerializedName("top_vendor_of_week")
        @Expose
        private String topVendorOfWeek;
        @SerializedName("top_vendor_main_image")
        @Expose
        private String topVendorMainImage;
        @SerializedName("no_of_contact_time")
        @Expose
        private String noOfContactTime;
        @SerializedName("uploaded_by")
        @Expose
        private String uploadedBy;
        @SerializedName("uploaded_date")
        @Expose
        private String uploadedDate;
        @SerializedName("updated_date")
        @Expose
        private String updatedDate;
        @SerializedName("profile_verified_status")
        @Expose
        private String profileVerifiedStatus;
        @SerializedName("profile_verifiedby_adminid")
        @Expose
        private String profileVerifiedbyAdminid;
        @SerializedName("profile_verified_time")
        @Expose
        private String profileVerifiedTime;
        @SerializedName("category_list")
        @Expose
        private List<Category> categoryList = null;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("new_profile_img")
        @Expose
        private String newProfileImg;
        @SerializedName("business_details")
        @Expose
        private BusinessDetails business_details;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getSubCategoryId() {
            return subCategoryId;
        }

        public void setSubCategoryId(String subCategoryId) {
            this.subCategoryId = subCategoryId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLocalarea() {
            return localarea;
        }

        public void setLocalarea(String localarea) {
            this.localarea = localarea;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getVendorEmail() {
            return vendorEmail;
        }

        public void setVendorEmail(String vendorEmail) {
            this.vendorEmail = vendorEmail;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getThumbPhoto() {
            return thumbPhoto;
        }

        public void setThumbPhoto(String thumbPhoto) {
            this.thumbPhoto = thumbPhoto;
        }

        public String getContactno() {
            return contactno;
        }

        public void setContactno(String contactno) {
            this.contactno = contactno;
        }

        public String getLandlineCode() {
            return landlineCode;
        }

        public void setLandlineCode(String landlineCode) {
            this.landlineCode = landlineCode;
        }

        public String getLandlineNo() {
            return landlineNo;
        }

        public void setLandlineNo(String landlineNo) {
            this.landlineNo = landlineNo;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContractorStatus() {
            return contractorStatus;
        }

        public void setContractorStatus(String contractorStatus) {
            this.contractorStatus = contractorStatus;
        }

        public String getDeal() {
            return deal;
        }

        public void setDeal(String deal) {
            this.deal = deal;
        }

        public String getAlbumname() {
            return albumname;
        }

        public void setAlbumname(String albumname) {
            this.albumname = albumname;
        }

        public String getAlbumpic() {
            return albumpic;
        }

        public void setAlbumpic(String albumpic) {
            this.albumpic = albumpic;
        }

        public String getRealfolder() {
            return realfolder;
        }

        public void setRealfolder(String realfolder) {
            this.realfolder = realfolder;
        }

        public String getSpecialmember() {
            return specialmember;
        }

        public void setSpecialmember(String specialmember) {
            this.specialmember = specialmember;
        }

        public String getBackgroundClass() {
            return backgroundClass;
        }

        public void setBackgroundClass(String backgroundClass) {
            this.backgroundClass = backgroundClass;
        }

        public String getFlagvalue() {
            return flagvalue;
        }

        public void setFlagvalue(String flagvalue) {
            this.flagvalue = flagvalue;
        }

        public String getTimesOfView() {
            return timesOfView;
        }

        public void setTimesOfView(String timesOfView) {
            this.timesOfView = timesOfView;
        }

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getTopWeddingVendor() {
            return topWeddingVendor;
        }

        public void setTopWeddingVendor(String topWeddingVendor) {
            this.topWeddingVendor = topWeddingVendor;
        }

        public String getTopVendorOfWeek() {
            return topVendorOfWeek;
        }

        public void setTopVendorOfWeek(String topVendorOfWeek) {
            this.topVendorOfWeek = topVendorOfWeek;
        }

        public String getTopVendorMainImage() {
            return topVendorMainImage;
        }

        public void setTopVendorMainImage(String topVendorMainImage) {
            this.topVendorMainImage = topVendorMainImage;
        }

        public String getNoOfContactTime() {
            return noOfContactTime;
        }

        public void setNoOfContactTime(String noOfContactTime) {
            this.noOfContactTime = noOfContactTime;
        }

        public String getUploadedBy() {
            return uploadedBy;
        }

        public void setUploadedBy(String uploadedBy) {
            this.uploadedBy = uploadedBy;
        }

        public String getUploadedDate() {
            return uploadedDate;
        }

        public void setUploadedDate(String uploadedDate) {
            this.uploadedDate = uploadedDate;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public String getProfileVerifiedStatus() {
            return profileVerifiedStatus;
        }

        public void setProfileVerifiedStatus(String profileVerifiedStatus) {
            this.profileVerifiedStatus = profileVerifiedStatus;
        }

        public String getProfileVerifiedbyAdminid() {
            return profileVerifiedbyAdminid;
        }

        public void setProfileVerifiedbyAdminid(String profileVerifiedbyAdminid) {
            this.profileVerifiedbyAdminid = profileVerifiedbyAdminid;
        }

        public String getProfileVerifiedTime() {
            return profileVerifiedTime;
        }

        public void setProfileVerifiedTime(String profileVerifiedTime) {
            this.profileVerifiedTime = profileVerifiedTime;
        }

        public List<Category> getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(List<Category> categoryList) {
            this.categoryList = categoryList;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getNewProfileImg() {
            return newProfileImg;
        }

        public void setNewProfileImg(String newProfileImg) {
            this.newProfileImg = newProfileImg;
        }

        public BusinessDetails getBusiness_details() {
            return business_details;
        }

        public void setBusiness_details(BusinessDetails business_details) {
            this.business_details = business_details;
        }
    }

    public static class Category {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("vendor_id")
        @Expose
        private String vendorId;
        @SerializedName("cat_id")
        @Expose
        private String catId;
        @SerializedName("flagvalue")
        @Expose
        private String flagvalue;
        @SerializedName("category_name")
        @Expose
        private String category_name;

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

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public String getFlagvalue() {
            return flagvalue;
        }

        public void setFlagvalue(String flagvalue) {
            this.flagvalue = flagvalue;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }
    }

    public class BusinessDetails {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("vendorid")
        @Expose
        private String vendorid;
        @SerializedName("establish")
        @Expose
        private String establish;
        @SerializedName("accepts")
        @Expose
        private String accepts;
        @SerializedName("office_hour")
        @Expose
        private String officeHour;
        @SerializedName("package_price")
        @Expose
        private Object packagePrice;
        @SerializedName("package_price_detail")
        @Expose
        private Object packagePriceDetail;
        @SerializedName("flagvalue")
        @Expose
        private String flagvalue;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVendorid() {
            return vendorid;
        }

        public void setVendorid(String vendorid) {
            this.vendorid = vendorid;
        }

        public String getEstablish() {
            return establish;
        }

        public void setEstablish(String establish) {
            this.establish = establish;
        }

        public String getAccepts() {
            return accepts;
        }

        public void setAccepts(String accepts) {
            this.accepts = accepts;
        }

        public String getOfficeHour() {
            return officeHour;
        }

        public void setOfficeHour(String officeHour) {
            this.officeHour = officeHour;
        }

        public Object getPackagePrice() {
            return packagePrice;
        }

        public void setPackagePrice(Object packagePrice) {
            this.packagePrice = packagePrice;
        }

        public Object getPackagePriceDetail() {
            return packagePriceDetail;
        }

        public void setPackagePriceDetail(Object packagePriceDetail) {
            this.packagePriceDetail = packagePriceDetail;
        }

        public String getFlagvalue() {
            return flagvalue;
        }

        public void setFlagvalue(String flagvalue) {
            this.flagvalue = flagvalue;
        }

    }
}
