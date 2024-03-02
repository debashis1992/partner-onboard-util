package com.partners.onboard.partneronboardws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class PartnerOnboardWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartnerOnboardWsApplication.class, args);
	}

}
