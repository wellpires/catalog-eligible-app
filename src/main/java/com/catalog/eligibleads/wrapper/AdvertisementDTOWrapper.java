package com.catalog.eligibleads.wrapper;

import java.util.List;

import com.catalog.eligibleads.dto.AdvertisementDTO;

public class AdvertisementDTOWrapper {

	private List<AdvertisementDTO> advertisementsDTO;
	private Long totalElements;

	public List<AdvertisementDTO> getAdvertisementsDTO() {
		return advertisementsDTO;
	}

	public void setAdvertisementsDTO(List<AdvertisementDTO> advertisementsDTO) {
		this.advertisementsDTO = advertisementsDTO;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

}
