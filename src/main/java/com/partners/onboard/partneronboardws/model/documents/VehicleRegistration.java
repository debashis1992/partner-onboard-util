package com.partners.onboard.partneronboardws.model.documents;

import com.partners.onboard.partneronboardws.enums.VerificationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data @AllArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleRegistration extends Document {
    String vehicleNumber;
    String vehicleName;
    String chassisNumber;
    boolean IsInsured;
    String insuranceNumber;
    Date insuranceExpiryDate;

    String verificationStatus;
    Date verifiedDate;

    public VehicleRegistration() {
        verificationStatus = VerificationStatus.NOT_VERIFIED.name();
    }
}
