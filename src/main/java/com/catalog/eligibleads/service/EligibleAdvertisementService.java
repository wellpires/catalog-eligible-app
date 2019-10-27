package com.catalog.eligibleads.service;

import java.util.List;

import com.catalog.eligibleads.dto.AdvertisementDTO;

public interface EligibleAdvertisementService {

	void findEligibleAds();

	List<AdvertisementDTO> findAdvertisements(String meliId, String accessToken);

}
