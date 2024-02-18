package com.partners.onboard.partneronboardws.service;

import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.repository.DriverRepository;
import com.partners.onboard.partneronboardws.service.verification.VerificationStrategy;
import com.partners.onboard.partneronboardws.service.verification.impl.VerificationRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverDocumentService {
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VerificationRules verificationRules;


    public List<String> getRequiredDocuments(String email) throws DriverNotFoundException {

        var driverOptional = driverRepository.getDriverByEmail(email);
        if(driverOptional.isPresent()) {

            int cityPin = driverOptional.get().getCityPin();
            var verificationStrategies = verificationRules.getRules(cityPin);

            return verificationStrategies.stream().map(VerificationStrategy::requiredDocument)
                    .collect(Collectors.toList());

        }

        throw new DriverNotFoundException("no driver found with email: "+email);
    }
}
