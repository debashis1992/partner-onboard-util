package com.partners.onboard.partneronboardws.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.partners.onboard.partneronboardws.service.DriverService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/driver-info")
public class DriverInfoController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/add")
    public void addProfileInfo(@Param("id") @NotEmpty @NotNull String id, @RequestBody String requestBody) throws IOException {

        Map<String, String> attributes = objectMapper.readValue(requestBody, Map.class);
        driverService.addProfileInfo(id, attributes);
    }

    @PutMapping("/update")
    public void updateProfileInfo() {

    }

    @GetMapping
    public void getProfileInfo(String id) {

    }
}
