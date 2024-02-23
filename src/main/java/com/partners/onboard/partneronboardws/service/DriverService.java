package com.partners.onboard.partneronboardws.service;

import com.partners.onboard.partneronboardws.exception.VerifyLinkExpiredException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.DriverEmailVerificationRequest;
import com.partners.onboard.partneronboardws.repository.DriverRepository;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.state.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
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
    private DocumentsCollectionState documentsCollectionState;

    @Autowired
    private ShipTrackingDeviceState shipTrackingDeviceState;

    @Autowired
    private ReadyToRideState readyToRideState;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private NotificationService notificationService;

    public Driver signUp(String email) {
        // driver signup
        // creating a new default driver
        // creating a new application for the driver onboarding process

        Driver driver = new Driver();
        driver.setEmail(email);
        driverRepository.addDriver(driver);
        DriverState state = driver.getDriverState();
        state.processApplication(driver);
        log.info("Created a new driver with id: "+driver.getId());

        CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> notificationService.sendEmail());
        completableFuture.whenComplete((res, throwable) -> {
            if(throwable==null) {
                System.out.println("sent verification email");
            } else {
                log.error("Exception occurred while sending email: {}", throwable.getMessage());
            }
        });

        return driver;
    }


    public Driver verifyEmail(DriverEmailVerificationRequest driverEmailVerificationRequest) throws VerifyLinkExpiredException {

        Optional<Driver> driver = driverRepository.getDriverByEmail(driverEmailVerificationRequest.getEmail());
        if(driver.isPresent()) {
            DriverState driverState = driver.get().setAndGetDriverState(verifyProfileState);
//            driverState.processApplication(driver.get());
            verifyProfileState.processApplication(driver.get());
            Driver driverOut = verifyProfileState.processApplication(driverEmailVerificationRequest, driver.get());
            return driverOut;
        }

        throw new VerifyLinkExpiredException("driver with email "+driverEmailVerificationRequest.getEmail()+" was not found!");
    }


    public Optional<Driver> getDriverDetails(String email) {
        return driverRepository.getDriverByEmail(email);
    }

    public void addProfileInfo(String id, Map<String, String> attributesMap) {

        Optional<Driver> driverOptional = driverRepository.getDriver(id);
        if(driverOptional.isPresent()) {
            Driver driver = driverOptional.get();

            addProfileInfoState.processApplication(driver);
            addProfileInfoState.updateDriverApplication(driver, attributesMap);

            //after profile info is added, we'll make the current state as DocumentCollectionState
            driver.setAndGetDriverState(documentsCollectionState);
        }
    }


    public void triggerShipTrackingDevice(String id) {
        //initiate ship tracking device

        Optional<Driver> driverOptional = driverRepository.getDriver(id);
        if(driverOptional.isPresent()) {
            Driver driver = driverOptional.get();

            DriverState state = driver.setAndGetDriverState(shipTrackingDeviceState);
            state.processApplication(driver);
        }
    }


    public void markDriverReadyToDrive(String id) {
        Optional<Driver> driverOptional = driverRepository.getDriver(id);
        if(driverOptional.isPresent()) {
            Driver driver = driverOptional.get();

            DriverState state = driver.setAndGetDriverState(readyToRideState);
            state.processApplication(driver);
        }
    }
}
