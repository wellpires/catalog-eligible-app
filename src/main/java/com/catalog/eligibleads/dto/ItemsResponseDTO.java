package com.catalog.eligibleads.dto;

import java.util.List;

import com.catalog.eligibleads.dto.PagingDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsResponseDTO {

	private PagingDTO paging;

	@JsonProperty("results")
	private List<String> items;

	public PagingDTO getPaging() {
		return paging;
	}

	public void setPaging(PagingDTO paging) {
		this.paging = paging;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

}
