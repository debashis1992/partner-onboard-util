package com.partners.onboard.partneronboardws.service;

import com.partners.onboard.partneronboardws.exception.VerifyLinkExpiredException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.DriverEmailVerificationRequest;
import com.partners.onboard.partneronboardws.repository.DriverRepository;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.state.impl.AddProfileInfoState;
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class DriverService {

    @Value("${ttl.verification-link-in-seconds}")
    private int verificationLinkTtl;

    @Autowired
    private AddProfileInfoState addProfileInfoState;

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

        Date callbackUrlTimestamp = driverEmailVerificationRequest.getCallbackUrlTimestamp();
        Date t1 = new Date();
        long diff = (t1.getTime() - callbackUrlTimestamp.getTime())/1000;
        if(diff > verificationLinkTtl)
            throw new VerifyLinkExpiredException("verification link expired");

        // mark the driver as verified

        Optional<Driver> driver = driverRepository.getDriverByEmail(driverEmailVerificationRequest.getEmail());
        if(driver.isPresent()) {
            driver.get().setAndGetDriverState(addProfileInfoState);
            return driver.get();
        }

        log.error("Unknown error. Driver not found but clicked on verify");
        return null;
    }

    public Optional<Driver> getDriverDetails(String id) {
        return driverRepository.getDriver(id);
    }



}
