package com.catalog.eligibleads.builder;

import java.util.List;

import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.wrapper.AdvertisementDTOWrapper;

public class AdvertisementDTOWrapperBuilder {

	private List<AdvertisementDTO> advertisementsDTO;
	private Long totalElements;

	public AdvertisementDTOWrapperBuilder advertisementsDTO(List<AdvertisementDTO> advertisementsDTO) {
		this.advertisementsDTO = advertisementsDTO;
		return this;
	}

	public AdvertisementDTOWrapperBuilder totalElements(Long totalElements) {
		this.totalElements = totalElements;
		return this;
	}

	public AdvertisementDTOWrapper build() {
		AdvertisementDTOWrapper advertisementDTOWrapper = new AdvertisementDTOWrapper();
		advertisementDTOWrapper.setAdvertisementsDTO(advertisementsDTO);
		advertisementDTOWrapper.setTotalElements(totalElements);
		return advertisementDTOWrapper;
	}

}
