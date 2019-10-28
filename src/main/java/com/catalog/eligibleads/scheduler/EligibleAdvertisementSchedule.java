package com.catalog.eligibleads.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.catalog.eligibleads.service.EligibleAdvertisementService;

@Component
public class EligibleAdvertisementSchedule {

	private final Logger logger = LoggerFactory.getLogger(EligibleAdvertisementSchedule.class);

	@Autowired
	private EligibleAdvertisementService eligibleAdvertisementService;

	@Scheduled(cron = "*/3600 * * * * *")
	public void selectEligibleAds() {

		logger.info("Eligible Ads search was started!");

		this.eligibleAdvertisementService.findEligibleAds();

		logger.info("Eligible Ads search finished!");

	}

}
