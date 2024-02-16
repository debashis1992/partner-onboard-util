package com.partners.onboard.partneronboardws.service.verification.impl;

import com.partners.onboard.partneronboardws.service.verification.VerificationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VerificationRules {

    private Map<Integer, List<VerificationStrategy>> verificationRulesController;
    private AadharVerificationStrategy aadharVerificationStrategy;
    private PanVerificationStrategy panVerificationStrategy;
    private VehicleRTOVerificationStrategy vehicleRTOVerificationStrategy;
    private VehicleInsuranceVerificationStrategy vehicleInsuranceVerificationStrategy;

    public VerificationRules(@Autowired AadharVerificationStrategy aadharVerificationStrategy, @Autowired PanVerificationStrategy panVerificationStrategy,
                             @Autowired VehicleRTOVerificationStrategy vehicleRTOVerificationStrategy,
                             @Autowired VehicleInsuranceVerificationStrategy vehicleInsuranceVerificationStrategy) {
        verificationRulesController = new HashMap<>();
        this.aadharVerificationStrategy=aadharVerificationStrategy;
        this.panVerificationStrategy=panVerificationStrategy;
        this.vehicleRTOVerificationStrategy=vehicleRTOVerificationStrategy;
        this.vehicleInsuranceVerificationStrategy=vehicleInsuranceVerificationStrategy;
        initializeSomeData();
    }

    private void initializeSomeData() {
        verificationRulesController.put(0, List.of(aadharVerificationStrategy));
        verificationRulesController.put(1, List.of(aadharVerificationStrategy));
        verificationRulesController.put(2, List.of(aadharVerificationStrategy, vehicleInsuranceVerificationStrategy));
        verificationRulesController.put(3, List.of(panVerificationStrategy, aadharVerificationStrategy));
        verificationRulesController.put(4, List.of(aadharVerificationStrategy, vehicleRTOVerificationStrategy));
        verificationRulesController.put(5, List.of(panVerificationStrategy, vehicleRTOVerificationStrategy, vehicleInsuranceVerificationStrategy));
    }

    public void addRule(int cityPin, VerificationStrategy verificationStrategy) {
        if(verificationStrategy!=null) {
            List<VerificationStrategy> v = verificationRulesController.getOrDefault(cityPin, new ArrayList<>());
            v.add(verificationStrategy);
        }
    }

    public void removeRule(int cityPin, VerificationStrategy rule) {

        if(verificationRulesController.containsKey(cityPin) && rule!=null) {
            verificationRulesController.remove(cityPin, rule);
        }
    }

    public void createNewRule(int cityPin, List<VerificationStrategy> verificationStrategies) {
        List<VerificationStrategy> v = verificationRulesController.getOrDefault(cityPin, new ArrayList<>());
        v.addAll(verificationStrategies);
        verificationRulesController.put(cityPin, v);
    }

    public List<VerificationStrategy> getRules(int cityPin) {
        int key = cityPin%8;
        return verificationRulesController.getOrDefault(key, new ArrayList<>());
    }
}
