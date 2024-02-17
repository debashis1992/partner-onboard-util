package com.partners.onboard.partneronboardws.model;

import com.partners.onboard.partneronboardws.model.documents.DriverDocuments;
import com.partners.onboard.partneronboardws.model.documents.VehicleRegistration;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.state.impl.SignupApplicationState;

import java.util.UUID;

public class Driver implements ModuleClient {

    String id;
    String firstName, lastName, email;
    int phoneNumber;
    int cityPin;

    DriverState driverState;
    OnboardingApplication application;

    DriverDocuments driverDocuments;
    VehicleRegistration vehicleRegistration;

    public String getId() {
        return id;
    }

    public Driver() {
        id = UUID.randomUUID().toString();
        driverState = new SignupApplicationState();
        // create a new application for the driver
        application = OnboardingApplication.createNewApplication(this);
        this.driverDocuments = new DriverDocuments();
        this.vehicleRegistration = new VehicleRegistration();
        this.cityPin = (int)(Math.random() * 100);

    }

    public String getEmail() {
        return email;
    }

    public DriverState getDriverState() {
        return driverState;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setApplication(OnboardingApplication application) {
        this.application = application;
    }

    public int getCityPin() {
        return cityPin;
    }

    public void setCityPin(int cityPin) {
        this.cityPin = cityPin;
    }

    public OnboardingApplication getApplication() {
        return application;
    }

    public DriverState setAndGetDriverState(DriverState driverState) {
        this.driverState = driverState;
        this.application.applicationInstances.add(driverState.getClass());
        return this.driverState;
    }

    public Driver(String firstName, String lastName, String email, int phoneNumber, int cityPin) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.driverState = new SignupApplicationState();

        this.driverDocuments = new DriverDocuments();
        this.vehicleRegistration = new VehicleRegistration();
        this.cityPin = cityPin;
    }


    public DriverDocuments getDriverDocuments() {
        return driverDocuments;
    }

    public VehicleRegistration getVehicleRegistration() {
        return vehicleRegistration;
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
