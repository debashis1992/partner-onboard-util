package com.partners.onboard.partneronboardws.service.verification;

public interface VerificationStrategy {

    boolean verifyDocuments(int cityPin) throws RuntimeException;
}
