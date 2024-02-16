package com.partners.onboard.partneronboardws.controller;

import com.partners.onboard.partneronboardws.exception.VerifyLinkExpiredException;
import com.partners.onboard.partneronboardws.model.ApiResponse;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.DriverEmailVerificationRequest;
import com.partners.onboard.partneronboardws.model.DriverResponse;
import com.partners.onboard.partneronboardws.service.DriverService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/driver")
@Validated
@Slf4j
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/sign-up")
    public ResponseEntity<DriverResponse> signUp(@Param("email") @NotEmpty(message = "email cannot be empty")
                                                     @NotNull @Email(message = "please use a valid email") String email) {
        Driver driver = driverService.signUp(email);

        DriverResponse response = DriverResponse.builder().id(driver.getId())
                .message("please check email for verification").email(driver.getEmail()).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Driver> confirmDriverEmailLinkVerification(@RequestBody @Validated DriverEmailVerificationRequest driverEmailVerificationRequest) throws VerifyLinkExpiredException {

        log.info("got confirm verification request for driver : {}", driverEmailVerificationRequest.getEmail());
        return ResponseEntity.ok().body(driverService.verifyEmail(driverEmailVerificationRequest));

    }


}
