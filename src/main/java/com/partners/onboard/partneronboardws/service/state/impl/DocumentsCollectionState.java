package com.partners.onboard.partneronboardws.service.state.impl;

import com.partners.onboard.partneronboardws.enums.CompletionStates;
import com.partners.onboard.partneronboardws.enums.DriverOnboardingProcessStates;
import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import org.springframework.stereotype.Service;

@Service
public class DocumentsCollectionState implements DriverState {
    @Override
    public void processApplication(Driver driver) throws DriverStateFailureException {
        try {
            System.out.println("starting documents collection process");
            driver.getApplication().setStatus(DriverOnboardingProcessStates.DOCUMENT_COLLECTION.name()+ CompletionStates._STARTED);

            uploadDocuments();
            driver.getApplication().setStatus(DriverOnboardingProcessStates.DOCUMENT_COLLECTION.name() + CompletionStates._COMPLETED);
            System.out.println("completed documents collection process");
            driver.getApplication().getApplicationInstances().add(this.getClass());

        } catch (RuntimeException e) {
            System.out.println("Exception occurred: "+e.getMessage());
            driver.getApplication().setFailedReason(e.getMessage());
            driver.getApplication().setStatus(DriverOnboardingProcessStates.DOCUMENT_COLLECTION.name()+CompletionStates._FAILED);
        }

    }

    public void uploadDocuments() {
        System.out.println("uploading documents to S3 storage");
    }

}
