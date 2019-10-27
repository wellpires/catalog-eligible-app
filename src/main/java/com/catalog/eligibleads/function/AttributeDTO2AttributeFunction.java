package com.catalog.eligibleads.function;

import java.util.function.Function;

import com.catalog.eligibleads.builder.AttributeBuilder;
import com.catalog.eligibleads.dto.AttributeDTO;
import com.catalog.eligibleads.redis.model.Attribute;

public class AttributeDTO2AttributeFunction implements Function<AttributeDTO, Attribute> {

	@Override
	public Attribute apply(AttributeDTO attributeDTO) {
		return new AttributeBuilder().id(attributeDTO.getId()).name(attributeDTO.getName())
				.valueId(attributeDTO.getValueId()).valueName(attributeDTO.getValueName())
				.styleColor(attributeDTO.getStyleColor()).build();
	}

}
