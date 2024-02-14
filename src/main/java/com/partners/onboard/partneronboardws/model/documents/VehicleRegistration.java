package com.partners.onboard.partneronboardws.model.documents;

import com.partners.onboard.partneronboardws.enums.VerificationStatus;

import java.util.Date;

public class VehicleRegistration {
    String vehicleNumber;
    String vehicleName;
    String chassisNumber;
    boolean IsInsured;
    String insuranceNumber;
    Date insuranceExpiryDate;

    String verificationStatus;
    Date verifiedDate;

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public boolean isInsured() {
        return IsInsured;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public Date getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public Date getVerifiedDate() {
        return verifiedDate;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public void setInsured(boolean insured) {
        IsInsured = insured;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public void setInsuranceExpiryDate(Date insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public void setVerifiedDate(Date verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public VehicleRegistration() {
        verificationStatus = VerificationStatus.NOT_VERIFIED.name();
    }
}
