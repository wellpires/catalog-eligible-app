package com.catalog.eligibleads.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.catalog.eligibleads.builder.AdvertisementDTOWrapperBuilder;
import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.dto.AdvertisementRequestDTO;
import com.catalog.eligibleads.function.AdvertisementDTO2EligibleAdvertisementFunction;
import com.catalog.eligibleads.function.EligibleAdvertisement2AdvertisementDTOFunction;
import com.catalog.eligibleads.redis.model.EligibleAdvertisement;
import com.catalog.eligibleads.redis.repository.EligibleAdsRepository;
import com.catalog.eligibleads.service.AdvertisementService;
import com.catalog.eligibleads.service.EligibleAdvertisementService;
import com.catalog.eligibleads.util.AppUtils;
import com.catalog.eligibleads.wrapper.AdvertisementDTOWrapper;

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
				.map(new AdvertisementDTO2EligibleAdvertisementFunction())
				.filter(AppUtils.distinctByKeys(EligibleAdvertisement::getMlbId, EligibleAdvertisement::getVariationId))
				.collect(Collectors.toList());

		this.eligibleAdvertisementRepository.saveAll(eligibleAds);

	}

	@Override
	public AdvertisementDTOWrapper findAdvertisements(AdvertisementRequestDTO advertisementRequestDTO) {

		PageRequest paging = PageRequest.of(advertisementRequestDTO.getPageNumber(),
				advertisementRequestDTO.getPageLimit());

		Page<EligibleAdvertisement> pageResult = eligibleAdvertisementRepository
				.findByMeliId(advertisementRequestDTO.getMeliId(), paging);

		return new AdvertisementDTOWrapperBuilder()
				.advertisementsDTO(pageResult.getContent().stream()
						.map(new EligibleAdvertisement2AdvertisementDTOFunction()).collect(Collectors.toList()))
				.totalElements(pageResult.getTotalElements()).build();
	}

}
