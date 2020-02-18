package com.catalog.eligibleads.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.catalog.eligibleads.builder.AdvertisementDTOBuilder;
import com.catalog.eligibleads.builder.AdvertisementRequestDTOBuilder;
import com.catalog.eligibleads.builder.EligibleAdvertisementBuilder;
import com.catalog.eligibleads.builder.MeliDTOBuilder;
import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.dto.AdvertisementRequestDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.redis.model.EligibleAdvertisement;
import com.catalog.eligibleads.redis.repository.EligibleAdsRepository;
import com.catalog.eligibleads.service.AdvertisementService;
import com.catalog.eligibleads.service.EligibleAdvertisementService;
import com.catalog.eligibleads.service.MeliAccountService;
import com.catalog.eligibleads.service.MeliService;
import com.catalog.eligibleads.wrapper.AdvertisementDTOWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class EligibleAdvertisementServiceImplTest {

	@Autowired
	private EligibleAdvertisementService eligibleAdvertisementService;

	@Autowired
	private AdvertisementService advertisementService;

	@Autowired
	private EligibleAdsRepository eligibleAdvertisementRepository;

	@Autowired
	private MeliService meliService;

	@Autowired
	private MeliAccountService meliAccountService;

	@Test
	public void shouldFindEligibleAds() {

		List<AdvertisementDTO> advertisementsDTOBuilt = new AdvertisementDTOBuilder().attributes(new ArrayList<>())
				.itemsAmount(10).buildList();
		List<MeliDTO> meliDTOBuilt = new MeliDTOBuilder().itemsAmount(10).buildList();
		when(meliService.findAllActivatedMeli()).thenReturn(meliDTOBuilt);
		when(advertisementService.findEligibleAds(any(MeliDTO.class))).thenReturn(advertisementsDTOBuilt);

		when(meliAccountService.meliAccountProbablyHasNoAds(any(MeliDTO.class))).thenReturn(Boolean.TRUE);

		eligibleAdvertisementService.findEligibleAds();

		verify(eligibleAdvertisementRepository, times(10)).saveAll(anyList());

	}

	@Test
	public void shouldFindAdvertisements() {

		List<EligibleAdvertisement> eligibleAds = new EligibleAdvertisementBuilder().itemsAmount(10)
				.attributes(new ArrayList<>()).buildList();
		when(eligibleAdvertisementRepository.findByMeliId(anyString(), any(PageRequest.class)))
				.thenReturn(new PageImpl<>(eligibleAds));

		AdvertisementRequestDTO adsBuilt = new AdvertisementRequestDTOBuilder().pageNumber(1l).pageLimit(20l)
				.meliId("meliIdTeste").build();

		when(meliAccountService.meliAccountProbablyHasNoAds(any(MeliDTO.class))).thenReturn(Boolean.TRUE);

		AdvertisementDTOWrapper advertisementDTOWrapper = eligibleAdvertisementService.findAdvertisements(adsBuilt);

		assertThat("Should return the Total Elements", advertisementDTOWrapper.getTotalElements(), equalTo(10l));
		assertThat("Should return the list of Advertisements", advertisementDTOWrapper.getAdvertisementsDTO(),
				hasSize(10));

	}

	@Configuration
	static class Config {

		@Bean
		public EligibleAdvertisementService eligibleAdvertisementService() {
			return new EligibleAdvertisementServiceImpl();
		}

		@Bean
		public AdvertisementService advertisementServiceMock() {
			return mock(AdvertisementService.class);
		}

		@Bean
		public EligibleAdsRepository eligibleAdvertisementRepository() {
			return mock(EligibleAdsRepository.class);
		}

		@Bean
		public MeliService meliService() {
			return mock(MeliService.class);
		}

		@Bean
		public MeliAccountService meliAccountService() {
			return mock(MeliAccountService.class);
		}

	}

}
