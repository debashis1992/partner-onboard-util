package com.partners.onboard.partneronboardws.controller;

import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.exception.VerifyLinkExpiredException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.DriverEmailVerificationRequest;
import com.partners.onboard.partneronboardws.model.DriverResponse;
import com.partners.onboard.partneronboardws.records.ApiResponse;
import com.partners.onboard.partneronboardws.service.DriverService;
import com.partners.onboard.partneronboardws.service.NotificationService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/driver")
@Validated
@Slf4j
public class DriverActionsController {

    @Value("${url.base}")
    String baseUrl;

    @Autowired
    private DriverService driverService;

    @Autowired
    private NotificationService notificationService;


    @PostMapping("/sign-up")
    public ResponseEntity<DriverResponse> signUp(@Param("email") @NotEmpty(message = "email cannot be empty")
                                                     @NotNull @Email(message = "please use a valid email") String email) {
        Driver driver = driverService.signUp(email);

        DriverResponse response = DriverResponse.builder().id(driver.getId())
                .message("please check email for verification").email(driver.getEmail()).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/generate-verify-link")
    public ResponseEntity<DriverEmailVerificationRequest> generateVerifyLink(@Param("email") @NotEmpty(message = "email cannot be empty")
                                                                             @NotNull @Email(message = "please use a valid email") String email) throws DriverNotFoundException {

        DriverEmailVerificationRequest driverEmailVerificationRequest = driverService.generateAndSendVerifyLink(email);
        return ResponseEntity.ok().location(URI.create(baseUrl+"/verify")).body(driverEmailVerificationRequest);
    }

    @PostMapping("/verify")
    public ResponseEntity<Driver> confirmDriverEmailLinkVerification(@RequestBody @Validated DriverEmailVerificationRequest driverEmailVerificationRequest)
            throws VerifyLinkExpiredException, DriverNotFoundException {

        return ResponseEntity.ok().body(driverService.verifyEmail(driverEmailVerificationRequest));
    }

    @PostMapping("/ship-tracking-device")
    public ResponseEntity<ApiResponse> triggerShipTrackingDevice(@Param("id") @NotEmpty @NotNull String id) throws DriverNotFoundException {

        driverService.triggerShipTrackingDevice(id);
        return ResponseEntity.ok(new ApiResponse("started ship tracking device process"));
    }

    @PostMapping("/ready-to-drive")
    public ResponseEntity<ApiResponse> markDriverReadyToDrive(@Param("id") @NotEmpty @NotNull String id) throws DriverNotFoundException {

        driverService.markDriverReadyToDrive(id);
        return ResponseEntity.ok(new ApiResponse("driver marked as ready to drive"));
    }

    @GetMapping("/state")
    public ResponseEntity<Driver> getDriverStateInfo(@Param("email") @NotEmpty @NotNull String email) throws DriverNotFoundException {
        return ResponseEntity.ok().body(driverService.getDriverDetails(email));
    }

}
