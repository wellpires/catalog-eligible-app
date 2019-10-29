package com.catalog.eligibleads.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;

import com.catalog.eligibleads.redis.model.Attribute;
import com.catalog.eligibleads.redis.model.EligibleAdvertisement;

public class EligibleAdvertisementBuilder {

	private Long id;
	private String mlbId;
	private String title;
	private String subtitle;
	private BigDecimal price;
	private String domainId;
	private String image;
	private String variationName;
	private Long availableQuantity;
	private String permalink;
	private int itemsAmount;
	private List<Attribute> attributes;
	private String siteId;
	private String status;
	private Long variationId;
	private String variationSKU;
	private String brand;
	private String model;
	private String productIdentifier;
	private String meliId;

	public EligibleAdvertisementBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public EligibleAdvertisementBuilder mlbId(String mlbId) {
		this.mlbId = mlbId;
		return this;
	}

	public EligibleAdvertisementBuilder title(String title) {
		this.title = title;
		return this;
	}

	public EligibleAdvertisementBuilder subtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public EligibleAdvertisementBuilder price(BigDecimal price) {
		this.price = price;
		return this;
	}

	public EligibleAdvertisementBuilder domainId(String domainId) {
		this.domainId = domainId;
		return this;
	}

	public EligibleAdvertisementBuilder image(String image) {
		this.image = image;
		return this;
	}

	public EligibleAdvertisementBuilder variationName(String variationName) {
		this.variationName = variationName;
		return this;
	}

	public EligibleAdvertisementBuilder availableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
		return this;
	}

	public EligibleAdvertisementBuilder permalink(String permalink) {
		this.permalink = permalink;
		return this;
	}

	public EligibleAdvertisementBuilder attributes(List<Attribute> attributes) {
		this.attributes = attributes;
		return this;
	}

	public EligibleAdvertisementBuilder siteId(String siteId) {
		this.siteId = siteId;
		return this;
	}

	public EligibleAdvertisementBuilder status(String status) {
		this.status = status;
		return this;
	}

	public EligibleAdvertisementBuilder itemsAmount(int itemsAmount) {
		this.itemsAmount = itemsAmount;
		return this;
	}

	public EligibleAdvertisementBuilder variationId(Long variationId) {
		this.variationId = variationId == 0 ? null : variationId;
		return this;
	}

	public EligibleAdvertisementBuilder variationSKU(String variationSKU) {
		this.variationSKU = variationSKU;
		return this;
	}

	public EligibleAdvertisementBuilder brand(String brand) {
		this.brand = brand;
		return this;
	}

	public EligibleAdvertisementBuilder model(String model) {
		this.model = model;
		return this;
	}

	public EligibleAdvertisementBuilder productIdentifier(String productIdentifier) {
		this.productIdentifier = productIdentifier;
		return this;
	}

	public EligibleAdvertisementBuilder meliId(String meliId) {
		this.meliId = meliId;
		return this;
	}

	public EligibleAdvertisement build() {
		EligibleAdvertisement eligibleAdvertisement = new EligibleAdvertisement();
		eligibleAdvertisement.setId(id);
		eligibleAdvertisement.setMlbId(mlbId);
		eligibleAdvertisement.setTitle(title);
		eligibleAdvertisement.setSubtitle(subtitle);
		eligibleAdvertisement.setPrice(price);
		eligibleAdvertisement.setDomainId(domainId);
		eligibleAdvertisement.setImage(image);
		eligibleAdvertisement.setVariationName(variationName);
		eligibleAdvertisement.setAvailableQuantity(availableQuantity);
		eligibleAdvertisement.setPermalink(permalink);
		eligibleAdvertisement.setAttributes(attributes);
		eligibleAdvertisement.setSiteId(siteId);
		eligibleAdvertisement.setStatus(status);
		eligibleAdvertisement.setVariationId(variationId);
		eligibleAdvertisement.setVariationSKU(variationSKU);
		eligibleAdvertisement.setBrand(brand);
		eligibleAdvertisement.setModel(model);
		eligibleAdvertisement.setProductIdentifier(productIdentifier);
		eligibleAdvertisement.setMeliId(meliId);

		return eligibleAdvertisement;
	}

	public List<EligibleAdvertisement> buildList() {
		List<EligibleAdvertisement> advertisements = new ArrayList<>();

		for (int i = 0; i < itemsAmount; i++) {
			EligibleAdvertisement EligibleAdvertisement = new EligibleAdvertisement();
			EligibleAdvertisement.setId(RandomUtils.nextLong(0, 1000));
			advertisements.add(EligibleAdvertisement);
		}

		return advertisements;
	}

}
