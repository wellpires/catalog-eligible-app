package com.catalog.eligibleads.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catalog.eligibleads.builder.AdvertisementRequestDTOBuilder;
import com.catalog.eligibleads.controller.resource.EligibleAdvertisementResource;
import com.catalog.eligibleads.dto.AdvertisementRequestDTO;
import com.catalog.eligibleads.response.EligibleAdvertisementsResponse;
import com.catalog.eligibleads.service.EligibleAdvertisementService;
import com.catalog.eligibleads.wrapper.AdvertisementDTOWrapper;

@RestController
@RequestMapping("v1/catalog/eligibles")
public class EligibleAdvertisementController implements EligibleAdvertisementResource {

	@Autowired
	private EligibleAdvertisementService eligibleAdvertisementService;

	@Override
	@GetMapping(path = "/{meliId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EligibleAdvertisementsResponse> findEligibleAds(@PathVariable("meliId") String meliId,
			@RequestParam("limit") Long limit, @RequestParam("page") Long page) {

		AdvertisementRequestDTO advertisementRequestDTO = new AdvertisementRequestDTOBuilder().meliId(meliId)
				.pageLimit(limit).pageNumber(page).build();

		AdvertisementDTOWrapper advertisements = this.eligibleAdvertisementService
				.findAdvertisements(advertisementRequestDTO);

		return ResponseEntity.ok(new EligibleAdvertisementsResponse(advertisements.getAdvertisementsDTO(),
				advertisements.getTotalElements()));
	}

}
