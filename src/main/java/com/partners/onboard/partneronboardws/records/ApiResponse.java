package com.partners.onboard.partneronboardws.records;

import lombok.Builder;

import java.util.Date;

@Builder
public record ApiResponse(String uri, Date timestamp, String message, Integer code) {

    public ApiResponse(String message) {
        this(null, null, message, null);
    }
}
