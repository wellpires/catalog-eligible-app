package com.catalog.eligibleads.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.catalog.eligibleads.service.EligibleAdvertisementService;

@Component
public class EligibleAdvertisementSchedule {

	private static final ZoneId ZONE_ID = ZoneId.of("GMT-3");

	private final Logger logger = LoggerFactory.getLogger(EligibleAdvertisementSchedule.class);

	@Autowired
	private EligibleAdvertisementService eligibleAdvertisementService;

	@Scheduled(fixedDelayString = "${schedule.interval.one-hour}")
	public void selectEligibleAds() {
		DateTimeFormatter dateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime initialTime = LocalDateTime.now(ZONE_ID);
		logger.info("===========================================================================");
		logger.info("Eligible Ads search was started at {}!", initialTime.format(dateTimePattern));
		logger.info("===========================================================================");

		this.eligibleAdvertisementService.findEligibleAds();

		LocalDateTime finalTime = LocalDateTime.now(ZONE_ID);
		logger.info("===========================================================================");
		logger.info("Eligible Ads search finished at {} - Total of {} minutes", finalTime.format(dateTimePattern),
				initialTime.until(finalTime, ChronoUnit.MINUTES));
		logger.info("===========================================================================");

	}

}
