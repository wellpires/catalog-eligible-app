package com.catalog.eligibleads.builder;

import com.catalog.eligibleads.dto.AttributeDTO;

public class AttributeDTOBuilder {

	private String id;
	private String name;
	private String valueId;
	private String valueName;
	private String styleColor;

	public AttributeDTOBuilder id(String id) {
		this.id = id;
		return this;
	}

	public AttributeDTOBuilder name(String name) {
		this.name = name;
		return this;
	}

	public AttributeDTOBuilder valueId(String valueId) {
		this.valueId = valueId;
		return this;
	}

	public AttributeDTOBuilder valueName(String valueName) {
		this.valueName = valueName;
		return this;
	}

	public AttributeDTOBuilder styleColor(String styleColor) {
		this.styleColor = styleColor;
		return this;
	}

	public AttributeDTO build() {

		AttributeDTO attributeDTO = new AttributeDTO();
		attributeDTO.setId(id);
		attributeDTO.setName(name);
		attributeDTO.setValueId(valueId);
		attributeDTO.setValueName(valueName);
		attributeDTO.setStyleColor(styleColor);

		return attributeDTO;
	}

}
