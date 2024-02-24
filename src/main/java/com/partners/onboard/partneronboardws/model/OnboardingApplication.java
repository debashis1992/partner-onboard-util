package com.partners.onboard.partneronboardws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OnboardingApplication {
    String id;
    @JsonIgnore
    Driver driver;
    String status;
    String failedReason;
    List<Class<? extends DriverState>> completedApplicationInstances;

    private OnboardingApplication() {}

    public static OnboardingApplication createNewApplication(Driver driver) {
        OnboardingApplication application = new OnboardingApplication();
        application.id = UUID.randomUUID().toString();
        application.driver = driver;
        application.completedApplicationInstances = new ArrayList<>();
        return application;
    }

    public OnboardingApplication(Driver driver, String status, String failedReason, String prevStatus) {
        id = UUID.randomUUID().toString();
        this.driver = driver;
        this.status = status;
        this.failedReason = failedReason;
        this.completedApplicationInstances = new ArrayList<>();
        this.driver.setApplication(this);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }

    public List<Class<? extends DriverState>> getCompletedApplicationInstances() {
        if(completedApplicationInstances == null)
            completedApplicationInstances = new ArrayList<>();
        return completedApplicationInstances;
    }

    public void addCompletedApplicationInstances(Class<? extends DriverState> completedApplicationInstance) {
        completedApplicationInstances.add(completedApplicationInstance);
    }



    @Override
    public String toString() {
        return "OnboardingApplication{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", failedReason='" + failedReason + '\'' +
                ", applicationInstances=" + completedApplicationInstances +
                '}';
    }
}
