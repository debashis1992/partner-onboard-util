package com.partners.onboard.partneronboardws.repository;

import com.partners.onboard.partneronboardws.model.OnboardingApplication;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OnboardingApplicationRepository {

    Map<String, OnboardingApplication> onboardingApplicationMap;
    public OnboardingApplicationRepository() { onboardingApplicationMap = new HashMap<>(); }

    public OnboardingApplication getApplication(String id) { return onboardingApplicationMap.get(id); }
}
