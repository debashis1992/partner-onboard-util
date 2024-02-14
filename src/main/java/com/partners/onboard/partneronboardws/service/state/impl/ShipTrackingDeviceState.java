package com.partners.onboard.partneronboardws.service.state.impl;

import com.partners.onboard.partneronboardws.enums.CompletionStates;
import com.partners.onboard.partneronboardws.enums.DriverOnboardingProcessStates;
import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.service.state.DriverState;

public class ShipTrackingDeviceState implements DriverState {
    @Override
    public void processApplication(Driver driver) throws DriverStateFailureException {

        try {

            driver.getApplication().setStatus(DriverOnboardingProcessStates.SHIPPING_TRACKING_DEVICE.name()+ CompletionStates._STARTED);
            performShipTrackingDevice();

            driver.getApplication().setStatus(DriverOnboardingProcessStates.SHIPPING_TRACKING_DEVICE.name()+CompletionStates._COMPLETED);
            driver.getApplication().getApplicationInstances().add(this.getClass());
        } catch (DriverStateFailureException e) {
            System.out.println("Failed to ship tracking device with error: "+e.getMessage());
            driver.getApplication().setStatus(DriverOnboardingProcessStates.SHIPPING_TRACKING_DEVICE.name()+CompletionStates._FAILED);
            throw e;
        }
    }

    public void performShipTrackingDevice() {
        System.out.println("shipping tracking device to driver");
    }
}
