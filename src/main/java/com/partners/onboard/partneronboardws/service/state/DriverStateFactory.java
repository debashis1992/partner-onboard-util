package com.partners.onboard.partneronboardws.service.state;

import com.partners.onboard.partneronboardws.enums.DriverProcessStates;
import com.partners.onboard.partneronboardws.service.state.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverStateFactory {

    @Autowired
    private SignupApplicationState signupApplicationState;
    @Autowired
    private AddProfileInfoState addProfileInfoState;
    @Autowired
    private DocumentsCollectionState documentsCollectionState;
    @Autowired
    private BackgroundVerificationState backgroundVerificationState;
    @Autowired
    private ShipTrackingDeviceState shipTrackingDeviceState;
    @Autowired
    private ReadyToRideState readyToRideState;

    public DriverState getDriverState(String state) {

        return null;
    }

}
