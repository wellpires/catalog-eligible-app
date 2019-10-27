package com.catalog.eligibleads.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.function.AdvertisementDTO2EligibleAdvertisementFunction;
import com.catalog.eligibleads.function.EligibleAdvertisement2AdvertisementDTOFunction;
import com.catalog.eligibleads.redis.model.EligibleAdvertisement;
import com.catalog.eligibleads.redis.repository.EligibleAdsRepository;
import com.catalog.eligibleads.service.AdvertisementService;
import com.catalog.eligibleads.service.EligibleAdvertisementService;

@Service
public class EligibleAdvertisementServiceImpl implements EligibleAdvertisementService {

	@Autowired
	private AdvertisementService advertisementService;

	@Autowired
	private EligibleAdsRepository eligibleAdvertisementRepository;

	@Override
	public void findEligibleAds() {

		List<AdvertisementDTO> eligibleAdvertisementsDTO = advertisementService.findAdvertisements();

		List<EligibleAdvertisement> eligibleAds = eligibleAdvertisementsDTO.stream()
				.map(new AdvertisementDTO2EligibleAdvertisementFunction()).collect(Collectors.toList());

		this.eligibleAdvertisementRepository.saveAll(eligibleAds);

	}

	@Override
	public List<AdvertisementDTO> findAdvertisements(String meliId, String accessToken) {
		return eligibleAdvertisementRepository.findByMeliId(meliId).stream()
				.map(new EligibleAdvertisement2AdvertisementDTOFunction()).collect(Collectors.toList());
	}

}
