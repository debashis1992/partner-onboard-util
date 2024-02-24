package com.partners.onboard.partneronboardws.records;

public record DriverInfoResponse(String firstName, String lastName,
                                 String email, int phoneNumber, int cityPin) {
}
