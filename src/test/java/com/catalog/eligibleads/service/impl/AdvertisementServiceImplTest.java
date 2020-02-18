package com.catalog.eligibleads.service.impl;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.catalog.eligibleads.builder.AdvertisementItemDTOBuilder;
import com.catalog.eligibleads.builder.AdvertisementVariationDTOBuilder;
import com.catalog.eligibleads.builder.BuyBoxVariationDTOBuilder;
import com.catalog.eligibleads.builder.ElegibleAdvertisementDTOBuilder;
import com.catalog.eligibleads.builder.ItemAttributeDTOBuilder;
import com.catalog.eligibleads.builder.ItemsResponseDTOBuilder;
import com.catalog.eligibleads.builder.MeliDTOBuilder;
import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.dto.AdvertisementVariationDTO;
import com.catalog.eligibleads.dto.BuyBoxVariationDTO;
import com.catalog.eligibleads.dto.ElegibleAdvertisementDTO;
import com.catalog.eligibleads.dto.FilterDTO;
import com.catalog.eligibleads.dto.ItemAttributeDTO;
import com.catalog.eligibleads.dto.ItemsResponseDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.exception.ClientAPIErrorException;
import com.catalog.eligibleads.exception.ExpiredTokenNotFoundException;
import com.catalog.eligibleads.exception.InvalidAccessTokenException;
import com.catalog.eligibleads.exception.MeliNotFoundException;
import com.catalog.eligibleads.service.AdvertisementService;
import com.catalog.eligibleads.service.ItemService;
import com.catalog.eligibleads.service.MeliAccountService;
import com.catalog.eligibleads.service.TokenValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestPropertySource(properties = { "api.mercadolivre.items.catalog-listing-eligibility=http://meli.teste", })
public class AdvertisementServiceImplTest {

	@Autowired
	private RestTemplate client;

	@Autowired
	private ItemService itemService;

	@Autowired
	private AdvertisementService advertisementService;

	@Autowired
	private TokenValidationService tokenValidationService;

	@Autowired
	private ObjectMapper mapper;

	@Test
	public void shouldFindEligibleAds() throws ExpiredTokenNotFoundException, ClientAPIErrorException,
			MeliNotFoundException, RestClientException, JsonProcessingException {

		MeliDTO meliBuilt = new MeliDTOBuilder().accessToken("accessToken").nameAccount("accountName").build();

		when(itemService.findItems(any(FilterDTO.class), any(MeliDTO.class)))
				.thenAnswer(new Answer<ItemsResponseDTO>() {
					int count = 0;

					@Override
					public ItemsResponseDTO answer(InvocationOnMock invocation) throws Throwable {
						if (count++ == 5) {
							return new ItemsResponseDTOBuilder().build();
						}
						return new ItemsResponseDTOBuilder()
								.items(Arrays.asList("MLB1", "MLB2", "MLB3", "MLB4", "MLB5")).build();
					}
				});

		List<BuyBoxVariationDTO> buyBoxVariations = new BuyBoxVariationDTOBuilder().itemsAmount(10).buildList();

		ElegibleAdvertisementDTO eligibleAdsBuilt = new ElegibleAdvertisementDTOBuilder().buyBoxElegible(true).id("id")
				.buyBoxVariations(buyBoxVariations).build();
		when(client.getForEntity(any(URI.class), any()))
				.thenReturn(ResponseEntity.ok(mapper.writeValueAsString(eligibleAdsBuilt)));

		Set<ItemAttributeDTO> itemsAttributes = new ItemAttributeDTOBuilder().itemsAmount(10).valueName("valueNameTest")
				.buildSet();
		List<AdvertisementVariationDTO> adsVariation = new AdvertisementVariationDTOBuilder().itemsAmount(10)
				.attributes(itemsAttributes).buildList();
		when(itemService.searchProductsByEligibleAds(anyList(), any(MeliDTO.class)))
				.thenReturn(new AdvertisementItemDTOBuilder().id("id").variations(adsVariation).itemsAmount(10)
						.permalink("http://testets.com").buildList());

		List<AdvertisementDTO> eligibleAds = advertisementService.findEligibleAds(meliBuilt);

		assertThat("Should find eligible advertisements", eligibleAds, hasSize(100));

	}

