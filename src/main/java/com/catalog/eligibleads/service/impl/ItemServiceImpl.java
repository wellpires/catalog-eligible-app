package com.catalog.eligibleads.service.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

@Service
public class ItemServiceImpl implements ItemService {

	@Value("${api.mercadolivre.users.items-search}")
	private String urlSearchItems;

	@Value("${api.mercadolivre.items}")
	private String urlSearchProduct;

	@Autowired
	private RestTemplate client;

	@Autowired
	private TokenValidationService tokenValidationService;

	@Override
	public ItemsResponseDTO findItems(FilterDTO filterDTO, MeliDTO meli)
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		ItemsResponseDTO tryFindItems;
		try {
			tryFindItems = tryFindItems(filterDTO, meli);
		} catch (InvalidAccessTokenException iat) {
			meli = tokenValidationService.refreshToken(meli);
			tryFindItems = tryFindItems(filterDTO, meli);
		}

		return tryFindItems;
	}

	private ItemsResponseDTO tryFindItems(FilterDTO filterDTO, MeliDTO meli) throws InvalidAccessTokenException {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("meli_id", meli.getId());
		URI buscarCliente = UriComponentsBuilder.fromHttpUrl(urlSearchItems).queryParams(filterDTO.getParameters())
				.queryParam("access_token", "APP_USR-189141373421891-102620-ec4093d5f2008ccaa5f98fad3d810873-111412004")
				.buildAndExpand(variables).toUri();

		ResponseEntity<ItemsResponseDTO> response = client.getForEntity(buscarCliente, ItemsResponseDTO.class);

		if (HttpStatus.FORBIDDEN.equals(response.getStatusCode())
				&& HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
			throw new InvalidAccessTokenException();
		}
		return response.getBody();
	}

	@Override
	public List<ItemResponseDTO> searchProduct(MeliDTO meli, String[] ids)
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		List<ItemResponseDTO> itemsResponseDTO;
		try {
			itemsResponseDTO = trySearchProduct(meli, ids);
		} catch (InvalidAccessTokenException iat) {
			meli = tokenValidationService.refreshToken(meli);
			itemsResponseDTO = trySearchProduct(meli, ids);
		}

		return itemsResponseDTO;

	}

	private List<ItemResponseDTO> trySearchProduct(MeliDTO meli, String[] ids) {
		MultiValueMap<String, String> queryParameter = new LinkedMultiValueMap<>();
		queryParameter.add("ids", Stream.of(ids).collect(Collectors.joining(",")));
		queryParameter.add("access_token", meli.getAccessToken());
		String uri = UriComponentsBuilder.fromHttpUrl(urlSearchProduct).queryParams(queryParameter).toUriString();

		ResponseEntity<List<ItemResponseDTO>> response = client.exchange(uri, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<ItemResponseDTO>>() {
				});

		if (HttpStatus.FORBIDDEN.equals(response.getStatusCode())
				&& HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
			throw new InvalidAccessTokenException();
		}

		return response.getBody();
	}

}
