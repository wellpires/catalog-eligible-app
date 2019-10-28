package com.catalog.eligibleads.builder;

import com.catalog.eligibleads.dto.AdvertisementRequestDTO;
import com.catalog.eligibleads.dto.PagingDTO;

public class AdvertisementRequestDTOBuilder {

	private String meliId;
	private String accessToken;
	private Long pageLimit;
	private Long pageNumber;

	public AdvertisementRequestDTOBuilder meliId(String meliId) {
		this.meliId = meliId;
		return this;
	}

	public AdvertisementRequestDTOBuilder accessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	public AdvertisementRequestDTOBuilder pageLimit(Long pageLimit) {
		this.pageLimit = pageLimit;
		return this;
	}

	public AdvertisementRequestDTOBuilder pageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
		return this;
	}

	public AdvertisementRequestDTO build() {
		PagingDTO pagingDTO = new PagingDTO();
		pagingDTO.setLimit(pageLimit);
		pagingDTO.setOffset(pageNumber);

		AdvertisementRequestDTO advertisementRequestDTO = new AdvertisementRequestDTO();
		advertisementRequestDTO.setAccessToken(accessToken);
		advertisementRequestDTO.setMeliId(meliId);
		advertisementRequestDTO.setPaging(pagingDTO);
		return advertisementRequestDTO;
	}

}