	@Test
	public void shouldNotReturnEligibleBecauseTokenIsInvalid() throws InvalidAccessTokenException,
			ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		when(itemService.findItems(any(FilterDTO.class), any(MeliDTO.class)))
				.thenThrow(InvalidAccessTokenException.class);

		MeliDTO meliBuilt = new MeliDTOBuilder().accessToken("accessToken").nameAccount("accountName").build();
		List<AdvertisementDTO> eligibleAds = advertisementService.findEligibleAds(meliBuilt);

		assertThat("Should not return eligible advertisements because token is invalid", eligibleAds, empty());

	}

	@Test
	public void shouldFindEligibleAdsUntillReachMaxNumberOfAdvertisements() throws ExpiredTokenNotFoundException,
			ClientAPIErrorException, MeliNotFoundException, RestClientException, JsonProcessingException {

		MeliDTO meliBuilt = new MeliDTOBuilder().accessToken("accessToken").nameAccount("accountName").build();

		when(itemService.findItems(any(FilterDTO.class), any(MeliDTO.class))).thenReturn(
				new ItemsResponseDTOBuilder().items(Arrays.asList("MLB1", "MLB2", "MLB3", "MLB4", "MLB5")).build());

		ElegibleAdvertisementDTO eligibleAdsBuilt = new ElegibleAdvertisementDTOBuilder().buyBoxElegible(true).id("id")
				.buyBoxVariations(new ArrayList<>()).build();
		when(client.getForEntity(any(URI.class), any()))
				.thenReturn(ResponseEntity.ok(mapper.writeValueAsString(eligibleAdsBuilt)));

		when(itemService.searchProductsByEligibleAds(anyList(), any(MeliDTO.class)))
				.thenReturn(new AdvertisementItemDTOBuilder().id("id").itemsAmount(10).buildList());

		List<AdvertisementDTO> eligibleAds = advertisementService.findEligibleAds(meliBuilt);

		assertThat("Should find eligible advertisements untill reach the max numbers of advertisement", eligibleAds,
				hasSize(10));

	}

	@Test
	public void shouldFindEligibleAdsEvenIfTokenIsExpired()
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		MeliDTO meliBuilt = new MeliDTOBuilder().accessToken("accessToken").nameAccount("accountName").build();

		when(itemService.findItems(any(FilterDTO.class), any(MeliDTO.class))).thenReturn(
				new ItemsResponseDTOBuilder().items(Arrays.asList("MLB1", "MLB2", "MLB3", "MLB4", "MLB5")).build());

		when(client.getForEntity(any(URI.class), any())).thenAnswer(new Answer<ResponseEntity<Object>>() {
			int count = 0;

			@Override
			public ResponseEntity<Object> answer(InvocationOnMock invocation) throws Throwable {
				ElegibleAdvertisementDTO eligibleAdsBuilt = new ElegibleAdvertisementDTOBuilder().buyBoxElegible(true)
						.id("id").buyBoxVariations(new ArrayList<>()).build();
				ResponseEntity<Object> responseEntity = ResponseEntity.ok(mapper.writeValueAsString(eligibleAdsBuilt));
				if (count++ == 0) {
					responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
				}
				return responseEntity;
			}

		});

		when(itemService.searchProductsByEligibleAds(anyList(), any(MeliDTO.class)))
				.thenReturn(new AdvertisementItemDTOBuilder().id("id").itemsAmount(10).buildList());

		List<AdvertisementDTO> eligibleAds = advertisementService.findEligibleAds(meliBuilt);

		assertThat("Should find eligible ads even if Token is expired", eligibleAds, hasSize(10));

	}

	@Test
	public void shouldNotFindEligibleAdsBecauseRefreshTokenServiceThrownAnException()
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		MeliDTO meliBuilt = new MeliDTOBuilder().accessToken("accessToken").nameAccount("accountName").build();

		when(itemService.findItems(any(FilterDTO.class), any(MeliDTO.class))).thenReturn(
				new ItemsResponseDTOBuilder().items(Arrays.asList("MLB1", "MLB2", "MLB3", "MLB4", "MLB5")).build());

		when(client.getForEntity(any(URI.class), any()))
				.thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN).build());

		when(tokenValidationService.refreshToken(any(MeliDTO.class))).thenThrow(ClientAPIErrorException.class);

		when(itemService.searchProductsByEligibleAds(anyList(), any(MeliDTO.class)))
				.thenReturn(new AdvertisementItemDTOBuilder().id("id").itemsAmount(10).buildList());

		List<AdvertisementDTO> eligibleAds = advertisementService.findEligibleAds(meliBuilt);

		assertThat("Should not find eligible ads because refresh token service was not available", eligibleAds,
				empty());

	}

	@Test
	public void shouldNotFindEligibleAdsBecauseNoAdvertisementsWereFound()
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		MeliDTO meliBuilt = new MeliDTOBuilder().accessToken("accessToken").nameAccount("accountName").build();

		when(itemService.findItems(any(FilterDTO.class), any(MeliDTO.class)))
				.thenReturn(new ItemsResponseDTOBuilder().build());

		when(client.getForEntity(any(URI.class), any()))
				.thenReturn(ResponseEntity.ok(new ElegibleAdvertisementDTOBuilder().buyBoxElegible(true).id("id")
						.buyBoxVariations(new ArrayList<>()).build()));

		when(itemService.searchProductsByEligibleAds(anyList(), any(MeliDTO.class)))
				.thenReturn(new AdvertisementItemDTOBuilder().id("id").itemsAmount(10).buildList());

		List<AdvertisementDTO> eligibleAds = advertisementService.findEligibleAds(meliBuilt);

		assertThat("Should not find eligible ads because no ads were found", eligibleAds, empty());

	}

	@Test
	public void shouldNotFindEligibleAdsBecauseNoEligibleAdvertisementsWereFound()
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		MeliDTO meliBuilt = new MeliDTOBuilder().accessToken("accessToken").nameAccount("accountName").build();

		when(itemService.findItems(any(FilterDTO.class), any(MeliDTO.class))).thenReturn(
				new ItemsResponseDTOBuilder().items(Arrays.asList("MLB1", "MLB2", "MLB3", "MLB4", "MLB5")).build());

		when(client.getForEntity(any(URI.class), any()))
				.thenReturn(ResponseEntity.ok(new ElegibleAdvertisementDTOBuilder().buyBoxElegible(false).id("id")
						.buyBoxVariations(new ArrayList<>()).build()));

		when(itemService.searchProductsByEligibleAds(anyList(), any(MeliDTO.class)))
				.thenReturn(new AdvertisementItemDTOBuilder().id("id").itemsAmount(10).buildList());

		List<AdvertisementDTO> eligibleAds = advertisementService.findEligibleAds(meliBuilt);

		assertThat("Should not find eligible ads because no eligible ads were found", eligibleAds, empty());

	}

	@Test
	public void shouldNotFindEligibleAdsBecauseAnExceptionWasThrown()
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {

		MeliDTO meliBuilt = new MeliDTOBuilder().accessToken("accessToken").nameAccount("accountName").build();

		doThrow(ClientAPIErrorException.class).when(itemService).findItems(any(FilterDTO.class), any(MeliDTO.class));

		when(client.getForEntity(any(URI.class), any()))
				.thenReturn(ResponseEntity.ok(new ElegibleAdvertisementDTOBuilder().buyBoxElegible(false).id("id")
						.buyBoxVariations(new ArrayList<>()).build()));

		when(itemService.searchProductsByEligibleAds(anyList(), any(MeliDTO.class)))
				.thenReturn(new AdvertisementItemDTOBuilder().id("id").itemsAmount(10).buildList());

		List<AdvertisementDTO> eligibleAds = advertisementService.findEligibleAds(meliBuilt);

		assertThat("Should not find eligible ads because an Exception was thrown", eligibleAds, empty());

	}

	@Configuration
	static class Config {

		@Bean
		public RestTemplate client() {
			return mock(RestTemplate.class);
		}

		@Bean
		public ItemService itemService() {
			return mock(ItemService.class);
		}

		@Bean
		public MeliAccountService meliAccountService() {
			return mock(MeliAccountService.class);
		}

		@Bean
		public TokenValidationService tokenValidationService() {
			return mock(TokenValidationService.class);
		}

		@Bean
		public AdvertisementService advertisementService() {
			return new AdvertisementServiceImpl();
		}

		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapper();
		}

	}

}
