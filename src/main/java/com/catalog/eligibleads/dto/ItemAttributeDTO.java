package com.catalog.eligibleads.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemAttributeDTO {

	private String id;
	
	private String name;
	
	@JsonProperty("value_id")
	private String valueId;
	
	@JsonProperty("value_name")
	private String valueName;
	
	@JsonProperty("value_struct")
	private ValueStructDTO valueStruct;
	
	@JsonProperty("attribute_group_id")
	private String attributeGroupId;
	
	@JsonProperty("attribute_group_name")
	private String attributeGroupName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValueId() {
		return valueId;
	}

	public void setValueId(String valueId) {
		this.valueId = valueId;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	public ValueStructDTO getValueStruct() {
		return valueStruct;
	}

	public void setValueStruct(ValueStructDTO valueStruct) {
		this.valueStruct = valueStruct;
	}

	public String getAttributeGroupId() {
		return attributeGroupId;
	}

	public void setAttributeGroupId(String attributeGroupId) {
		this.attributeGroupId = attributeGroupId;
	}

	public String getAttributeGroupName() {
		return attributeGroupName;
	}

	public void setAttributeGroupName(String attributeGroupName) {
		this.attributeGroupName = attributeGroupName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemAttributeDTO other = (ItemAttributeDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
