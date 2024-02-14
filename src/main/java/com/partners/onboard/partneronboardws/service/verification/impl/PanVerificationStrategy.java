package com.partners.onboard.partneronboardws.service.verification.impl;

import com.partners.onboard.partneronboardws.service.verification.VerificationStrategy;

public class PanVerificationStrategy implements VerificationStrategy {
    @Override
    public boolean verifyDocuments(int cityPin) throws RuntimeException {
        System.out.println("verifying based on pan card");
        return true;
    }
}
