package com.partners.onboard.partneronboardws.service.state.impl;

import com.partners.onboard.partneronboardws.enums.CompletionStates;
import com.partners.onboard.partneronboardws.enums.DriverProcessStates;
import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.OnboardingApplication;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AddProfileInfoState implements DriverState {
    @Override
    public void processApplication(Driver driver) throws DriverStateFailureException {

        isValidRequest(driver);

        try {
            driver.getApplication().setStatus(DriverProcessStates.PROFILE_INFO.name()+CompletionStates._STARTED);
            driver.getApplication().getCompletedApplicationInstances().add(this.getClass());
        } catch (DriverStateFailureException e) {
            driver.getApplication().setFailedReason(e.getMessage());
            driver.getApplication().setStatus(DriverProcessStates.PROFILE_INFO.name() + CompletionStates._FAILED.toString());
            throw new DriverStateFailureException(e.getMessage());
        }
    }


    @Override
    public void updateDriverApplication(Driver driver, Map<String,String> attributes) {
        try {
            driver.getApplication().setStatus(DriverProcessStates.PROFILE_INFO + CompletionStates._STARTED.toString());
            attributes.forEach((k, v) -> {
                switch (k) {
                    case "firstName" -> driver.setFirstName(v);
                    case "lastName" -> driver.setLastName(v);
                    case "phone" -> driver.setPhoneNumber(Integer.parseInt(v));
                    case "email" -> driver.setEmail(v);
                    case "pin" -> driver.setCityPin(Integer.parseInt(v));
                }
            });

            System.out.println("completed adding profile information");
            driver.getApplication().setStatus(DriverProcessStates.PROFILE_INFO + CompletionStates._COMPLETED.toString());
        } catch (RuntimeException e) {
            driver.getApplication().setFailedReason(e.getMessage());
            driver.getApplication().setStatus(DriverProcessStates.PROFILE_INFO + CompletionStates._FAILED.toString());
        }
    }
}
