package com.catalog.eligibleads.service.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.catalog.eligibleads.dto.FilterDTO;
import com.catalog.eligibleads.dto.ItemResponseDTO;
import com.catalog.eligibleads.dto.ItemsResponseDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	private static final String URL_SEARCH_ITEMS = "https://api.mercadolibre.com/users/{meli_id}/items/search";
	private static final String URL_SEARCH_PRODUCT = "https://api.mercadolibre.com/items";

	@Override
	public ItemsResponseDTO findItems(FilterDTO filterDTO, MeliDTO meli) {

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("meli_id", meli.getId());
		URI buscarCliente = UriComponentsBuilder.fromHttpUrl(URL_SEARCH_ITEMS).queryParams(filterDTO.getParameters())
				.queryParam("access_token", meli.getAccessToken()).buildAndExpand(variables).toUri();

		RestTemplate rest = new RestTemplate();
		return rest.getForEntity(buscarCliente, ItemsResponseDTO.class).getBody();
	}

	@Override
	public List<ItemResponseDTO> searchProduct(MeliDTO meli, String[] ids) {

		MultiValueMap<String, String> queryParameter = new LinkedMultiValueMap<>();
		queryParameter.add("ids", Stream.of(ids).collect(Collectors.joining(",")));
		queryParameter.add("access_token", meli.getAccessToken());
		String uri = UriComponentsBuilder.fromHttpUrl(URL_SEARCH_PRODUCT).queryParams(queryParameter).toUriString();

		RestTemplate rest = new RestTemplate();
		return rest.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<ItemResponseDTO>>() {
		}).getBody();
	}

}
