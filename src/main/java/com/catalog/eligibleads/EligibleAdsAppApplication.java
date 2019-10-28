package com.catalog.eligibleads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EligibleAdsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EligibleAdsAppApplication.class, args);
	}

}
