package com.catalog.eligibleads.builder;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.catalog.eligibleads.dto.ItemAttributeDTO;

public class ItemAttributeDTOBuilder {

	private int itemsAmount;
	private String valueName;

	public ItemAttributeDTOBuilder itemsAmount(int itemsAmount) {
		this.itemsAmount = itemsAmount;
		return this;
	}

	public ItemAttributeDTOBuilder valueName(String valueName) {
		this.valueName = valueName;
		return this;
	}

	public Set<ItemAttributeDTO> buildSet() {
		return IntStream.range(0, itemsAmount).mapToObj(index -> {
			ItemAttributeDTO itemAttributeDTO = new ItemAttributeDTO();
			itemAttributeDTO.setValueName(valueName);
			return itemAttributeDTO;
		}).collect(Collectors.toSet());
	}

}
