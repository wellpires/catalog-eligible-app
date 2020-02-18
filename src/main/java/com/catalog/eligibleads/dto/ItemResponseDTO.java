package com.catalog.eligibleads.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemResponseDTO {

	private Integer code;

	@JsonProperty("body")
	private AdvertisementItemDTO advertisementItemDTO;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public AdvertisementItemDTO getAdvertisementItemDTO() {
		return advertisementItemDTO;
	}

	public void setAdvertisementItemDTO(AdvertisementItemDTO advertisementItemDTO) {
		this.advertisementItemDTO = advertisementItemDTO;
	}

	public boolean isSuccess() {
		return HttpStatus.OK.value() == code;
	}

}
