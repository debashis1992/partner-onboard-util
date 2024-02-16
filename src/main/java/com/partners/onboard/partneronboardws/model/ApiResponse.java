package com.partners.onboard.partneronboardws.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class ApiResponse {
    String uri;
    Date timestamp;
    String message;
    int code;
}
