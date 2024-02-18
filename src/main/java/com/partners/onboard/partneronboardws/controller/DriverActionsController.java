package com.partners.onboard.partneronboardws.controller;

import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.exception.VerifyLinkExpiredException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.DriverEmailVerificationRequest;
import com.partners.onboard.partneronboardws.model.DriverResponse;
import com.partners.onboard.partneronboardws.service.DriverDocumentService;
import com.partners.onboard.partneronboardws.service.DriverService;
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
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private DriverDocumentService driverDocumentService;


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
                                                                             @NotNull @Email(message = "please use a valid email") String email) throws ParseException {

        log.info("creating verify link for email: {}", email);
        DriverEmailVerificationRequest driverEmailVerificationRequest = DriverEmailVerificationRequest.builder().email(email)
                .callbackUrlTimestamp(
                        new Date()
                        //DateUtils.getCurrentDateBasedOnDateSerializerFormat()
                ).build();
        return ResponseEntity.ok().location(URI.create(baseUrl+"/verify")).body(driverEmailVerificationRequest);

    }

    @PostMapping("/verify")
    public ResponseEntity<Driver> confirmDriverEmailLinkVerification(@RequestBody @Validated DriverEmailVerificationRequest driverEmailVerificationRequest) throws VerifyLinkExpiredException {

        log.info("got confirm verification request for driver : {}", driverEmailVerificationRequest.getEmail());
        return ResponseEntity.ok().body(driverService.verifyEmail(driverEmailVerificationRequest));
    }

    @GetMapping("/required-documents")
    public ResponseEntity<List<String>> getRequiredDocuments(@Param("email") @NotEmpty @NotNull String email) throws DriverNotFoundException {

        return ResponseEntity.ok(driverDocumentService.getRequiredDocuments(email));
    }

    @





    @GetMapping("/state")
    public ResponseEntity<Optional<Driver>> getDriverStateInfo(@Param("email") @NotEmpty @NotNull String email) {
        return ResponseEntity.ok().body(driverService.getDriverDetails(email));
    }

}
