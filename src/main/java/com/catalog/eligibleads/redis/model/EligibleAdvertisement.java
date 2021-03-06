package com.catalog.eligibleads.redis.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "eligibleAd")
public class EligibleAdvertisement implements Serializable {

	private static final long serialVersionUID = -2795955666775572028L;

	@Id
	private Long id;
	@Reference
	private List<Attribute> attributes;
	@Indexed
	private String meliId;
	private String title;
	private String subtitle;
	private BigDecimal price;
	private String domainId;
	private String image;
	private String variationName;
	private String variationSKU;
	private String siteId;
	private String brand;
	private String model;
	private String productIdentifier;
	private String status;
	private Long availableQuantity;
	private Long variationId;
	private String permalink;
	private String mlbId;

	@Transient
	private String accountName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public String getMeliId() {
		return meliId;
	}

	public void setMeliId(String meliId) {
		this.meliId = meliId;
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

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getVariationName() {
		return variationName;
	}

	public void setVariationName(String variationName) {
		this.variationName = variationName;
	}

	public String getVariationSKU() {
		return variationSKU;
	}

	public void setVariationSKU(String variationSKU) {
		this.variationSKU = variationSKU;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getProductIdentifier() {
		return productIdentifier;
	}

	public void setProductIdentifier(String productIdentifier) {
		this.productIdentifier = productIdentifier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Long getVariationId() {
		return variationId;
	}

	public void setVariationId(Long variationId) {
		this.variationId = variationId;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getMlbId() {
		return mlbId;
	}

	public void setMlbId(String mlbId) {
		this.mlbId = mlbId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
