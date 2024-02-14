package com.partners.onboard.partneronboardws.service.state;

import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.model.Driver;

import java.util.Map;

public interface DriverState {

    void processApplication(Driver driver) throws DriverStateFailureException;

    default void updateDriverApplication(Driver driver, Map<String,String> attributes) {}
}
