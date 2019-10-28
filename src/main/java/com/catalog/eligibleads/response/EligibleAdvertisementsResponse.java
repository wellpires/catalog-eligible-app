package com.catalog.eligibleads.response;

import java.util.List;

import com.catalog.eligibleads.dto.AdvertisementDTO;

public class EligibleAdvertisementsResponse {

	private Long totalItems;

	private List<AdvertisementDTO> advertisements;

	public EligibleAdvertisementsResponse(List<AdvertisementDTO> advertisements, Long totalItems) {
		this.advertisements = advertisements;
		this.totalItems = totalItems;
	}

	public Long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Long totalItems) {
		this.totalItems = totalItems;
	}

	public List<AdvertisementDTO> getAdvertisements() {
		return advertisements;
	}

	public void setAdvertisements(List<AdvertisementDTO> advertisements) {
		this.advertisements = advertisements;
	}

}
