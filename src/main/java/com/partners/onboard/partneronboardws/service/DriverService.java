package com.partners.onboard.partneronboardws.service;

import com.partners.onboard.partneronboardws.exception.DriverAlreadyExistsException;
import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.exception.ServiceApiFailureException;
import com.partners.onboard.partneronboardws.exception.VerifyLinkExpiredException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.DriverEmailVerificationRequest;
import com.partners.onboard.partneronboardws.repository.DriverRepository;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.state.impl.AddProfileInfoState;
import com.partners.onboard.partneronboardws.service.state.impl.ReadyToRideState;
import com.partners.onboard.partneronboardws.service.state.impl.ShipTrackingDeviceState;
import com.partners.onboard.partneronboardws.service.state.impl.VerifyProfileState;
import com.partners.onboard.partneronboardws.utils.DriverUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class DriverService {

    @Value("${ttl.verification-link-in-seconds}")
    private int verificationLinkTtl;

    @Autowired
    private VerifyProfileState verifyProfileState;

    @Autowired
    private AddProfileInfoState addProfileInfoState;

    @Autowired
    private ShipTrackingDeviceState shipTrackingDeviceState;

    @Autowired
    private ReadyToRideState readyToRideState;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DriverUtils driverUtils;

    public Driver signUp(String email) {
        // driver signup
        // creating a new default driver
        // creating a new application for the driver onboarding process

        Optional<Driver> driverOptional = driverRepository.getDriverByEmail(email);
        if(driverOptional.isPresent()) {
            throw new DriverAlreadyExistsException("email already in-use. Please provide a different email");
        }

        Driver driver = new Driver();
        driver.setEmail(email);
        driverRepository.addDriver(driver);
        DriverState state = driver.getDriverState();
        state.processApplication(driver);
        log.info("Created a new driver with id: " + driver.getId());

        try {
            generateAndSendVerifyLink(email);
        } catch (DriverNotFoundException e) {
            log.error("recently added driver with email:{} was not found. Something went wrong!",email);
        }
        return driver;
    }

    public DriverEmailVerificationRequest generateAndSendVerifyLink(String email) throws DriverNotFoundException {
        driverUtils.getDriverDetailsByEmail(email);

        log.info("creating verify link for email: {}", email);

        //send this payload to notifier-ws to email the user
        DriverEmailVerificationRequest driverEmailVerificationRequest = DriverEmailVerificationRequest.builder().email(email)
                .callbackUrlTimestamp(new Date()).build();

        CompletableFuture.runAsync(() -> {
            try {
                notificationService.sendEmail(email, driverEmailVerificationRequest);
                log.info("successfully sent verification link");
            } catch (ServiceApiFailureException e) {
                log.error("Exception occurred while sending email: {}", e.getMessage());
            }
        });

        return driverEmailVerificationRequest;
    }


    public Driver verifyEmail(DriverEmailVerificationRequest driverEmailVerificationRequest) throws VerifyLinkExpiredException, DriverNotFoundException {

        log.info("got confirm verification request for driver : {}", driverEmailVerificationRequest.getEmail());

        Driver driver = driverUtils.getDriverDetailsByEmail(driverEmailVerificationRequest.getEmail());
        DriverState state = driver.setAndGetDriverState(verifyProfileState);

        state.processApplication(driver);
        Driver driverOut = verifyProfileState.processApplication(driverEmailVerificationRequest, driver);
        return driverOut;
    }


    public void triggerShipTrackingDevice(String id) throws DriverNotFoundException {
        //initiate ship tracking device

        Driver driver = driverUtils.getDriverDetails(id);

        DriverState state = driver.setAndGetDriverState(shipTrackingDeviceState);
        state.processApplication(driver);
    }


    public void markDriverReadyToDrive(String id) throws DriverNotFoundException {
        Driver driver = driverUtils.getDriverDetails(id);

        DriverState state = driver.setAndGetDriverState(readyToRideState);
        state.processApplication(driver);

    }

    public Driver getDriverDetails(String email) throws DriverNotFoundException {
        return driverUtils.getDriverDetailsByEmail(email);
    }
}
