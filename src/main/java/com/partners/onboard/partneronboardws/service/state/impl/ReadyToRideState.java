package com.partners.onboard.partneronboardws.service.state.impl;

import com.partners.onboard.partneronboardws.enums.CompletionStates;
import com.partners.onboard.partneronboardws.enums.DriverProcessStates;
import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.service.state.DriverState;

public class ReadyToRideState implements DriverState {
    @Override
    public void processApplication(Driver driver) throws DriverStateFailureException {

        try {
            System.out.println("marking driver ready to drive");
            driver.getApplication().setStatus(DriverProcessStates.READY_FOR_RIDE+ CompletionStates._STARTED.name());
            driver.getApplication().getApplicationInstances().add(this.getClass());

        } catch(DriverStateFailureException e) {
            System.out.println("failed to mark driver ready to drive");
            driver.getApplication().setFailedReason(e.getMessage());
            driver.getApplication().setStatus(DriverProcessStates.READY_FOR_RIDE+CompletionStates._FAILED.name());
            throw e;
        }
    }
}
