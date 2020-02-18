package com.catalog.eligibleads.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.catalog.eligibleads.dto.AdvertisementItemDTO;
import com.catalog.eligibleads.dto.ElegibleAdvertisementDTO;
import com.catalog.eligibleads.dto.FilterDTO;
import com.catalog.eligibleads.dto.ItemResponseDTO;
import com.catalog.eligibleads.dto.ItemsResponseDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.exception.ClientAPIErrorException;
import com.catalog.eligibleads.exception.ExpiredTokenNotFoundException;
import com.catalog.eligibleads.exception.InvalidAccessTokenException;
import com.catalog.eligibleads.exception.MeliNotFoundException;
import com.catalog.eligibleads.service.ItemService;
import com.catalog.eligibleads.service.TokenValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ItemServiceImpl implements ItemService {

	private static final String DELIMITER_COMMA = ",";

	private static final Integer LIMIT_MULTI_GET = 20;

	@Value("${api.mercadolivre.users.items-search}")
	private String urlSearchItems;

	@Value("${api.mercadolivre.items}")
	private String urlSearchProduct;

	@Autowired
	private RestTemplate client;

	@Autowired
	private TokenValidationService tokenValidationService;

	@Autowired
	private ObjectMapper mapper;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ItemsResponseDTO findItems(FilterDTO filterDTO, MeliDTO meli) throws ExpiredTokenNotFoundException,
			ClientAPIErrorException, MeliNotFoundException, InvalidAccessTokenException {

		ItemsResponseDTO tryFindItems;
		try {
			tryFindItems = tryFindItems(filterDTO, meli);
		} catch (InvalidAccessTokenException iat) {
			meli = tokenValidationService.refreshToken(meli);
			tryFindItems = tryFindItems(filterDTO, meli);
		}

		return tryFindItems;
	}

	@Override
	public List<AdvertisementItemDTO> searchProductsByEligibleAds(List<ElegibleAdvertisementDTO> eligibleAds,
			MeliDTO meli) throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		int maxSize = Double
				.valueOf(Math.ceil(Integer.valueOf(eligibleAds.size()).doubleValue() / LIMIT_MULTI_GET.doubleValue()))
				.intValue();
		return IntStream.iterate(0, i -> i += LIMIT_MULTI_GET).limit(maxSize).parallel()
				.mapToObj((index) -> eligibleAds.stream().skip(index).limit(20).map(ElegibleAdvertisementDTO::getId)
						.collect(Collectors.joining(DELIMITER_COMMA)))
				.map(ids -> {
					List<AdvertisementItemDTO> advertisementItemDTO = new ArrayList<>();
					try {
						advertisementItemDTO = searchProduct(meli, ids).stream().filter(ItemResponseDTO::isSuccess)
								.map(ItemResponseDTO::getAdvertisementItemDTO).collect(Collectors.toList());
					} catch (ExpiredTokenNotFoundException | ClientAPIErrorException | MeliNotFoundException e) {
						logger.error("Error when try refresh token", e);
						advertisementItemDTO = Collections.emptyList();
					}
					return advertisementItemDTO;
				}).filter(CollectionUtils::isNotEmpty).flatMap(Collection::stream).collect(Collectors.toList());

	}

	private ItemsResponseDTO tryFindItems(FilterDTO filterDTO, MeliDTO meli) throws InvalidAccessTokenException {

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("meli_id", meli.getId());
		URI uri = UriComponentsBuilder.fromHttpUrl(urlSearchItems).queryParams(filterDTO.getParameters())
				.queryParam("access_token", meli.getAccessToken()).buildAndExpand(variables).toUri();

		ResponseEntity<String> response = client.getForEntity(uri, String.class);

		if (HttpStatus.FORBIDDEN.equals(response.getStatusCode())
				|| HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
			throw new InvalidAccessTokenException(meli.getNameAccount());
		}

		ItemsResponseDTO itemsResponseDTO = new ItemsResponseDTO();
		if (response.getStatusCode().is2xxSuccessful()) {
			try {
				itemsResponseDTO = mapper.readValue(response.getBody(), ItemsResponseDTO.class);
			} catch (JsonProcessingException e) {
				logger.error("Error parsing JSON - Account {}", meli.getNameAccount());
				logger.error(e.getMessage(), e);
			}
		}
		return itemsResponseDTO;
	}

	private List<ItemResponseDTO> searchProduct(MeliDTO meli, String ids) throws ExpiredTokenNotFoundException,
			ClientAPIErrorException, MeliNotFoundException, InvalidAccessTokenException {

		List<ItemResponseDTO> itemsResponseDTO;
		try {
			itemsResponseDTO = trySearchProduct(meli, ids);
		} catch (InvalidAccessTokenException iat) {
			meli = tokenValidationService.refreshToken(meli);
			itemsResponseDTO = trySearchProduct(meli, ids);
		}

		return itemsResponseDTO;

	}

	private List<ItemResponseDTO> trySearchProduct(MeliDTO meli, String ids) {
		MultiValueMap<String, String> queryParameter = new LinkedMultiValueMap<>();
		queryParameter.add("ids", ids);
		queryParameter.add("access_token", meli.getAccessToken());
		String uri = UriComponentsBuilder.fromHttpUrl(urlSearchProduct).queryParams(queryParameter).toUriString();

		ResponseEntity<String> response = client.exchange(uri, HttpMethod.GET, null, String.class);

		if (HttpStatus.FORBIDDEN.equals(response.getStatusCode())
				|| HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
			throw new InvalidAccessTokenException(meli.getNameAccount());
		}

		List<ItemResponseDTO> itemResponseDTO = new ArrayList<>();
		if (response.getStatusCode().is2xxSuccessful()) {
			try {
				itemResponseDTO = mapper.readValue(response.getBody(), new TypeReference<List<ItemResponseDTO>>() {
				});
			} catch (JsonProcessingException e) {
				logger.error("Error parsing JSON - Account {}", meli.getNameAccount());
				logger.error(e.getMessage(), e);
			}

		}

		return itemResponseDTO;
	}

}
