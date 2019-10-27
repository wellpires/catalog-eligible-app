package com.catalog.eligibleads.controller.resource;

import org.springframework.http.ResponseEntity;

import com.catalog.eligibleads.response.EligibleAdvertisementsResponse;

public interface EligibleAdvertisementResource {

	public ResponseEntity<EligibleAdvertisementsResponse> findEligibleAds(String meliId, String accessToken);

	public ResponseEntity<Void> saveAllEligibleAds();

}
