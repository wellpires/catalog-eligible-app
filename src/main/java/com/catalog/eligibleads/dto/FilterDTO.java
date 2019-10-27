package com.catalog.eligibleads.dto;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.catalog.eligibleads.enums.AdvertisementStatus;
import com.catalog.eligibleads.enums.ListingType;
import com.catalog.eligibleads.enums.Order;

public class FilterDTO {

	private String categoryId;
	private Long limit;
	private ListingType listingTypeId;
	private Integer offset;
	private Order order;
	private String query;
	private String sku;
	private AdvertisementStatus status;
	private String accessToken;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public ListingType getListingTypeId() {
		return listingTypeId;
	}

	public void setListingTypeId(ListingType listingTypeId) {
		this.listingTypeId = listingTypeId;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public AdvertisementStatus getStatus() {
		return status;
	}

	public void setStatus(AdvertisementStatus status) {
		this.status = status;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public MultiValueMap<String, String> getParameters() {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

		if (StringUtils.isNotBlank(query)) {
			parameters.add("query", query);
		}

		if (Objects.nonNull(status) && StringUtils.isNotBlank(status.getStatus())) {
			parameters.add("status", status.getStatus());
		}

		if (StringUtils.isNotBlank(categoryId)) {
//			String categoryId = buscarCategoria(categoryId);
			parameters.add("category_id", categoryId);
		}

		if (Objects.nonNull(listingTypeId) && StringUtils.isNotBlank(listingTypeId.getListingType())) {
			parameters.add("listing_type_id", listingTypeId.getListingType());
		}

		if (Objects.nonNull(order) && StringUtils.isNotBlank(order.getOrder())) {
			parameters.add("orders", order.getOrder());
		}

		if (StringUtils.isNotBlank(sku)/* && !isMuitosSkus() */) {
			parameters.add("sku", sku);
		}

		if (Objects.nonNull(limit)) {
			String pageLimit = "50";
			if (limit < 50) {
				pageLimit = String.valueOf(limit);
			}
			parameters.add("limit", pageLimit);
		}
//		
//		if(buscaPublica && StringUtils.isNotBlank(sellerId)) {
//			mapa.put("seller_id", sellerId);
//		}

		parameters.add("offset", String.valueOf(offset));

		return parameters;
	}

//	category_id=linkteste
//limit=25
//listing_type_id=gold_pro
//offset=0
//orders=start_time_asc
//query=titulomlb
//sku=skuteste
//status=active
//access_token=APP_USR-189141373421891-102620-ec4093d5f2008ccaa5f98fad3d810873-111412004

}
