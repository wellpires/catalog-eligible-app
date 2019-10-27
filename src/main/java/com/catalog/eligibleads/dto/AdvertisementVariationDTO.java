package com.catalog.eligibleads.dto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdvertisementVariationDTO {

	private Long id;
	private String name;
	@JsonProperty("attribute_combinations")
	private Set<ItemAttributeDTO> attributes;
	@JsonProperty("seller_custom_field")
	private String sellerCustomField;
	@JsonProperty("picture_ids")
	private List<String> pictureIds;

	public String getSku() {

		if (attributes != null && !attributes.isEmpty()) {
			Optional<ItemAttributeDTO> sellerSKU = attributes.stream()
					.filter(a -> a.getId().equalsIgnoreCase("SELLER_SKU")).findFirst();

			if (sellerSKU.isPresent() && Objects.nonNull(sellerSKU.get().getValueName())) {
				return sellerSKU.get().getValueName();
			}
		}

		return sellerCustomField;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ItemAttributeDTO> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<ItemAttributeDTO> attributes) {
		this.attributes = attributes;
	}

	public String getSellerCustomField() {
		return sellerCustomField;
	}

	public void setSellerCustomField(String sellerCustomField) {
		this.sellerCustomField = sellerCustomField;
	}

	public List<String> getPictureIds() {
		return pictureIds;
	}

	public void setPictureIds(List<String> pictureIds) {
		this.pictureIds = pictureIds;
	}

}
