package com.partners.onboard.partneronboardws.model.documents;

import com.partners.onboard.partneronboardws.enums.VerificationStatus;

import java.util.Date;

public class DriverDocuments {
    String licenseNumber;
    Date validUpto;
    String registeredCity;
    Object photo;

    String verificationStatus;
    Date verifiedDate;

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public Date getValidUpto() {
        return validUpto;
    }

    public String getRegisteredCity() {
        return registeredCity;
    }

    public Object getPhoto() {
        return photo;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public Date getVerifiedDate() {
        return verifiedDate;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setValidUpto(Date validUpto) {
        this.validUpto = validUpto;
    }

    public void setRegisteredCity(String registeredCity) {
        this.registeredCity = registeredCity;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public void setVerifiedDate(Date verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public DriverDocuments() {
        verificationStatus = VerificationStatus.NOT_VERIFIED.name();
    }
}
