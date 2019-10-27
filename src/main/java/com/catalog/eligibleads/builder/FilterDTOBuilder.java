package com.catalog.eligibleads.builder;

import com.catalog.eligibleads.dto.FilterDTO;
import com.catalog.eligibleads.enums.AdvertisementStatus;
import com.catalog.eligibleads.enums.Order;

public class FilterDTOBuilder {

	private String accessToken;
	private Long limit;
	private Integer offset;
	private Order order;
	private AdvertisementStatus status;

	public FilterDTOBuilder accessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	public FilterDTOBuilder limit(Long limit) {
		this.limit = limit;
		return this;
	}

	public FilterDTOBuilder offset(Integer offset) {
		this.offset = offset;
		return this;
	}

	public FilterDTOBuilder order(Order order) {
		this.order = order;
		return this;
	}

	public FilterDTOBuilder status(AdvertisementStatus status) {
		this.status = status;
		return this;
	}

	public FilterDTO build() {
		FilterDTO filterDTO = new FilterDTO();
		filterDTO.setAccessToken(accessToken);
		filterDTO.setLimit(limit);
		filterDTO.setOffset(offset);
		filterDTO.setOrder(order);
		filterDTO.setStatus(status);

		return filterDTO;
	}

}
