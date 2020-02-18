package com.catalog.eligibleads.builder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.catalog.eligibleads.dto.AdvertisementVariationDTO;
import com.catalog.eligibleads.dto.ItemAttributeDTO;

public class AdvertisementVariationDTOBuilder {

	private int itemsAmount;
	private Set<ItemAttributeDTO> attributes;

	public AdvertisementVariationDTOBuilder itemsAmount(int itemsAmount) {
		this.itemsAmount = itemsAmount;
		return this;
	}

	public AdvertisementVariationDTOBuilder attributes(Set<ItemAttributeDTO> attributes) {
		this.attributes = attributes;
		return this;
	}

	public List<AdvertisementVariationDTO> buildList() {
		return IntStream.range(0, itemsAmount).mapToObj(index -> {
			AdvertisementVariationDTO advertisementVariationDTO = new AdvertisementVariationDTO();
			advertisementVariationDTO.setId(Long.valueOf(index));
			advertisementVariationDTO.setAttributes(attributes);
			return advertisementVariationDTO;
		}).collect(Collectors.toList());
	}

}
