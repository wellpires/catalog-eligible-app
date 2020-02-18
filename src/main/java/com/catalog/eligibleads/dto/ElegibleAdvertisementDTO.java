package com.catalog.eligibleads.dto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.catalog.eligibleads.enums.BuyBoxStatus;
import com.catalog.eligibleads.function.BuyBoxVariationDTO2ElegibleAdvertisementDTOFunction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElegibleAdvertisementDTO {

	private String id;
	private BuyBoxStatus status;

	@JsonProperty("site_id")
	private String siteId;

	@JsonProperty("domain_id")
	private String domainId;

	@JsonProperty("buy_box_eligible")
	private Boolean buyBoxElegible;

	@JsonProperty("variations")
	private List<BuyBoxVariationDTO> buyBoxVariations;

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

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public Boolean getBuyBoxElegible() {
		return buyBoxElegible;
	}

	public void setBuyBoxElegible(Boolean buyBoxElegible) {
		this.buyBoxElegible = buyBoxElegible;
	}

	public List<BuyBoxVariationDTO> getBuyBoxVariations() {
		return buyBoxVariations;
	}

	public void setBuyBoxVariations(List<BuyBoxVariationDTO> buyBoxVariations) {
		this.buyBoxVariations = buyBoxVariations;
	}

	public List<ElegibleAdvertisementDTO> selectElegibles() {

		List<ElegibleAdvertisementDTO> elegibles = Collections.emptyList();
		if (Objects.isNull(this.buyBoxElegible)) {
			elegibles = this.buyBoxVariations.stream().filter(BuyBoxVariationDTO::isElegible)
					.map(new BuyBoxVariationDTO2ElegibleAdvertisementDTOFunction()).collect(Collectors.toList());
			elegibles.stream().forEach(elegible -> {
				elegible.setId(this.id);
				elegible.setBuyBoxVariations(this.buyBoxVariations);
			});
		}

		if (Objects.nonNull(this.buyBoxElegible) && this.buyBoxElegible) {
			elegibles = Arrays.asList(this);
		}

		return elegibles;
	}
	
}
