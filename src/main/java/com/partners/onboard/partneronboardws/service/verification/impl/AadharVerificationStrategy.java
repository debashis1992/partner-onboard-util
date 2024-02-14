package com.partners.onboard.partneronboardws.service.verification.impl;

import com.partners.onboard.partneronboardws.service.verification.VerificationStrategy;

public class AadharVerificationStrategy implements VerificationStrategy {
    @Override
    public boolean verifyDocuments(int cityPin) throws RuntimeException {
        System.out.println("verifying based on aadhar");
        return true;
    }
}
