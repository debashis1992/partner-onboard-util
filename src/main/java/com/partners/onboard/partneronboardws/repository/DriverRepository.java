package com.partners.onboard.partneronboardws.repository;

import com.partners.onboard.partneronboardws.model.Driver;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class DriverRepository {

    Map<String, Driver> driverDB;

    public DriverRepository() {
        driverDB = new HashMap<>();
    }

    public void addDriver(Driver driver) {
        driverDB.put(driver.getId(), driver);
    }

    public void removeDriver(Driver driver) {
        driverDB.remove(driver.getId());
    }

    public void updateDriver(Driver driver) {
        driverDB.put(driver.getId(), driver);
    }

    public boolean searchDriver(Driver driver) {
        return driverDB.containsKey(driver.getId());
    }

    public Optional<Driver> getDriver(String id) {
        return Optional.ofNullable(driverDB.get(id));
    }
}
