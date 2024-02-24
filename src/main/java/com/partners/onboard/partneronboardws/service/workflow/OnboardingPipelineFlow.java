package com.partners.onboard.partneronboardws.service.workflow;

import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.OnboardingApplication;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.state.impl.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnboardingPipelineFlow {

    public static final List<Class<? extends DriverState>> flows = List.of(
             SignupApplicationState.class,
                VerifyProfileState.class,
                AddProfileInfoState.class,
                DocumentsCollectionState.class,
                BackgroundVerificationState.class,
                ShipTrackingDeviceState.class,
                ReadyToRideState.class
    );

    public List<Class<? extends DriverState>> getFlows() {
        return flows;
    }

    public void triggerRetry(Driver driver) {

        OnboardingApplication application = driver.getApplication();
        int i=application.getCompletedApplicationInstances().size()-1;

        if(i== flows.size()) {
            System.out.println("all stages are completed. Nothing to retry for driverId: "+driver.getId());
        } else {
            // retrigger pending states one by one
        }
    }
}
