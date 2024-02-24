package com.partners.onboard.partneronboardws.service.state.impl;

import com.partners.onboard.partneronboardws.constant.ExceptionMessages;
import com.partners.onboard.partneronboardws.enums.CompletionStates;
import com.partners.onboard.partneronboardws.enums.DriverProcessStates;
import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.exception.VerifyLinkExpiredException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.DriverEmailVerificationRequest;
import com.partners.onboard.partneronboardws.model.OnboardingApplication;
import com.partners.onboard.partneronboardws.repository.DriverRepository;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service @Slf4j
public class VerifyProfileState implements DriverState {
    @Value("${ttl.verification-link-in-seconds}")
    private int verificationLinkTtl;

    @Autowired
    private AddProfileInfoState addProfileInfoState;

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public void processApplication(Driver driver) throws DriverStateFailureException {
        isValidRequest(driver);
    }

    @Override
    public Driver processApplication(DriverEmailVerificationRequest driverEmailVerificationRequest, Driver driver) throws VerifyLinkExpiredException {
        try {

            driver.getApplication().setStatus(DriverProcessStates.VERIFY_PROFILE+CompletionStates._STARTED.name());
            Date callbackUrlTimestamp = driverEmailVerificationRequest.getCallbackUrlTimestamp();
            Date t1 = new Date();
            long diff = (t1.getTime() - callbackUrlTimestamp.getTime()) / 1000;
            if (diff > verificationLinkTtl)
                throw new VerifyLinkExpiredException("verification link expired");

            // mark the driver as verified
            driver.getApplication().addCompletedApplicationInstances(this.getClass());
            driver.getApplication().setStatus(DriverProcessStates.VERIFY_PROFILE+CompletionStates._COMPLETED.name());
            return driver;

        } catch (VerifyLinkExpiredException e) {
            driver.getApplication().setStatus(DriverProcessStates.VERIFY_PROFILE + CompletionStates._FAILED.toString());
            throw e;
        }
    }
}
