package com.catalog.eligibleads.service;

import java.util.List;

import com.catalog.eligibleads.dto.AdvertisementDTO;

public interface AdvertisementService {

	List<AdvertisementDTO> findAdvertisements();

}
