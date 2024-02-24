package com.partners.onboard.partneronboardws.repository;

import com.partners.onboard.partneronboardws.model.OnboardingApplication;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OnboardingApplicationRepository {

    ConcurrentHashMap<String, OnboardingApplication> onboardingApplicationMap;
    public OnboardingApplicationRepository() { onboardingApplicationMap = new ConcurrentHashMap<>(); }

    public OnboardingApplication getApplication(String id) { return onboardingApplicationMap.get(id); }
}
