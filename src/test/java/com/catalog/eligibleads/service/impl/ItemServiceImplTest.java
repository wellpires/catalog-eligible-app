package com.catalog.eligibleads.service.impl;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.catalog.eligibleads.builder.ElegibleAdvertisementDTOBuilder;
import com.catalog.eligibleads.builder.ItemResponseDTOBuilder;
import com.catalog.eligibleads.builder.MeliDTOBuilder;
import com.catalog.eligibleads.dto.AdvertisementItemDTO;
import com.catalog.eligibleads.dto.ElegibleAdvertisementDTO;
import com.catalog.eligibleads.dto.FilterDTO;
import com.catalog.eligibleads.dto.ItemResponseDTO;
import com.catalog.eligibleads.dto.ItemsResponseDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.exception.ClientAPIErrorException;
import com.catalog.eligibleads.exception.ExpiredTokenNotFoundException;
import com.catalog.eligibleads.exception.MeliNotFoundException;
import com.catalog.eligibleads.service.ItemService;
import com.catalog.eligibleads.service.TokenValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestPropertySource(properties = { "api.mercadolivre.users.items-search=http://meli-teste/items-search",
		"api.mercadolivre.items=http://meli-teste/items" })
public class ItemServiceImplTest {

	@Value("${api.mercadolivre.users.items-search}")
	private String urlSearchItems;

	@Value("${api.mercadolivre.items}")
	private String urlSearchProduct;

	@Autowired
	private RestTemplate client;

	@Autowired
	private TokenValidationService tokenValidationService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private ObjectMapper mapper;

	@Test
	public void shouldFindItems() throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException,
			RestClientException, JsonProcessingException {

		ItemsResponseDTO itemsResponseDTO = new ItemsResponseDTO();
		when(client.getForEntity(any(URI.class), any()))
				.thenReturn(ResponseEntity.ok(mapper.writeValueAsString(itemsResponseDTO)));

		ItemsResponseDTO itemResponseDTO = itemService.findItems(new FilterDTO(), new MeliDTO());

		assertNotNull("Should return item response DTO", itemResponseDTO);

	}

	@Test
	public void shouldNotFindItemsBecauseWasNotReturnedSuccessStatus() throws ExpiredTokenNotFoundException,
			ClientAPIErrorException, MeliNotFoundException, RestClientException, JsonProcessingException {

		when(client.getForEntity(any(URI.class), any())).thenReturn(ResponseEntity.badRequest().build());

		ItemsResponseDTO itemResponseDTO = itemService.findItems(new FilterDTO(), new MeliDTO());

		assertNull("Should not find return items because was returned not success status", itemResponseDTO.getPaging());

	}

	@Test
	public void shouldFindItemsTryingForTheSecondTime()
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		when(client.getForEntity(any(URI.class), any())).thenAnswer(new Answer<ResponseEntity<String>>() {
			int count = 0;

			@Override
			public ResponseEntity<String> answer(InvocationOnMock invocation) throws Throwable {
				ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				ItemsResponseDTO itemsResponseDTO = new ItemsResponseDTO();
				if (count++ == 1) {
					responseEntity = ResponseEntity.ok(mapper.writeValueAsString(itemsResponseDTO));
				}
				return responseEntity;
			}

		});
		when(tokenValidationService.refreshToken(any(MeliDTO.class))).thenReturn(new MeliDTOBuilder().build());

		ItemsResponseDTO itemResponseDTO = itemService.findItems(new FilterDTO(), new MeliDTO());

