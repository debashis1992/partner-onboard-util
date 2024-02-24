package com.partners.onboard.partneronboardws.service;

import com.partners.onboard.partneronboardws.exception.ServiceApiFailureException;
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

    public boolean sendEmail(String email, Object content) throws ServiceApiFailureException {
        try {
            Thread.sleep(1000);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceApiFailureException("exception occurred: "+e.getMessage());
        }
    }


    public boolean sendSms() {
        return false;
    }
}
