package com.catalog.eligibleads.builder;

import com.catalog.eligibleads.redis.model.Attribute;

public class AttributeBuilder {

	private String id;
	private String name;
	private String valueId;
	private String valueName;
	private String styleColor;

	public AttributeBuilder id(String id) {
		this.id = id;
		return this;
	}

	public AttributeBuilder name(String name) {
		this.name = name;
		return this;
	}

	public AttributeBuilder valueId(String valueId) {
		this.valueId = valueId;
		return this;
	}

	public AttributeBuilder valueName(String valueName) {
		this.valueName = valueName;
		return this;
	}

	public AttributeBuilder styleColor(String styleColor) {
		this.styleColor = styleColor;
		return this;
	}

	public Attribute build() {

		Attribute attribute = new Attribute();
		attribute.setId(id);
		attribute.setName(name);
		attribute.setValueId(valueId);
		attribute.setValueName(valueName);
		attribute.setStyleColor(styleColor);

		return attribute;
	}

}
