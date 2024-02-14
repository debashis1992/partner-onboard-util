package com.partners.onboard.partneronboardws.model;

import com.partners.onboard.partneronboardws.service.state.DriverState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OnboardingApplication {
    String id;
    Driver driver;
    String status;
    String failedReason;
    List<Class<? extends DriverState>> applicationInstances;

    private OnboardingApplication() {}

    public static OnboardingApplication createNewApplication(Driver driver) {
        OnboardingApplication application = new OnboardingApplication();
        application.id = UUID.randomUUID().toString();
        application.driver = driver;
        application.applicationInstances = new ArrayList<>();
        return application;
    }

    public OnboardingApplication(Driver driver, String status, String failedReason, String prevStatus) {
        id = UUID.randomUUID().toString();
        this.driver = driver;
        this.status = status;
        this.failedReason = failedReason;
        this.driver.setApplication(this);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }

    public List<Class<? extends DriverState>> getApplicationInstances() {
        if(applicationInstances == null)
            applicationInstances = new ArrayList<>();
        return applicationInstances;
    }

    @Override
    public String toString() {
        return "OnboardingApplication{" +
                "id='" + id + '\'' +
                ", applicationInstances=" + applicationInstances +
                '}';
    }
}
