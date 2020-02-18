package com.catalog.eligibleads.dto;

import java.util.Objects;

public class AdvertisementRequestDTO {

	private String meliId;
	private PagingDTO paging;

	public String getMeliId() {
		return meliId;
	}

	public void setMeliId(String meliId) {
		this.meliId = meliId;
	}

	public PagingDTO getPaging() {
		return paging;
	}

	public void setPaging(PagingDTO paging) {
		this.paging = paging;
	}

	public int getPageNumber() {
		int pageNumber = 0;
		if (Objects.nonNull(paging) && Objects.nonNull(paging.getOffset())) {
			pageNumber = this.paging.getOffset().intValue();
		}
		return pageNumber;
	}

	public int getPageLimit() {
		int pageLimit = 0;
		if (Objects.nonNull(paging) && Objects.nonNull(paging.getLimit())) {
			pageLimit = this.paging.getLimit().intValue();
		}
		return pageLimit;
	}

}
