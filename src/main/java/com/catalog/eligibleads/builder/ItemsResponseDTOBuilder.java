package com.catalog.eligibleads.builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.catalog.eligibleads.dto.ItemsResponseDTO;

public class ItemsResponseDTOBuilder {

	private List<String> items;
	private int itemsAmount;

	public ItemsResponseDTOBuilder items(List<String> items) {
		this.items = items;
		return this;
	}

	public ItemsResponseDTOBuilder itemsAmount(int itemsAmount) {
		this.itemsAmount = itemsAmount;
		return this;
	}

	public ItemsResponseDTO build() {
		ItemsResponseDTO itemsResponseDTO = new ItemsResponseDTO();
		itemsResponseDTO.setItems(items);
		return itemsResponseDTO;
	}

	public List<ItemsResponseDTO> buildList() {
		return IntStream.range(0, itemsAmount).mapToObj(index -> {
			return build();
		}).collect(Collectors.toList());
	}

}
