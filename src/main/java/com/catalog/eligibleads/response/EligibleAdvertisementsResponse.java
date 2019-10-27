package com.catalog.eligibleads.response;

import java.util.List;

import com.catalog.eligibleads.dto.AdvertisementDTO;

public class EligibleAdvertisementsResponse {

	private List<AdvertisementDTO> advertisements;

	public EligibleAdvertisementsResponse(List<AdvertisementDTO> advertisements) {
		this.advertisements = advertisements;
	}

	public List<AdvertisementDTO> getAdvertisements() {
		return advertisements;
	}

	public void setAdvertisements(List<AdvertisementDTO> advertisements) {
		this.advertisements = advertisements;
	}

}
