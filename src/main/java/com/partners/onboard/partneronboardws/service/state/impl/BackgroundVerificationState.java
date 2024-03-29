package com.partners.onboard.partneronboardws.service.state.impl;

import com.partners.onboard.partneronboardws.enums.CompletionStates;
import com.partners.onboard.partneronboardws.enums.DriverOnboardingProcessStates;
import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.documents.Document;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.verification.VerificationStrategy;
import com.partners.onboard.partneronboardws.service.verification.impl.VerificationRules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service @Slf4j
public class BackgroundVerificationState implements DriverState {

    @Autowired
    private VerificationRules verificationRules;

    @Override
    public void processApplication(Driver driver) throws DriverStateFailureException {

        try {
            // just add document collection state as completed
            driver.getApplication().addCompletedApplicationInstances(DocumentsCollectionState.class);
            isValidRequest(driver);
            System.out.println("starting background verification process");
            driver.getApplication().setStatus(DriverOnboardingProcessStates.BACKGROUND_VERIFICATION.name()+ CompletionStates._STARTED);
            triggerBackgroundVerificationStep(driver);

            driver.getApplication().setStatus(DriverOnboardingProcessStates.BACKGROUND_VERIFICATION.name()+CompletionStates._COMPLETED);
            driver.getApplication().getCompletedApplicationInstances().add(this.getClass());
        } catch (DriverStateFailureException e) {
            System.out.println("background verification failed due to error: "+e.getMessage());
            driver.getApplication().setFailedReason(e.getMessage());
            driver.getApplication().setStatus(DriverOnboardingProcessStates.BACKGROUND_VERIFICATION.name()+CompletionStates._FAILED);
            throw e;
        }
    }

    public boolean triggerBackgroundVerificationStep(Driver driver) throws RuntimeException {

        List<VerificationStrategy> verificationStrategyList = verificationRules.getRules(driver.getCityPin());
        if(verificationStrategyList.isEmpty()) {
            throw new DriverStateFailureException("No verification rules are defined in this city with pin: "+ driver.getCityPin());
        }

        System.out.println("Found rules: "+verificationStrategyList.size());

        for(VerificationStrategy strategy : verificationStrategyList) {
            strategy.verifyDocuments(driver.getCityPin());
        }

        List<Document> documents = driver.getDocuments();
        if(documents.isEmpty()) {
            CompletableFuture<Boolean> completableFutures = CompletableFuture.supplyAsync(() -> {
                try {
                    documents.forEach(this::verifyDocument);
                    return true;
                } catch (RuntimeException e) {
                    log.error("exception occurred: {}", e.getMessage());
                    return false;
                }
            });
        }
        return true;
    }

    public boolean verifyDocument(Document document) throws RuntimeException {
        //calling document-verification-svc and verifying document
        return true;
    }
}
