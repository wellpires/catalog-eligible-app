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

	private boolean isFirstTimeExec = true;

	@Scheduled(fixedDelayString = "${schedule.interval.one-hour}")
	public void selectEligibleAds() {

		if (isFirstTimeExec) {
			logger.info("Eligible Ads search was started!");
			long initialTime = System.currentTimeMillis();

			this.eligibleAdvertisementService.findEligibleAds();

			long finalTime = System.currentTimeMillis();
			logger.info("Eligible Ads search finished - {}ms", (finalTime - initialTime));

			System.out.println("APAGAR ESSA PORRA");
			isFirstTimeExec = false;
		}

	}

}
