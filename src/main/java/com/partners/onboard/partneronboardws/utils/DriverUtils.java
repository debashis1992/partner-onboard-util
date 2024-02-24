package com.partners.onboard.partneronboardws.utils;

import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DriverUtils {

    @Autowired
    private DriverRepository driverRepository;


    public Driver getDriverDetails(String id) throws DriverNotFoundException {
        Optional<Driver> driverOptional = driverRepository.getDriver(id);
        if(driverOptional.isPresent()) {
            return driverOptional.get();
        }
        throw new DriverNotFoundException("No driver was found in the system with id: "+id);
    }

    public Driver getDriverDetailsByEmail(String email) throws DriverNotFoundException {
        Optional<Driver> driverOptional = driverRepository.getDriverByEmail(email);
        if(driverOptional.isPresent()) {
            return driverOptional.get();
        }
        throw new DriverNotFoundException("No driver was found in the system with email: "+email);
    }


}
