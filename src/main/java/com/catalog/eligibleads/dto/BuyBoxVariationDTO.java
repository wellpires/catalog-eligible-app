package com.catalog.eligibleads.dto;

import java.util.Objects;

import com.catalog.eligibleads.enums.BuyBoxStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BuyBoxVariationDTO {

	private String id;
	private BuyBoxStatus status;

	@JsonProperty("buy_box_eligible")
	private Boolean buyBoxElegible;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BuyBoxStatus getStatus() {
		return status;
	}

	public void setStatus(BuyBoxStatus status) {
		this.status = status;
	}

	public Boolean getBuyBoxElegible() {
		return buyBoxElegible;
	}

	public void setBuyBoxElegible(Boolean buyBoxElegible) {
		this.buyBoxElegible = buyBoxElegible;
	}

	public boolean isElegible() {
		return Objects.nonNull(buyBoxElegible) && buyBoxElegible && Objects.nonNull(status) && status.isReadyForOptin();
	}
	
}
