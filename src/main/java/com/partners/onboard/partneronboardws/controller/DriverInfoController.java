package com.partners.onboard.partneronboardws.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.records.ApiResponse;
import com.partners.onboard.partneronboardws.records.DriverInfoResponse;
import com.partners.onboard.partneronboardws.service.DriverInfoService;
import com.partners.onboard.partneronboardws.service.DriverService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/driver-info")
public class DriverInfoController {

    @Autowired
    private DriverInfoService driverInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProfileInfo(@Param("id") @NotEmpty @NotNull String id,
                                                      @RequestBody String requestBody) throws IOException, DriverNotFoundException {

        var attributes = objectMapper.readValue(requestBody, Map.class);
        driverInfoService.addProfileInfo(id, attributes);

        return ResponseEntity.ok(new ApiResponse("driver profile info was added successfully"));
    }

    @PutMapping("/update")
    public void updateProfileInfo(@Param("id") @NotEmpty @NotNull String id, @RequestBody String requestBody) throws IOException, DriverNotFoundException {

        var attributes = objectMapper.readValue(requestBody, Map.class);
        driverInfoService.updateProfileInfo(id, attributes);

    }

    @GetMapping
    public ResponseEntity<DriverInfoResponse> getProfileInfo(@Param("id") @NotEmpty @NotNull String id) throws DriverNotFoundException {

        return ResponseEntity.ok(driverInfoService.getDriverInfo(id));
    }
}
