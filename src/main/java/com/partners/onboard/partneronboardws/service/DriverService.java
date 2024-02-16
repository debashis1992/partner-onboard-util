package com.partners.onboard.partneronboardws.service;

import com.partners.onboard.partneronboardws.exception.VerifyLinkExpiredException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.DriverEmailVerificationRequest;
import com.partners.onboard.partneronboardws.repository.DriverRepository;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.state.impl.AddProfileInfoState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;


@Service
@Slf4j
public class DriverService {

    @Value("${ttl.verification-link-in-seconds}")
    int verificationLinkTtl;

    @Autowired
    private AddProfileInfoState addProfileInfoState;

    @Autowired
    private DriverRepository driverRepository;

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
        return driver;
    }


    public Driver verifyEmail(DriverEmailVerificationRequest driverEmailVerificationRequest) throws VerifyLinkExpiredException {

        Date callbackUrlTimestamp = driverEmailVerificationRequest.getCallbackUrlTimestamp();
        LocalDateTime t1 = LocalDateTime.now();
//        LocalDateTime t2 =
                if(callbackUrlTimestamp.toInstant().isAfter(Instant.ofEpochSecond(verificationLinkTtl))) {
                    throw new VerifyLinkExpiredException("verification link expired");
                }

//        Duration duration = Duration.between(t1, t2);
//
//        long diff = duration.toSeconds();
//        if(diff > verificationLinkTtl)
//            throw new VerifyLinkExpiredException("verification link expired");

        // mark the driver as verified

        Optional<Driver> driver = driverRepository.getDriver(driverEmailVerificationRequest.getEmail());
        if(driver.isPresent()) {
            driver.get().setAndGetDriverState(addProfileInfoState);
            return driver.get();
        }

        log.error("Unknown error. Driver not found but clicked on verify");
        return null;
    }

}
