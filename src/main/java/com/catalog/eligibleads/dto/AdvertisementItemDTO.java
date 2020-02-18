package com.catalog.eligibleads.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdvertisementItemDTO {

	private String id;
	private String title;
	private String subtitle;
	private BigDecimal price;
	private String thumbnail;
	private List<PictureDTO> pictures;

	@JsonProperty("available_quantity")
	private Long availableQuantity;

	@JsonProperty("domain_id")
	private String domainId;

	private String permalink;

	@JsonProperty("site_id")
	private String siteId;

	private String status;

	private List<AdvertisementVariationDTO> variations;

	private List<AttributeDTO> attributes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Long getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<AdvertisementVariationDTO> getVariations() {
		return variations;
	}

	public void setVariations(List<AdvertisementVariationDTO> variations) {
		this.variations = variations;
	}

	public List<AttributeDTO> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeDTO> attributes) {
		this.attributes = attributes;
	}

	public List<PictureDTO> getPictures() {
		return pictures;
	}

	public void setPictures(List<PictureDTO> pictures) {
		this.pictures = pictures;
	}
	
}
