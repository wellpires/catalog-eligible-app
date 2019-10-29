package com.catalog.eligibleads.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.catalog.eligibleads.dto.AdvertisementDTO;
import com.catalog.eligibleads.dto.AttributeDTO;

public class AdvertisementDTOBuilder {

	private String id;
	private String title;
	private String subtitle;
	private BigDecimal price;
	private String domainId;
	private String image;
	private String variationName;
	private Long availableQuantity;
	private String permalink;
	private int itemsAmount;
	private List<AttributeDTO> attributes;
	private String siteId;
	private String status;
	private Long variationId;
	private String variationSKU;
	private String brand;
	private String model;
	private String productIdentifier;
	private String meliId;

	public AdvertisementDTOBuilder id(String id) {
		this.id = id;
		return this;
	}

	public AdvertisementDTOBuilder title(String title) {
		this.title = title;
		return this;
	}

	public AdvertisementDTOBuilder subtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public AdvertisementDTOBuilder price(BigDecimal price) {
		this.price = price;
		return this;
	}

	public AdvertisementDTOBuilder domainId(String domainId) {
		this.domainId = domainId;
		return this;
	}

	public AdvertisementDTOBuilder image(String image) {
		this.image = image;
		return this;
	}

	public AdvertisementDTOBuilder variationName(String variationName) {
		this.variationName = variationName;
		return this;
	}

	public AdvertisementDTOBuilder id(Long id) {
		this.id = String.valueOf(id);
		return this;
	}

	public AdvertisementDTOBuilder availableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
		return this;
	}

	public AdvertisementDTOBuilder permalink(String permalink) {
		this.permalink = permalink;
		return this;
	}

	public AdvertisementDTOBuilder attributes(List<AttributeDTO> attributes) {
		this.attributes = attributes;
		return this;
	}

	public AdvertisementDTOBuilder siteId(String siteId) {
		this.siteId = siteId;
		return this;
	}

	public AdvertisementDTOBuilder status(String status) {
		this.status = status;
		return this;
	}

	public AdvertisementDTOBuilder itemsAmount(int itemsAmount) {
		this.itemsAmount = itemsAmount;
		return this;
	}

	public AdvertisementDTOBuilder variationId(Long variationId) {
		this.variationId = variationId == 0l ? null : variationId;
		return this;
	}

	public AdvertisementDTOBuilder variationSKU(String variationSKU) {
		this.variationSKU = variationSKU;
		return this;
	}

	public AdvertisementDTOBuilder brand(String brand) {
		this.brand = brand;
		return this;
	}

	public AdvertisementDTOBuilder model(String model) {
		this.model = model;
		return this;
	}

	public AdvertisementDTOBuilder productIdentifier(String productIdentifier) {
		this.productIdentifier = productIdentifier;
		return this;
	}

	public AdvertisementDTOBuilder meliId(String meliId) {
		this.meliId = meliId;
		return this;
	}

	public AdvertisementDTO build() {
		AdvertisementDTO adsDTO = new AdvertisementDTO();
		adsDTO.setId(id);
		adsDTO.setTitle(title);
		adsDTO.setSubtitle(subtitle);
		adsDTO.setPrice(price);
		adsDTO.setDomainId(domainId);
		adsDTO.setImage(image);
		adsDTO.setVariationName(variationName);
		adsDTO.setAvailableQuantity(availableQuantity);
		adsDTO.setPermalink(permalink);
		adsDTO.setAttributes(attributes);
		adsDTO.setSiteId(siteId);
		adsDTO.setStatus(status);
		adsDTO.setVariationId(variationId);
		adsDTO.setVariationSKU(variationSKU);
		adsDTO.setBrand(brand);
		adsDTO.setModel(model);
		adsDTO.setProductIdentifier(productIdentifier);
		adsDTO.setMeliId(meliId);

		return adsDTO;
	}

	public List<AdvertisementDTO> buildList() {
		List<AdvertisementDTO> advertisements = new ArrayList<>();

		for (int i = 0; i < itemsAmount; i++) {
			AdvertisementDTO advertisementDTO = new AdvertisementDTO();
			advertisementDTO.setId(RandomStringUtils.randomAlphanumeric(10));
			advertisements.add(advertisementDTO);
		}

		return advertisements;
	}

}
