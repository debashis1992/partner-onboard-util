package com.partners.onboard.partneronboardws.service.verification.impl;

import com.partners.onboard.partneronboardws.service.verification.VerificationStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerificationRules {

    private Map<Integer, List<VerificationStrategy>> verificationRulesController;

    public VerificationRules() {
        verificationRulesController = new HashMap<>();
        initializeSomeData();
    }

    private void initializeSomeData() {
        verificationRulesController.put(0, List.of(new AadharVerificationStrategy()));
        verificationRulesController.put(2, List.of(new AadharVerificationStrategy(), new VehicleInsuranceVerificationStrategy()));
        verificationRulesController.put(3, List.of(new PanVerificationStrategy(), new AadharVerificationStrategy()));
        verificationRulesController.put(4, List.of(new AadharVerificationStrategy(), new VehicleRTOVerification()));
        verificationRulesController.put(5, List.of(new PanVerificationStrategy(), new VehicleRTOVerification(), new VehicleInsuranceVerificationStrategy()));
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
