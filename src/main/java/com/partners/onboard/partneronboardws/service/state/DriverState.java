package com.partners.onboard.partneronboardws.service.state;

import com.partners.onboard.partneronboardws.constant.ExceptionMessages;
import com.partners.onboard.partneronboardws.exception.VerifyLinkExpiredException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.DriverEmailVerificationRequest;
import com.partners.onboard.partneronboardws.model.OnboardingApplication;
import com.partners.onboard.partneronboardws.service.state.impl.SignupApplicationState;
import com.partners.onboard.partneronboardws.service.workflow.OnboardingPipelineFlow;

import java.util.List;
import java.util.Map;

public interface DriverState {

    void processApplication(Driver driver) throws DriverStateFailureException;

    default void updateDriverApplication(Driver driver, Map<String,String> attributes) {}

    default Driver processApplication(DriverEmailVerificationRequest driverEmailVerificationRequest, Driver driver) throws VerifyLinkExpiredException { return null; }

    default void isValidRequest(Driver driver) {

        OnboardingApplication application = driver.getApplication();
        List<Class<? extends DriverState>> completedInstances = application.getCompletedApplicationInstances();

        List<Class<? extends DriverState>> totalInstances = OnboardingPipelineFlow.flows;
        int currentFlowIndex = totalInstances.indexOf(this.getClass());

        if(currentFlowIndex==-1) {
            throw new DriverStateFailureException("currently flow not configured, please check onboarding flows config");
        }

        // check if the last flow is successfully completed, if not throw exception
        if(completedInstances.isEmpty() || !completedInstances.get(completedInstances.size()-1).equals(totalInstances.get(currentFlowIndex-1)))
            throw new DriverStateFailureException(ExceptionMessages.INVALID_REQUEST);
    }
}
