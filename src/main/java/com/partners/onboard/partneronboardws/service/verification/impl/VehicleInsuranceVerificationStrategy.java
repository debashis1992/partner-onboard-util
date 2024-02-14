package com.partners.onboard.partneronboardws.service.verification.impl;

import com.partners.onboard.partneronboardws.service.verification.VerificationStrategy;

public class VehicleInsuranceVerificationStrategy implements VerificationStrategy {
    @Override
    public boolean verifyDocuments(int cityPin) throws RuntimeException {
        System.out.println("verifying vehicle details based on insurance");
        return true;
    }
}
