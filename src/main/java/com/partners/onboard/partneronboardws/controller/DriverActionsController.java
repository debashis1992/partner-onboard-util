package com.partners.onboard.partneronboardws.controller;

import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.exception.VerifyLinkExpiredException;
import com.partners.onboard.partneronboardws.model.ApiResponse;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.DriverEmailVerificationRequest;
import com.partners.onboard.partneronboardws.model.DriverResponse;
import com.partners.onboard.partneronboardws.model.documents.Document;
import com.partners.onboard.partneronboardws.service.DriverDocumentService;
import com.partners.onboard.partneronboardws.service.DriverService;
import com.partners.onboard.partneronboardws.service.NotificationService;
import jakarta.validation.Valid;
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
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
                                                                             @NotNull @Email(message = "please use a valid email") String email) throws InterruptedException {

        log.info("creating verify link for email: {}", email);
        DriverEmailVerificationRequest driverEmailVerificationRequest = DriverEmailVerificationRequest.builder().email(email)
                .callbackUrlTimestamp(
                        new Date()
                ).build();

        //send this payload to notifier-ws to email the user
        CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return notificationService.sendVerificationLink(email);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        completableFuture.whenComplete((req, throwable) -> {
            if(throwable!=null) {
                log.error("exception occurred in sending notification: {}", throwable.getMessage());
            }
        });
        return ResponseEntity.ok().location(URI.create(baseUrl+"/verify")).body(driverEmailVerificationRequest);

    }

    @PostMapping("/verify")
    public ResponseEntity<Driver> confirmDriverEmailLinkVerification(@RequestBody @Validated DriverEmailVerificationRequest driverEmailVerificationRequest) throws VerifyLinkExpiredException {

        log.info("got confirm verification request for driver : {}", driverEmailVerificationRequest.getEmail());
        return ResponseEntity.ok().body(driverService.verifyEmail(driverEmailVerificationRequest));
    }

    @PostMapping("/ship-tracking-device")
    public ResponseEntity<ApiResponse> triggerShipTrackingDevice(@Param("id") @NotEmpty @NotNull String id) {

        driverService.triggerShipTrackingDevice(id);
        return ResponseEntity.ok(ApiResponse.builder().message("started ship tracking device process").build());
    }

    @PostMapping("/ready-to-drive")
    public ResponseEntity<ApiResponse> markDriverReadyToDrive(@Param("id") @NotEmpty @NotNull String id) {

        driverService.markDriverReadyToDrive(id);
        return ResponseEntity.ok(ApiResponse.builder().message("driver marked as ready to drive").build());
    }

    @GetMapping("/state")
    public ResponseEntity<Optional<Driver>> getDriverStateInfo(@Param("email") @NotEmpty @NotNull String email) {
        return ResponseEntity.ok().body(driverService.getDriverDetails(email));
    }

}
