package com.interbank.antifraude.antifraudservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interbank.antifraude.antifraudservice.domain.EventTrxDomain;

@SpringBootApplication
@ComponentScan(basePackages = {"com.interbank.antifraude.antifraudservice.domain", "com.interbank.antifraude.antifraudservice"})
public class AntiFraudServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AntiFraudServiceApplication.class, args);
	}

}
