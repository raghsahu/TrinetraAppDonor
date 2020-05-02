package com.trinetraapp.model.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 02-May-20.
 */
public class LoginModelData {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("user_fullname")
    @Expose
    private String userFullname;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_password")
    @Expose
    private String userPassword;
    @SerializedName("user_type_id")
    @Expose
    private String userTypeId;
    @SerializedName("user_bdate")
    @Expose
    private String userBdate;
    @SerializedName("school_name")
    @Expose
    private String schoolName;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("is_email_varified")
    @Expose
    private String isEmailVarified;
    @SerializedName("varified_token")
    @Expose
    private String varifiedToken;
    @SerializedName("user_gcm_code")
    @Expose
    private String userGcmCode;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("gstin_no")
    @Expose
    private String gstinNo;
    @SerializedName("user_ios_token")
    @Expose
    private String userIosToken;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("reference")
    @Expose
    private String reference;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(String userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getUserBdate() {
        return userBdate;
    }

    public void setUserBdate(String userBdate) {
        this.userBdate = userBdate;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getIsEmailVarified() {
        return isEmailVarified;
    }

    public void setIsEmailVarified(String isEmailVarified) {
        this.isEmailVarified = isEmailVarified;
    }

    public String getVarifiedToken() {
        return varifiedToken;
    }

    public void setVarifiedToken(String varifiedToken) {
        this.varifiedToken = varifiedToken;
    }

    public String getUserGcmCode() {
        return userGcmCode;
    }

    public void setUserGcmCode(String userGcmCode) {
        this.userGcmCode = userGcmCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGstinNo() {
        return gstinNo;
    }

    public void setGstinNo(String gstinNo) {
        this.gstinNo = gstinNo;
    }

    public String getUserIosToken() {
        return userIosToken;
    }

    public void setUserIosToken(String userIosToken) {
        this.userIosToken = userIosToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
