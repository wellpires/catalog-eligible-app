package com.catalog.eligibleads.builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;

import com.catalog.eligibleads.dto.ItemResponseDTO;

public class ItemResponseDTOBuilder {

	private int itemsAmount;
	private int code;

	public ItemResponseDTOBuilder itemsAmount(int itemsAmount) {
		this.itemsAmount = itemsAmount;
		return this;
	}

	public List<ItemResponseDTO> buildList() {
		return IntStream.range(0, itemsAmount).mapToObj(index -> {
			ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
			itemResponseDTO.setCode(code);
			return itemResponseDTO;
		}).collect(Collectors.toList());
	}

	public ItemResponseDTOBuilder code(HttpStatus httpCode) {
		this.code = httpCode.value();
		return this;
	}

}
