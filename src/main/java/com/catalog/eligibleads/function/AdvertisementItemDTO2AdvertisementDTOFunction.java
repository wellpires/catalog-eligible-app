package com.catalog.eligibleads.function;

import java.util.function.Function;

import org.apache.commons.lang3.math.NumberUtils;

import com.catalog.eligibleads.builder.AdvertisementDTOBuilder;
import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.dto.AdvertisementItemDTO;

public class AdvertisementItemDTO2AdvertisementDTOFunction implements Function<AdvertisementItemDTO, AdvertisementDTO> {

	@Override
	public AdvertisementDTO apply(AdvertisementItemDTO advertisementItemDTO) {
		return new AdvertisementDTOBuilder().id(advertisementItemDTO.getId()).title(advertisementItemDTO.getTitle())
				.subtitle(advertisementItemDTO.getSubtitle()).price(advertisementItemDTO.getPrice())
				.domainId(advertisementItemDTO.getDomainId()).image(advertisementItemDTO.getThumbnail())
				.availableQuantity(advertisementItemDTO.getAvailableQuantity())
				.permalink(advertisementItemDTO.getPermalink()).attributes(advertisementItemDTO.getAttributes())
				.siteId(advertisementItemDTO.getSiteId()).status(advertisementItemDTO.getStatus())
				.variationId(NumberUtils.LONG_ZERO).build();
	}

}
