package com.catalog.eligibleads.service;

import java.util.List;

import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.dto.MeliDTO;

public interface AdvertisementService {

	List<AdvertisementDTO> findEligibleAds(MeliDTO meli);

}
