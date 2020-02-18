package com.catalog.eligibleads.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.catalog.eligibleads.builder.AdvertisementDTOBuilder;
import com.catalog.eligibleads.builder.AdvertisementDTOWrapperBuilder;
import com.catalog.eligibleads.dto.AdvertisementRequestDTO;
import com.catalog.eligibleads.response.EligibleAdvertisementsResponse;
import com.catalog.eligibleads.service.EligibleAdvertisementService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "exception.general.error-message=Erro interno" })
public class EligibleAdvertisementControllerTest {

	private static final String PATH_BUY_BOX_ELIGIBLE = "/v1/catalog/eligibles/{meliId}";

	@InjectMocks
	private EligibleAdvertisementController eligibleAdvertisementController;

	@Mock
	private EligibleAdvertisementService eligibleAdvertisementService;

	private MockMvc mockMVC;
	private ObjectMapper mapper;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMVC = MockMvcBuilders.standaloneSetup(eligibleAdvertisementController)
				.setControllerAdvice(new EligibleAdvertisementControllerAdvice()).build();
		this.mapper = new ObjectMapper();
	}

	@Test
	public void shouldFindEligibleAds() throws Exception {

		when(this.eligibleAdvertisementService.findAdvertisements(any(AdvertisementRequestDTO.class)))
				.thenReturn(new AdvertisementDTOWrapperBuilder()
						.advertisementsDTO(new AdvertisementDTOBuilder().itemsAmount(10).buildList()).totalElements(10l)
						.build());

		MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<>();
		queryParameters.add("limit", "1");
		queryParameters.add("page", "2");

		URI pathBuyBoxEligibleUri = UriComponentsBuilder.fromPath(PATH_BUY_BOX_ELIGIBLE)
				.replaceQueryParams(queryParameters).build("MLB123");

		MvcResult result = mockMVC.perform(get(pathBuyBoxEligibleUri).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();

		EligibleAdvertisementsResponse response = mapper.readValue(result.getResponse().getContentAsString(),
				EligibleAdvertisementsResponse.class);

		assertThat("Should return eligibles advertisements", response.getAdvertisements(), hasSize(10));
		assertThat("Should return total of items", response.getTotalItems(), equalTo(10l));
		assertThat("Should return status as OK (200)", result.getResponse().getStatus(),
				equalTo(HttpStatus.OK.value()));

	}

	@Test
	public void shouldNotFindEligibleAdsBecauseExceptionWasThrown() throws Exception {

		when(this.eligibleAdvertisementService.findAdvertisements(any(AdvertisementRequestDTO.class))).thenReturn(null);

		MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<>();
		queryParameters.add("access_token", "");
		queryParameters.add("limit", "1");
		queryParameters.add("page", "2");

		URI pathBuyBoxEligibleUri = UriComponentsBuilder.fromPath(PATH_BUY_BOX_ELIGIBLE)
				.replaceQueryParams(queryParameters).build("MLB123");

		MvcResult result = mockMVC.perform(get(pathBuyBoxEligibleUri).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();

		assertThat("Should return status as Internal Server Error (500)", result.getResponse().getStatus(),
				equalTo(HttpStatus.INTERNAL_SERVER_ERROR.value()));

	}

	@Test
	public void shouldNotFindEligibleAdsBecauseParameterWasNotFound() throws Exception {

		MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<>();
		queryParameters.add("access_token", "");
		queryParameters.add("page", "2");

		URI pathBuyBoxEligibleUri = UriComponentsBuilder.fromPath(PATH_BUY_BOX_ELIGIBLE)
				.replaceQueryParams(queryParameters).build("MLB123");

		MvcResult result = mockMVC.perform(get(pathBuyBoxEligibleUri).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn();

		assertThat("Should return status as Bad Request (400)", result.getResponse().getStatus(),
				equalTo(HttpStatus.BAD_REQUEST.value()));

	}

}
