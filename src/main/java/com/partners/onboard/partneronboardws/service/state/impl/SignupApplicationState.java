package com.partners.onboard.partneronboardws.service.state.impl;

import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.OnboardingApplication;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.enums.CompletionStates;
import com.partners.onboard.partneronboardws.enums.DriverProcessStates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SignupApplicationState implements DriverState {
    @Override
    public void processApplication(Driver driver) throws DriverStateFailureException {
        try {
            OnboardingApplication application = new OnboardingApplication(driver,
                    DriverProcessStates.SIGN_UP + CompletionStates._STARTED.toString(), null, null);

            application.setStatus(DriverProcessStates.SIGN_UP.name()+CompletionStates._COMPLETED);
            application.getApplicationInstances().add(this.getClass());
        } catch (RuntimeException e) {
            log.error("Exception occurred: " + e.getMessage());
            driver.getApplication().setFailedReason(e.getMessage());
            driver.getApplication().setStatus(DriverProcessStates.SIGN_UP + CompletionStates._FAILED.toString());
            throw new DriverStateFailureException(e.getMessage());
        }
    }
}
