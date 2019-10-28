package com.catalog.eligibleads.service;

import com.catalog.eligibleads.dto.AdvertisementRequestDTO;
import com.catalog.eligibleads.wrapper.AdvertisementDTOWrapper;

public interface EligibleAdvertisementService {

	void findEligibleAds();

	AdvertisementDTOWrapper findAdvertisements(AdvertisementRequestDTO advertisementRequestDTO);

}
