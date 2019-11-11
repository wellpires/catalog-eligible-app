package com.catalog.eligibleads.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.catalog.eligibleads.builder.FilterDTOBuilder;
import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.dto.AdvertisementItemDTO;
import com.catalog.eligibleads.dto.BuyBoxVariationDTO;
import com.catalog.eligibleads.dto.ElegibleAdvertisementDTO;
import com.catalog.eligibleads.dto.FilterDTO;
import com.catalog.eligibleads.dto.ItemsResponseDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.enums.AdvertisementStatus;
import com.catalog.eligibleads.enums.Order;
import com.catalog.eligibleads.exception.ClientAPIErrorException;
import com.catalog.eligibleads.exception.ExpiredTokenNotFoundException;
import com.catalog.eligibleads.exception.InvalidAccessTokenException;
import com.catalog.eligibleads.exception.MeliNotFoundException;
import com.catalog.eligibleads.function.AdvertisementItemDTO2ListAdvertisementDTOFunction;
import com.catalog.eligibleads.redis.repository.EligibleAdsRepository;
import com.catalog.eligibleads.service.AdvertisementService;
import com.catalog.eligibleads.service.ItemService;
import com.catalog.eligibleads.service.TokenValidationService;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

	private static final Logger logger = LoggerFactory.getLogger(AdvertisementServiceImpl.class);

	@Autowired
	private RestTemplate client;

	@Autowired
	private ItemService itemService;

	@Autowired
	private EligibleAdsRepository eligibleAdvertisementRepository;

	@Autowired
	private TokenValidationService tokenValidationService;

	@Value("${api.mercadolivre.items.catalog-listing-eligibility}")
	private String urlEligibleAds;

	@Override
	public List<AdvertisementDTO> findEligibleAds(MeliDTO meli) {

		List<AdvertisementDTO> advertisementsDTOs = Collections.emptyList();
		try {
			logger.info(" >>>>>>>>>>>> Finding eligible advertisements for {} account <<<<<<<<<<<<",
					meli.getNameAccount());

			FilterDTO filterDTO = new FilterDTOBuilder().accessToken(meli.getAccessToken()).limit(50l)
					.order(Order.START_TIME_ASC).status(AdvertisementStatus.ACTIVE).build();

			logger.info("{} account - Finding advertisements", meli.getNameAccount());
			List<String> items = new ArrayList<>();
			for (int offset = 0; offset < 1000; offset += 50) {
				filterDTO.setOffset(offset);
				ItemsResponseDTO itemsResponse;
				itemsResponse = itemService.findItems(filterDTO, meli);

				if (CollectionUtils.isEmpty(itemsResponse.getItems())) {
					break;
				}
				items.addAll(itemsResponse.getItems());
			}

			if (isContaHasNoAds(items)) {
				logger.warn("{} account has no advertisements", meli.getNameAccount());
				return Collections.emptyList();
			}

			logger.info("{} account - Finding eligible advertisements", meli.getNameAccount());
			List<ElegibleAdvertisementDTO> eligiblesAds = findEligibleAdvertisement(items, meli);

			if (isContaHasNoEligibleAds(eligiblesAds)) {
				logger.warn("{} account has no eligible advertisements", meli.getNameAccount());
				return Collections.emptyList();
			}

			logger.info("{} account - Finding eligible advertisements", meli.getNameAccount());
			List<AdvertisementItemDTO> advertisementBuyBoxes = itemService.searchProductsByEligibleAds(eligiblesAds,
					meli);

			advertisementsDTOs = createAdvertisements(eligiblesAds, advertisementBuyBoxes, meli);

			logger.info("{} account - Removing eligible advertisements that already exists in database",
					meli.getNameAccount());
			return removeExistingAdvertisements(advertisementsDTOs);

		} catch (ExpiredTokenNotFoundException | ClientAPIErrorException | MeliNotFoundException e) {
			logger.error(e.getMessage(), e);
		}

		logger.info(" ======================================================================== ");

		return advertisementsDTOs;
	}

	private boolean isContaHasNoEligibleAds(List<ElegibleAdvertisementDTO> eligiblesAds) {
		return CollectionUtils.isEmpty(eligiblesAds);
	}

	private boolean isContaHasNoAds(List<String> items) {
		return CollectionUtils.isEmpty(items);
	}

	private List<AdvertisementDTO> removeExistingAdvertisements(List<AdvertisementDTO> advertisementsDTOs) {
		return advertisementsDTOs.stream().filter(ad -> advertisementsDTOs.stream().noneMatch(this::isRegisterExists))
				.collect(Collectors.toList());
	}

	private boolean isRegisterExists(AdvertisementDTO adDTO) {
		return eligibleAdvertisementRepository
				.findByVariationIdAndMeliIdAndMlbId(adDTO.getVariationId(), adDTO.getMeliId(), adDTO.getId())
				.isPresent();
	}

	private List<AdvertisementDTO> createAdvertisements(List<ElegibleAdvertisementDTO> eligiblesAds,
			List<AdvertisementItemDTO> advertisementBuyBoxes, MeliDTO meli) {
		List<AdvertisementDTO> advertisements = advertisementBuyBoxes.stream()
				.map(new AdvertisementItemDTO2ListAdvertisementDTOFunction()).flatMap(Collection::stream).map(item -> {
					item.setMeliId(meli.getId());
					return item;
				}).collect(Collectors.toList());

		advertisements = filterByEligibleAdId(eligiblesAds, advertisements);

		List<BuyBoxVariationDTO> variations = eligiblesAds.stream().map(ElegibleAdvertisementDTO::getBuyBoxVariations)
				.flatMap(Collection::stream).collect(Collectors.toList());

		return advertisements.stream()
				.filter(advertisement -> isVariationIDIsZeroOrEqualBuyBoxVariationID(advertisement, variations))
				.collect(Collectors.toList());
	}

	private boolean isVariationIDIsZeroOrEqualBuyBoxVariationID(AdvertisementDTO advertisement,
			List<BuyBoxVariationDTO> variations) {

		return NumberUtils.LONG_ZERO.equals(advertisement.getVariationId()) || variations.stream()
				.anyMatch(variation -> variation.getId().equals(Optional.ofNullable(advertisement.getVariationId())
						.map(Objects::toString).orElseGet(() -> NumberUtils.LONG_ZERO.toString())));
	}

	private List<AdvertisementDTO> filterByEligibleAdId(List<ElegibleAdvertisementDTO> elegibleAds,
			List<AdvertisementDTO> advertisements) {
		return advertisements.stream().filter(advertisement -> elegibleAds.stream().map(ElegibleAdvertisementDTO::getId)
				.anyMatch(eligibleAd -> advertisement.getId().equals(eligibleAd))).collect(Collectors.toList());
	}

	private List<ElegibleAdvertisementDTO> findEligibleAdvertisement(List<String> items, MeliDTO meli) {
		return items.parallelStream().map(this::setParameter).map(item -> {
			try {
				return configClient(item, meli);
			} catch (ExpiredTokenNotFoundException | ClientAPIErrorException | MeliNotFoundException e) {
				throw new RuntimeException(e);
			}
		}).map(ElegibleAdvertisementDTO::selectElegibles).flatMap(Collection::stream).collect(Collectors.toList());
	}

	private ElegibleAdvertisementDTO configClient(Map<String, String> parameter, MeliDTO meli)
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		try {
			return tryConfigClient(parameter, meli);
		} catch (InvalidAccessTokenException iate) {
			tokenValidationService.refreshToken(meli);
			return tryConfigClient(parameter, meli);
		}

	}

	private ElegibleAdvertisementDTO tryConfigClient(Map<String, String> parameter, MeliDTO meli) {
		URI findEligibleAd = UriComponentsBuilder.fromHttpUrl(urlEligibleAds)
				.queryParam("access_token", meli.getAccessToken()).buildAndExpand(parameter).toUri();

		ResponseEntity<ElegibleAdvertisementDTO> response = client.getForEntity(findEligibleAd,
				ElegibleAdvertisementDTO.class);
		if (HttpStatus.FORBIDDEN.equals(response.getStatusCode())
				&& HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
			throw new InvalidAccessTokenException();
		}

		return response.getBody();
	}

	private Map<String, String> setParameter(String itemId) {
		return Collections.singletonMap("itemId", itemId);
	}

}
