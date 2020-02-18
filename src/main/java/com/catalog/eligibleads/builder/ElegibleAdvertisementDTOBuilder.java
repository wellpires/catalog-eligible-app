package com.catalog.eligibleads.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.RandomUtils;

import com.catalog.eligibleads.dto.BuyBoxVariationDTO;
import com.catalog.eligibleads.dto.ElegibleAdvertisementDTO;
import com.catalog.eligibleads.enums.BuyBoxStatus;
import com.catalog.eligibleads.enums.GenerationIdStrategy;

public class ElegibleAdvertisementDTOBuilder {

	private String id;
	private BuyBoxStatus status;
	private Boolean buyBoxElegible;
	private long itemsAmount;
	private List<BuyBoxVariationDTO> buyBoxVariations;
	private GenerationIdStrategy strategy;

	public ElegibleAdvertisementDTOBuilder id(String id) {
		this.id = id;
		return this;
	}

	public ElegibleAdvertisementDTOBuilder status(BuyBoxStatus status) {
		this.status = status;
		return this;
	}

	public ElegibleAdvertisementDTOBuilder buyBoxElegible(Boolean buyBoxElegible) {
		this.buyBoxElegible = buyBoxElegible;
		return this;
	}

	public ElegibleAdvertisementDTOBuilder itemsAmount(long itemsAmount) {
		this.itemsAmount = itemsAmount;
		return this;
	}

	public ElegibleAdvertisementDTOBuilder buyBoxVariations(List<BuyBoxVariationDTO> buyBoxVariations) {
		this.buyBoxVariations = buyBoxVariations;
		return this;
	}

	public ElegibleAdvertisementDTOBuilder nonSequentialIds(GenerationIdStrategy strategy) {
		this.strategy = strategy;
		return this;
	}

	public ElegibleAdvertisementDTO build() {
		ElegibleAdvertisementDTO elegibleAdvertisementDTO = new ElegibleAdvertisementDTO();
		elegibleAdvertisementDTO.setId(id);
		elegibleAdvertisementDTO.setStatus(status);
		elegibleAdvertisementDTO.setBuyBoxElegible(buyBoxElegible);
		elegibleAdvertisementDTO.setBuyBoxVariations(buyBoxVariations);
		return elegibleAdvertisementDTO;
	}

	public List<ElegibleAdvertisementDTO> buildList() {

		if (Objects.isNull(strategy)) {
			this.strategy = GenerationIdStrategy.ALL_NUMBERS;
		}

		List<ElegibleAdvertisementDTO> elegiblesAdvertisements = new ArrayList<>();
		for (long i = 0; i < itemsAmount; i++) {
			ElegibleAdvertisementDTO elegibleAdvertisementDTO = new ElegibleAdvertisementDTO();
			elegibleAdvertisementDTO.setId("idTeste".concat(strategy.defineId(i).toString()));
			elegibleAdvertisementDTO
					.setStatus(BuyBoxStatus.valueOf(RandomUtils.nextInt(0, BuyBoxStatus.values().length)));
			elegibleAdvertisementDTO.setBuyBoxElegible(Boolean.TRUE);
			elegibleAdvertisementDTO.setBuyBoxVariations(buyBoxVariations);
			elegiblesAdvertisements.add(elegibleAdvertisementDTO);
		}

		return elegiblesAdvertisements;
	}
	
}
