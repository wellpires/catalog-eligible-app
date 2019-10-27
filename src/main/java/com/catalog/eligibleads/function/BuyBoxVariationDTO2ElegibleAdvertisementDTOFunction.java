package com.catalog.eligibleads.function;

import java.util.function.Function;

import com.catalog.eligibleads.builder.ElegibleAdvertisementDTOBuilder;
import com.catalog.eligibleads.dto.BuyBoxVariationDTO;
import com.catalog.eligibleads.dto.ElegibleAdvertisementDTO;

public class BuyBoxVariationDTO2ElegibleAdvertisementDTOFunction
		implements Function<BuyBoxVariationDTO, ElegibleAdvertisementDTO> {

	@Override
	public ElegibleAdvertisementDTO apply(BuyBoxVariationDTO buyBoxVariationDTO) {
		return new ElegibleAdvertisementDTOBuilder().status(buyBoxVariationDTO.getStatus())
				.buyBoxElegible(buyBoxVariationDTO.getBuyBoxElegible()).build();
	}

}
