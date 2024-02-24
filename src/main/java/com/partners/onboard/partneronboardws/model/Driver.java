package com.partners.onboard.partneronboardws.model;

import com.partners.onboard.partneronboardws.model.documents.Document;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.state.impl.SignupApplicationState;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Driver implements ModuleClient {

    String id;
    String firstName, lastName, email;
    int phoneNumber;
    int cityPin;

    DriverState driverState;
    OnboardingApplication application;

    List<Document> documents;

    public Driver() {
        id = UUID.randomUUID().toString();
        driverState = new SignupApplicationState();
        // create a new application for the driver
        application = OnboardingApplication.createNewApplication(this);
        this.documents = new ArrayList<>();
        this.cityPin = (int)(Math.random() * 100);

    }

    public DriverState setAndGetDriverState(DriverState driverState) {
        this.driverState = driverState;
        return this.driverState;
    }

    public Driver(String firstName, String lastName, String email, int phoneNumber, int cityPin) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.driverState = new SignupApplicationState();

        this.documents = new ArrayList<>();
        this.cityPin = cityPin;
    }


    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", driverState=" + driverState +
                ", application=" + application +
                '}';
    }
}
