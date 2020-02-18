package com.catalog.eligibleads.builder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.catalog.eligibleads.dto.BuyBoxVariationDTO;

public class BuyBoxVariationDTOBuilder {

	private String id;
	private int itemsAmount;

	public BuyBoxVariationDTOBuilder id(String id) {
		this.id = id;
		return this;
	}

	public BuyBoxVariationDTOBuilder itemsAmount(int itemsAmount) {
		this.itemsAmount = itemsAmount;
		return this;
	}

	public List<BuyBoxVariationDTO> buildList() {
		return IntStream.range(0, itemsAmount).mapToObj(index -> {
			BuyBoxVariationDTO buyBoxVariationDTO = new BuyBoxVariationDTO();
			buyBoxVariationDTO.setId(Optional.ofNullable(id).orElseGet(() -> String.valueOf(index)));
			return buyBoxVariationDTO;
		}).collect(Collectors.toList());
	}

}
