package com.catalog.eligibleads.function;

import java.util.function.Function;

import com.catalog.eligibleads.builder.AttributeDTOBuilder;
import com.catalog.eligibleads.dto.AttributeDTO;
import com.catalog.eligibleads.redis.model.Attribute;

public class Attribute2AttributeDTOFunction implements Function<Attribute, AttributeDTO> {

	@Override
	public AttributeDTO apply(Attribute attribute) {
		return new AttributeDTOBuilder().id(attribute.getId()).name(attribute.getName()).valueId(attribute.getValueId())
				.valueName(attribute.getValueName()).styleColor(attribute.getStyleColor()).build();
	}

}
