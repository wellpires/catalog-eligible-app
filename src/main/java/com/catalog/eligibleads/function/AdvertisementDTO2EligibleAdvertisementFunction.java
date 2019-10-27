package com.catalog.eligibleads.function;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.catalog.eligibleads.builder.EligibleAdvertisementBuilder;
import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.redis.model.Attribute;
import com.catalog.eligibleads.redis.model.EligibleAdvertisement;

public class AdvertisementDTO2EligibleAdvertisementFunction
		implements Function<AdvertisementDTO, EligibleAdvertisement> {

	@Override
	public EligibleAdvertisement apply(AdvertisementDTO advertisementDTO) {

		List<Attribute> attributes = advertisementDTO.getAttributes().stream().map(new AttributeDTO2AttributeFunction())
				.collect(Collectors.toList());

		return new EligibleAdvertisementBuilder().mlbId(advertisementDTO.getId()).title(advertisementDTO.getTitle())
				.subtitle(advertisementDTO.getSubtitle()).price(advertisementDTO.getPrice())
				.domainId(advertisementDTO.getDomainId()).image(advertisementDTO.getImage())
				.availableQuantity(advertisementDTO.getAvailableQuantity()).permalink(advertisementDTO.getPermalink())
				.attributes(attributes).siteId(advertisementDTO.getSiteId()).status(advertisementDTO.getStatus())
				.variationName(advertisementDTO.getVariationName()).variationSKU(advertisementDTO.getVariationSKU())
				.brand(advertisementDTO.getBrand()).model(advertisementDTO.getModel())
				.productIdentifier(advertisementDTO.getProductIdentifier())
				.variationId(advertisementDTO.getVariationId()).meliId(advertisementDTO.getMeliId()).build();
	}

}
