package com.partners.onboard.partneronboardws.service;

import com.partners.onboard.partneronboardws.model.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public boolean sendVerificationLink(String email) throws InterruptedException {
        try {
            Thread.sleep(1000);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean sendEmail() {
        return true;
    }

    public boolean sendSms() {
        return false;
    }
}
