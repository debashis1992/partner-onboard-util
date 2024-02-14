package com.partners.onboard.partneronboardws.controller;

import com.partners.onboard.partneronboardws.model.Driver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DriverController {

    Map<String, Driver> driverDB;

    public DriverController() {
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
