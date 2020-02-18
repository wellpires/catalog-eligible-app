package com.catalog.eligibleads.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.catalog.eligibleads.builder.AdvertisementDTOBuilder;
import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.dto.AttributeDTO;
import com.catalog.eligibleads.redis.model.EligibleAdvertisement;

public class EligibleAdvertisement2AdvertisementDTOFunction
		implements Function<EligibleAdvertisement, AdvertisementDTO> {

	@Override
	public AdvertisementDTO apply(EligibleAdvertisement eligibleAdvertisement) {

		List<AttributeDTO> attributes = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(eligibleAdvertisement.getAttributes())) {
			attributes = eligibleAdvertisement.getAttributes().stream().map(new Attribute2AttributeDTOFunction())
					.collect(Collectors.toList());
		}

		return new AdvertisementDTOBuilder().id(eligibleAdvertisement.getMlbId())
				.title(eligibleAdvertisement.getTitle()).subtitle(eligibleAdvertisement.getSubtitle())
				.price(eligibleAdvertisement.getPrice()).domainId(eligibleAdvertisement.getDomainId())
				.image(eligibleAdvertisement.getImage()).availableQuantity(eligibleAdvertisement.getAvailableQuantity())
				.permalink(eligibleAdvertisement.getPermalink()).attributes(attributes)
				.siteId(eligibleAdvertisement.getSiteId()).status(eligibleAdvertisement.getStatus())
				.variationName(eligibleAdvertisement.getVariationName())
				.variationSKU(eligibleAdvertisement.getVariationSKU()).brand(eligibleAdvertisement.getBrand())
				.model(eligibleAdvertisement.getModel()).productIdentifier(eligibleAdvertisement.getProductIdentifier())
				.variationId(eligibleAdvertisement.getVariationId()).meliId(eligibleAdvertisement.getMeliId()).build();
	}

}
