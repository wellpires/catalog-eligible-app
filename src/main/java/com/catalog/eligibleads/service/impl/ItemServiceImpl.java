package com.catalog.eligibleads.service.impl;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

@Service
public class ItemServiceImpl implements ItemService {

	private static final int LIMIT_MULTI_GET = 20;

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

	@Override
	public List<AdvertisementItemDTO> searchProductsByEligibleAds(List<ElegibleAdvertisementDTO> eligibleAds,
			MeliDTO meli) throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		return IntStream.iterate(0, i -> i += LIMIT_MULTI_GET).limit(eligibleAds.size() / LIMIT_MULTI_GET).parallel()
				.mapToObj((index) -> eligibleAds.stream().skip(index).limit(20).map(ElegibleAdvertisementDTO::getId)
						.collect(Collectors.joining(", ")))
				.map(ids -> {
					try {
						return searchProduct(meli, ids).stream().filter(ItemResponseDTO::isSuccess)
								.map(ItemResponseDTO::getAdvertisementItemDTO).collect(Collectors.toList());
					} catch (ExpiredTokenNotFoundException | ClientAPIErrorException | MeliNotFoundException e) {
						throw new RuntimeException(e);
					}
				}).flatMap(Collection::stream).collect(Collectors.toList());

	}

	private ItemsResponseDTO tryFindItems(FilterDTO filterDTO, MeliDTO meli) throws InvalidAccessTokenException {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("meli_id", meli.getId());
		URI buscarCliente = UriComponentsBuilder.fromHttpUrl(urlSearchItems).queryParams(filterDTO.getParameters())
				.queryParam("access_token", meli.getAccessToken()).buildAndExpand(variables).toUri();

		ResponseEntity<ItemsResponseDTO> response = client.getForEntity(buscarCliente, ItemsResponseDTO.class);

		if (HttpStatus.FORBIDDEN.equals(response.getStatusCode())
				|| HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
			throw new InvalidAccessTokenException();
		}
		return response.getBody();
	}

	private List<ItemResponseDTO> searchProduct(MeliDTO meli, String ids)
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

	private List<ItemResponseDTO> trySearchProduct(MeliDTO meli, String ids) {
		MultiValueMap<String, String> queryParameter = new LinkedMultiValueMap<>();
		queryParameter.add("ids", ids);
		queryParameter.add("access_token", meli.getAccessToken());
		String uri = UriComponentsBuilder.fromHttpUrl(urlSearchProduct).queryParams(queryParameter).toUriString();

		ResponseEntity<List<ItemResponseDTO>> response = client.exchange(uri, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<ItemResponseDTO>>() {
				});

		if (HttpStatus.FORBIDDEN.equals(response.getStatusCode())
				|| HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
			throw new InvalidAccessTokenException();
		}

		return response.getBody();
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		List<String> values = IntStream.range(0, 100).mapToObj(Objects::toString).collect(Collectors.toList());

		System.out.println("Normal Way");
		long normalWayInitialTime = System.currentTimeMillis();
		IntStream.iterate(0, i -> i += 20).limit(values.size() / 20).mapToObj(index -> {
			return values.stream().skip(index).limit(20).collect(Collectors.joining(", "));
		}).forEach(ItemServiceImpl::waitAndPrint);
		long normalWayTime = System.currentTimeMillis() - normalWayInitialTime;

		System.out.println("Parallel Way");
		long parallelWayInitialTime = System.currentTimeMillis();
		IntStream.iterate(0, i -> i += 20).limit(values.size() / 20).parallel().mapToObj(index -> {
			return values.stream().skip(index).limit(20).collect(Collectors.joining(", "));
		}).forEach(ItemServiceImpl::waitAndPrint);
		long parallelWayTime = System.currentTimeMillis() - parallelWayInitialTime;

		System.out.println("Total time");
		System.out.println("Normal way: " + normalWayTime);
		System.out.println("Parallel way: " + parallelWayTime);

	}

	private static void waitAndPrint(String value) {

		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(value);

	}

}
