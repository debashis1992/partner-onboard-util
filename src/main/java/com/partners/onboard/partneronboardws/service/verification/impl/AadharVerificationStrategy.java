package com.partners.onboard.partneronboardws.service.verification.impl;

import com.partners.onboard.partneronboardws.enums.DocumentTypes;
import com.partners.onboard.partneronboardws.service.verification.VerificationStrategy;
import org.springframework.stereotype.Service;

@Service
public class AadharVerificationStrategy implements VerificationStrategy {
    @Override
    public boolean verifyDocuments(int cityPin) throws RuntimeException {
        System.out.println("verifying based on aadhar");
        return true;
    }

    @Override
    public String requiredDocument() {
        return DocumentTypes.AADHAR.name();
    }
}
