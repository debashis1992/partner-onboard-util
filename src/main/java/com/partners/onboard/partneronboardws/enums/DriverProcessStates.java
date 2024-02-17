package com.partners.onboard.partneronboardws.enums;

import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
public enum DriverProcessStates {
    SIGN_UP,
    PROFILE_INFO,
    ONBOARDING_PROCESS,
    READY_FOR_RIDE
}