		assertNotNull("Should return item response DTO trying for the second time", itemResponseDTO);

	}

	@Test
	public void shouldFindItemsTryingForTheSecondTimeBecauseWasReturnedForbidden()
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		when(client.getForEntity(any(URI.class), any())).thenAnswer(new Answer<ResponseEntity<String>>() {
			int count = 0;

			@Override
			public ResponseEntity<String> answer(InvocationOnMock invocation) throws Throwable {
				ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
				ItemsResponseDTO itemsResponseDTO = new ItemsResponseDTO();
				if (count++ == 1) {
					responseEntity = ResponseEntity.ok(mapper.writeValueAsString(itemsResponseDTO));
				}
				return responseEntity;
			}

		});
		when(tokenValidationService.refreshToken(any(MeliDTO.class))).thenReturn(new MeliDTOBuilder().build());

		ItemsResponseDTO itemResponseDTO = itemService.findItems(new FilterDTO(), new MeliDTO());

		assertNotNull(
				"Should return item response DTO trying for the second time because was returned Forbidden status",
				itemResponseDTO);

	}

	@Test
	public void shouldSearchProductsByEligibleAds() throws ExpiredTokenNotFoundException, ClientAPIErrorException,
			MeliNotFoundException, RestClientException, JsonProcessingException {

		List<ElegibleAdvertisementDTO> eligibleAds = new ElegibleAdvertisementDTOBuilder().itemsAmount(80).buildList();
		MeliDTO meliDTO = new MeliDTOBuilder().accessToken("acess-token-teste").build();

		List<ItemResponseDTO> itemResponseDTOBuilt = new ItemResponseDTOBuilder().itemsAmount(80).code(HttpStatus.OK)
				.buildList();
		when(client.exchange(anyString(), any(HttpMethod.class), nullable(HttpEntity.class),
				Mockito.<Class<String>>any()))
						.thenReturn(ResponseEntity.ok(mapper.writeValueAsString(itemResponseDTOBuilt)));

		List<AdvertisementItemDTO> adsFound = itemService.searchProductsByEligibleAds(eligibleAds, meliDTO);

		assertThat("Should return products by eligible ads Id", adsFound, hasSize(320));

	}

	@Test
	public void shouldNotSearchProductsByEligibleAdsBecauseWasNotReturnedSuccessStatus()
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException, RestClientException,
			JsonProcessingException {

		List<ElegibleAdvertisementDTO> eligibleAds = new ElegibleAdvertisementDTOBuilder().itemsAmount(80).buildList();
		MeliDTO meliDTO = new MeliDTOBuilder().accessToken("acess-token-teste").build();

		when(client.exchange(anyString(), any(HttpMethod.class), nullable(HttpEntity.class),
				Mockito.<Class<String>>any())).thenReturn(ResponseEntity.badRequest().build());

		List<AdvertisementItemDTO> adsFound = itemService.searchProductsByEligibleAds(eligibleAds, meliDTO);

		assertThat("Should return products by eligible ads Id", adsFound, empty());

	}

	@Test
	public void shouldSearchProductByEligibleAdsEvenIfTokenIsExpired()
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		List<ElegibleAdvertisementDTO> eligibleAds = new ElegibleAdvertisementDTOBuilder().itemsAmount(80).buildList();
		MeliDTO meliDTO = new MeliDTOBuilder().accessToken("acess-token-teste").build();

		when(client.exchange(anyString(), any(HttpMethod.class), nullable(HttpEntity.class),
				Mockito.<Class<String>>any())).thenAnswer(new Answer<ResponseEntity<String>>() {
					int count = 0;

					@Override
					public ResponseEntity<String> answer(InvocationOnMock invocation) throws Throwable {

						if (count++ > 0) {
							List<ItemResponseDTO> itemResponseBuilt = new ItemResponseDTOBuilder().itemsAmount(80)
									.code(HttpStatus.OK).buildList();
							return ResponseEntity.ok(mapper.writeValueAsString(itemResponseBuilt));
						}

						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
					}

				});

		String newAccessToken = "new-access-token-teste";
		when(tokenValidationService.refreshToken(any(MeliDTO.class)))
				.thenReturn(new MeliDTOBuilder().accessToken(newAccessToken).modify(meliDTO));

		List<AdvertisementItemDTO> adsFound = itemService.searchProductsByEligibleAds(eligibleAds, meliDTO);

		assertThat("Should return products by eligible ads Id even if Token is Expired", adsFound, hasSize(320));
		assertThat("Should return the new Token", meliDTO.getAccessToken(), equalTo(newAccessToken));

	}

	@Test
	public void shouldNotFindProductsBecauseTokenServiceIsNotAvailable()
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		List<ElegibleAdvertisementDTO> eligibleAds = new ElegibleAdvertisementDTOBuilder().itemsAmount(80).buildList();
		MeliDTO meliDTO = new MeliDTOBuilder().accessToken("acess-token-teste").build();

		when(client.exchange(anyString(), any(HttpMethod.class), nullable(HttpEntity.class),
				Mockito.<Class<String>>any())).thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN).build());

		when(tokenValidationService.refreshToken(any(MeliDTO.class))).thenThrow(ClientAPIErrorException.class);
		List<AdvertisementItemDTO> adsFound = itemService.searchProductsByEligibleAds(eligibleAds, meliDTO);

		assertThat("Should not find products because token service is not available", adsFound, empty());

	}

	@Configuration
	public static class Config {

		@Bean
		public RestTemplate client() {
			return mock(RestTemplate.class);
		}

		@Bean
		public TokenValidationService tokenValidationService() {
			return mock(TokenValidationService.class);
		}

		@Bean
		public ItemService itemService() {
			return new ItemServiceImpl();
		}

		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapper();
		}

	}

}
