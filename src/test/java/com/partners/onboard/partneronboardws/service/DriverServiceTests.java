package com.partners.onboard.partneronboardws.service;

import com.partners.onboard.partneronboardws.enums.CompletionStates;
import com.partners.onboard.partneronboardws.enums.DriverProcessStates;
import com.partners.onboard.partneronboardws.exception.DriverAlreadyExistsException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.repository.DriverRepository;
import com.partners.onboard.partneronboardws.utils.DriverUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTests {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverUtils driverUtils;

    @InjectMocks
    private DriverService driverService;

    @Test
    public void testDriverSignUpWithDuplicateEmail() {

        String email = "user1@gmail.com";
        Mockito.when(driverRepository.getDriverByEmail(email))
                .thenReturn(Optional.of(new Driver()));

        Assertions.assertThrows(DriverAlreadyExistsException.class,
                () -> driverService.signUp(email));
    }

    @Test
    public void testDriverSignUpWithValidEmail() {
        String email = "user2@gmail.com";
        Mockito.when(driverRepository.getDriverByEmail(email))
                .thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() -> driverService.signUp(email));
        Mockito.verify(driverRepository
        , Mockito.times(1))
                .addDriver(Mockito.any(Driver.class));


        Driver driver = driverService.signUp(email);
        Assertions.assertEquals(email, driver.getEmail());
        Assertions.assertEquals(DriverProcessStates.SIGN_UP+
                CompletionStates._COMPLETED.name(), driver.getApplication().getStatus());
    }
}
