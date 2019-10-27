package com.catalog.eligibleads.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catalog.eligibleads.controller.resource.EligibleAdvertisementResource;
import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.response.EligibleAdvertisementsResponse;
import com.catalog.eligibleads.service.EligibleAdvertisementService;

@RestController
@RequestMapping("buybox/eligible")
public class EligibleAdvertisementController implements EligibleAdvertisementResource {

	@Autowired
	private EligibleAdvertisementService eligibleAdvertisementService;

	@Override
	@GetMapping(path = "/{meliId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EligibleAdvertisementsResponse> findEligibleAds(@PathVariable("meliId") String meliId,
			@RequestParam("access_token") String accessToken) {

		List<AdvertisementDTO> advertisements = this.eligibleAdvertisementService.findAdvertisements(meliId,
				accessToken);

		return ResponseEntity.ok(new EligibleAdvertisementsResponse(advertisements));
	}

	@Override
	@PostMapping
	public ResponseEntity<Void> saveAllEligibleAds() {

		this.eligibleAdvertisementService.findEligibleAds();

		return ResponseEntity.noContent().build();
	}

}
