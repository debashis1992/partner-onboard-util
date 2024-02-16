package com.partners.onboard.partneronboardws.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data @NoArgsConstructor @Validated
public class DriverEmailVerificationRequest {

    @Email @NotNull @NotEmpty(message = "email cannot be empty")
    String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy@HH:mm:ss")
    Date callbackUrlTimestamp;
}
