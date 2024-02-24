package com.partners.onboard.partneronboardws.exception;

public class ServiceApiFailureException extends Exception {
    private int message;
    public ServiceApiFailureException(String message) {
        super(message);
    }
}
