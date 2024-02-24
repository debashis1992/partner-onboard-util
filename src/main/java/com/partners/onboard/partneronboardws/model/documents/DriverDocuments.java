package com.partners.onboard.partneronboardws.model.documents;

import com.partners.onboard.partneronboardws.enums.VerificationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data @AllArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverDocuments extends Document {
    String licenseNumber;
    Date validUpto;
    String registeredCity;

    String verificationStatus;
    Date verifiedDate;

    public DriverDocuments() {
        verificationStatus = VerificationStatus.NOT_VERIFIED.name();
    }
}
