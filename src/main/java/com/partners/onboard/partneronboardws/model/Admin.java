package com.partners.onboard.partneronboardws.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Admin implements ModuleUser {
    String id;

    public Admin() {
        id = UUID.randomUUID().toString();
    }
}
