package com.partners.onboard.partneronboardws;

import com.partners.onboard.partneronboardws.repository.DriverRepository;
import com.partners.onboard.partneronboardws.exception.DriverStateFailureException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.state.impl.*;
import com.partners.onboard.partneronboardws.service.verification.impl.VerificationRules;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.Map;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class PartnerOnboardWsApplication {

//	public static DriverRepository driverRepository = new DriverRepository();

//	public static VerificationRules verificationRules = new VerificationRules();

	@Autowired
	private VerificationRules verificationRules;

	@Autowired
	private DriverRepository driverRepository;

	public static void main(String[] args) {
		SpringApplication.run(PartnerOnboardWsApplication.class, args);



	}

	@PostConstruct
	public void testSomeActions() {
		// driver adding profile info from collection form
		Map<String, String> attributes = Map.of(
				"firstName","debashis",
				"lastName", "das",
				"phone", "342342342",
				"email", "d2011das@gmail.com"
		);

		Driver driver = signUp();

		addProfileInfo(driver, attributes);

		// driver documents collection
		startDocumentsCollection(driver);

		// driver background verification
		performDocumentsVerificationProcess(driver);

		// driver shipping of tracking device
		shipTrackingDeviceToDriver(driver);

		// driver ready to ride
		startReadyToRideState(driver);

//		System.out.println("application instances: "+driver.getApplication().getApplicationInstances());
	}


	public Driver signUp() {
		// driver signup
		// creating a new default driver
		// creating a new application for the driver onboarding process

		Driver driver = new Driver();
		driverRepository.addDriver(driver);
		DriverState state = driver.getDriverState();
		state.processApplication(driver);
		return driver;
	}

	public void addProfileInfo(Driver driver, Map<String, String> attributes) throws DriverStateFailureException {
		DriverState state = driver.setAndGetDriverState(new AddProfileInfoState());
		state.processApplication(driver);
		state.updateDriverApplication(driver, attributes);
	}

	public Driver startDocumentsCollection(Driver driver) throws DriverStateFailureException {
		DriverState state = driver.setAndGetDriverState(new DocumentsCollectionState());
		state.processApplication(driver);
		return driver;
	}

	public Driver performDocumentsVerificationProcess(Driver driver) throws DriverStateFailureException {
		DriverState state = driver.setAndGetDriverState(new BackgroundVerificationState(verificationRules));
		state.processApplication(driver);
		return driver;
	}

	public Driver shipTrackingDeviceToDriver(Driver driver) throws DriverStateFailureException {
		DriverState state = driver.setAndGetDriverState(new ShipTrackingDeviceState());
		state.processApplication(driver);
		return driver;
	}

	public Driver startReadyToRideState(Driver driver) throws DriverStateFailureException {
		DriverState state = driver.setAndGetDriverState(new ReadyToRideState());
		state.processApplication(driver);
		return driver;
	}

}
