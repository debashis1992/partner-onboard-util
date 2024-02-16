package com.partners.onboard.partneronboardws.repository;

import com.partners.onboard.partneronboardws.exception.DriverAlreadyExistsException;
import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.model.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DriverRepository {

    ConcurrentHashMap<String, String> driverEntryMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Driver> driverDB = new ConcurrentHashMap<>();

//    @Autowired
//    public DriverRepository() {
//        driverEntryMap = new ConcurrentHashMap<>();
//        driverDB = new ConcurrentHashMap<>();
//    }

    public void addDriver(Driver driver) {
        if(driverEntryMap.containsKey(driver.getEmail()))
            throw new DriverAlreadyExistsException("driver with email "+driver.getEmail()+" already exists!!!");

        driverEntryMap.put(driver.getEmail(), driver.getId());
        driverDB.put(driver.getId(), driver);
    }

    public void removeDriver(Driver driver) {
        driverEntryMap.remove(driver.getEmail());
        driverDB.remove(driver.getId());
    }

    public void updateDriver(Driver driver) {
        //email update
        // d2011, obj
        // xxx, obj
        if(driverDB.containsKey(driver.getId())) {
            Driver oldDetails = driverDB.get(driver.getId());
            String oldEmail = oldDetails.getEmail();

            //remove old email
            driverEntryMap.remove(oldEmail);
        }
        driverEntryMap.put(driver.getEmail(), driver.getId());
        driverDB.put(driver.getId(), driver);
    }

    public boolean searchDriver(Driver driver) {
        return driverDB.containsKey(driver.getId());
    }

    public Optional<Driver> getDriver(String id) {
        return Optional.ofNullable(driverDB.get(id));
    }

    public Optional<Driver> getDriverByEmail(String email) {
        if(driverEntryMap.containsKey(email)) {
            String id = driverEntryMap.get(email);
            return Optional.of(driverDB.get(id));
        }

        return Optional.empty();
    }
}
