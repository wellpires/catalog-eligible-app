package com.catalog.eligibleads.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PictureDTO {

	private String id;

	private String url;

	@JsonProperty("suggested_for_picker")
	private List<String> suggestedForPicker;

	@JsonProperty("max_width")
	private String maxWidth;

	@JsonProperty("max_height")
	private String maxHeight;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getSuggestedForPicker() {
		return suggestedForPicker;
	}

	public void setSuggestedForPicker(List<String> suggestedForPicker) {
		this.suggestedForPicker = suggestedForPicker;
	}

	public String getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(String maxWidth) {
		this.maxWidth = maxWidth;
	}

	public String getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(String maxHeight) {
		this.maxHeight = maxHeight;
	}

}
