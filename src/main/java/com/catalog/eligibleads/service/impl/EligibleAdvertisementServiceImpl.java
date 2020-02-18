package com.catalog.eligibleads.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import com.catalog.eligibleads.service.MeliAccountService;
import com.catalog.eligibleads.service.MeliService;
import com.catalog.eligibleads.util.AppUtils;
import com.catalog.eligibleads.wrapper.AdvertisementDTOWrapper;

@Service
public class EligibleAdvertisementServiceImpl implements EligibleAdvertisementService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AdvertisementService advertisementService;

	@Autowired
	private EligibleAdsRepository eligibleAdvertisementRepository;

	@Autowired
	private MeliService meliService;

	@Autowired
	private MeliAccountService meliAccountService;

	@Override
	public void findEligibleAds() {

		meliService.findAllActivatedMeli().parallelStream().filter(meliAccountService::meliAccountProbablyHasNoAds)
				.map(advertisementService::findEligibleAds).filter(CollectionUtils::isNotEmpty)
				.map(this::convertAdvertisementDTO2EligibleAdvertisement).forEach(this::saveAllEligibleAdvertisements);

	}

	private void saveAllEligibleAdvertisements(List<EligibleAdvertisement> eligibleAdvertisement) {
		logger.info("Saving the total of {} eligible ads to {} account", eligibleAdvertisement.size(),
				eligibleAdvertisement.stream().findFirst().get().getAccountName());

		eligibleAdvertisement.stream().map(EligibleAdvertisement::getMeliId)
				.map(eligibleAdvertisementRepository::findByMeliId).forEach(eligibleAdvertisementRepository::deleteAll);
		this.eligibleAdvertisementRepository.saveAll(eligibleAdvertisement);
	}

	private List<EligibleAdvertisement> convertAdvertisementDTO2EligibleAdvertisement(
			List<AdvertisementDTO> eligibleAdvertisementsDTO) {
		return eligibleAdvertisementsDTO.stream().map(new AdvertisementDTO2EligibleAdvertisementFunction())
				.filter(AppUtils.distinctByKeys(EligibleAdvertisement::getMlbId, EligibleAdvertisement::getVariationId))
				.collect(Collectors.toList());
	}

	@Override
	public AdvertisementDTOWrapper findAdvertisements(AdvertisementRequestDTO advertisementRequestDTO) {

		PageRequest paging = PageRequest.of(advertisementRequestDTO.getPageNumber(),
				advertisementRequestDTO.getPageLimit(), Sort.by(Direction.ASC, "meliId"));

		Page<EligibleAdvertisement> pageResult = eligibleAdvertisementRepository
				.findByMeliId(advertisementRequestDTO.getMeliId(), paging);

		return new AdvertisementDTOWrapperBuilder()
				.advertisementsDTO(pageResult.getContent().stream()
						.map(new EligibleAdvertisement2AdvertisementDTOFunction()).collect(Collectors.toList()))
				.totalElements(pageResult.getTotalElements()).build();
	}

}
