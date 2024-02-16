package com.partners.onboard.partneronboardws.service.state.impl;

import com.partners.onboard.partneronboardws.enums.CompletionStates;
import com.partners.onboard.partneronboardws.enums.DriverOnboardingProcessStates;
import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.verification.VerificationStrategy;
import com.partners.onboard.partneronboardws.service.verification.impl.VerificationRules;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackgroundVerificationState implements DriverState {

    private VerificationRules verificationRules;
    public BackgroundVerificationState(VerificationRules verificationRules) {
        this.verificationRules = verificationRules;
    }
    @Override
    public void processApplication(Driver driver) throws DriverStateFailureException {

        try {
            System.out.println("starting background verification process");
            driver.getApplication().setStatus(DriverOnboardingProcessStates.BACKGROUND_VERIFICATION.name()+ CompletionStates._STARTED);
            triggerBackgroundVerificationStep(driver);

            driver.getApplication().setStatus(DriverOnboardingProcessStates.BACKGROUND_VERIFICATION.name()+CompletionStates._COMPLETED);
            driver.getApplication().getApplicationInstances().add(this.getClass());
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
        return true;
    }
}
