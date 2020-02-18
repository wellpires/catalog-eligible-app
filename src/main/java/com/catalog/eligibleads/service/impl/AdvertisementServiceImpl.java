package com.catalog.eligibleads.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
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
import com.catalog.eligibleads.service.AdvertisementService;
import com.catalog.eligibleads.service.ItemService;
import com.catalog.eligibleads.service.MeliAccountService;
import com.catalog.eligibleads.service.TokenValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

	private static final int ITEMS_LIMIT_OFFSET = 1000;

	private static final int ITEMS_OFFSET = 50;

	private static final Logger logger = LoggerFactory.getLogger(AdvertisementServiceImpl.class);

	@Autowired
	private RestTemplate client;

	@Autowired
	private ItemService itemService;

	@Autowired
	private TokenValidationService tokenValidationService;

	@Autowired
	private MeliAccountService meliAccountService;

	@Value("${api.mercadolivre.items.catalog-listing-eligibility}")
	private String urlEligibleAds;

	@Autowired
	private ObjectMapper mapper;

	@Override
	public List<AdvertisementDTO> findEligibleAds(MeliDTO meli) {

		List<AdvertisementDTO> advertisementsDTOs = Collections.emptyList();
		try {
			FilterDTO filterDTO = new FilterDTOBuilder().accessToken(meli.getAccessToken()).limit(50l)
					.order(Order.START_TIME_ASC).status(AdvertisementStatus.ACTIVE).build();

			List<String> items = new ArrayList<>();
			for (int offset = 0; offset <= ITEMS_LIMIT_OFFSET; offset += ITEMS_OFFSET) {
				filterDTO.setOffset(offset);

				ItemsResponseDTO itemsResponse = itemService.findItems(filterDTO, meli);
				if (CollectionUtils.isEmpty(itemsResponse.getItems())) {
					break;
				}
				items.addAll(itemsResponse.getItems());
			}

			if (isContaHasNoAds(items)) {
				logger.warn("{} account has no advertisements", meli.getNameAccount());
				meliAccountService.saveMeliAccount(meli);
				return Collections.emptyList();
			}

			List<ElegibleAdvertisementDTO> eligiblesAds = findEligibleAdvertisement(items, meli);

			if (isContaHasNoEligibleAds(eligiblesAds)) {
				logger.warn("{} account has no eligible advertisements", meli.getNameAccount());
				meliAccountService.saveMeliAccount(meli);
				return Collections.emptyList();
			}

			List<AdvertisementItemDTO> advertisementBuyBoxes = itemService.searchProductsByEligibleAds(eligiblesAds,
					meli);

			return createAdvertisements(eligiblesAds, advertisementBuyBoxes, meli);

		} catch (MeliNotFoundException | ExpiredTokenNotFoundException | ClientAPIErrorException ex) {
			logger.error(ex.getMessage());
		} catch (InvalidAccessTokenException ex) {
			logger.warn(ex.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return advertisementsDTOs;
	}

	private boolean isContaHasNoEligibleAds(List<ElegibleAdvertisementDTO> eligiblesAds) {
		return CollectionUtils.isEmpty(eligiblesAds);
	}

	private boolean isContaHasNoAds(List<String> items) {
		return CollectionUtils.isEmpty(items);
	}

	private List<AdvertisementDTO> createAdvertisements(List<ElegibleAdvertisementDTO> eligiblesAds,
			List<AdvertisementItemDTO> advertisementBuyBoxes, MeliDTO meli) {
		List<AdvertisementDTO> advertisements = advertisementBuyBoxes.stream()
				.map(new AdvertisementItemDTO2ListAdvertisementDTOFunction()).flatMap(Collection::stream).map(item -> {
					item.setMeliId(meli.getId());
					item.setAccountName(meli.getNameAccount());
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

		return Objects.isNull(advertisement.getVariationId()) || variations.stream()
				.anyMatch(variation -> variation.getId().equals(String.valueOf(advertisement.getVariationId())));
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
			} catch (ExpiredTokenNotFoundException | ClientAPIErrorException | MeliNotFoundException
					| InvalidAccessTokenException e) {
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

		ResponseEntity<String> response = client.getForEntity(findEligibleAd, String.class);
		if (HttpStatus.FORBIDDEN.equals(response.getStatusCode())
				|| HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
			throw new InvalidAccessTokenException(meli.getNameAccount());
		}

		ElegibleAdvertisementDTO elegibleAdvertisementDTO = new ElegibleAdvertisementDTO();
		if (response.getStatusCode().is2xxSuccessful()) {
			try {
				elegibleAdvertisementDTO = mapper.readValue(response.getBody(), ElegibleAdvertisementDTO.class);
			} catch (JsonProcessingException e) {
				logger.error("Error parsing JSON - Account {}", meli.getNameAccount());
				logger.error(e.getMessage(), e);
			}
		}

		return elegibleAdvertisementDTO;
	}

	private Map<String, String> setParameter(String itemId) {
		return Collections.singletonMap("itemId", itemId);
	}

}
