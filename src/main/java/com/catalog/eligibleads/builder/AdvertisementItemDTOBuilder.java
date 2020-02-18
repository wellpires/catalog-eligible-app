package com.catalog.eligibleads.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections.CollectionUtils;

import com.catalog.eligibleads.dto.AdvertisementItemDTO;
import com.catalog.eligibleads.dto.AdvertisementVariationDTO;

public class AdvertisementItemDTOBuilder {

	private int itemsAmount;
	private String id;
	private List<AdvertisementVariationDTO> variations;
	private String permalink;

	public AdvertisementItemDTOBuilder itemsAmount(int itemsAmount) {
		this.itemsAmount = itemsAmount;
		return this;
	}

	public AdvertisementItemDTOBuilder id(String id) {
		this.id = id;
		return this;
	}

	public AdvertisementItemDTOBuilder variations(List<AdvertisementVariationDTO> variations) {
		this.variations = variations;
		return this;
	}

	public AdvertisementItemDTOBuilder permalink(String permalink) {
		this.permalink = permalink;
		return this;
	}

	public List<AdvertisementItemDTO> buildList() {
		return IntStream.range(0, itemsAmount).mapToObj(index -> {
			AdvertisementItemDTO advertisementItemDTO = new AdvertisementItemDTO();
			advertisementItemDTO.setId(Optional.ofNullable(id).orElseGet(() -> String.valueOf(index)));
			advertisementItemDTO.setPermalink(permalink);
			advertisementItemDTO.setPictures(new ArrayList<>());
			advertisementItemDTO.setVariations(CollectionUtils.isEmpty(variations) ? new ArrayList<>() : variations);
			return advertisementItemDTO;
		}).collect(Collectors.toList());
	}

}
