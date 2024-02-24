package com.partners.onboard.partneronboardws.service;

import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.records.DriverInfoResponse;
import com.partners.onboard.partneronboardws.service.state.impl.AddProfileInfoState;
import com.partners.onboard.partneronboardws.service.state.impl.DocumentsCollectionState;
import com.partners.onboard.partneronboardws.utils.DriverUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DriverInfoService {

    @Autowired
    private DriverUtils driverUtils;

    @Autowired
    private AddProfileInfoState addProfileInfoState;

    @Autowired
    private DocumentsCollectionState documentsCollectionState;

    public void addProfileInfo(String id, Map<String, String> attributesMap) throws DriverNotFoundException {

        Driver driver = driverUtils.getDriverDetails(id);

        addProfileInfoState.processApplication(driver);
        addProfileInfoState.updateDriverApplication(driver, attributesMap);

        //after profile info is added, we'll make the current state as DocumentCollectionState
        driver.setAndGetDriverState(documentsCollectionState);
    }

    public void updateProfileInfo(String id, Map<String, String> attributesMap) throws DriverNotFoundException {
        Driver driver = driverUtils.getDriverDetails(id);

        addProfileInfoState.updateDriverApplication(driver, attributesMap);
    }

    public DriverInfoResponse getDriverInfo(String id) throws DriverNotFoundException {

        Driver driver = driverUtils.getDriverDetails(id);
        return new DriverInfoResponse(driver.getFirstName(), driver.getLastName(),
                driver.getEmail(), driver.getPhoneNumber(), driver.getCityPin());

    }
}
