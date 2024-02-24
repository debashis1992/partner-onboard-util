package com.partners.onboard.partneronboardws.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data @NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) @Builder
public class DriverResponse {
    String id;
    String message;
    String email;
}
