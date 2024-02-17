package com.partners.onboard.partneronboardws.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.partners.onboard.partneronboardws.config.CustomDateDeserializer;
import com.partners.onboard.partneronboardws.config.CustomDateSerializer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.TimeZone;

@Data @NoArgsConstructor @AllArgsConstructor
@Validated @Builder
public class DriverEmailVerificationRequest {

    @Email @NotNull @NotEmpty(message = "email cannot be empty")
    String email;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    Date callbackUrlTimestamp;
}
