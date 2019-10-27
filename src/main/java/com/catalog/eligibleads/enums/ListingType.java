package com.catalog.eligibleads.enums;

import java.util.stream.Stream;

public enum ListingType {

	GOLD_PRO("gold_pro"), GOLD_SPECIAL("gold_special"), FREE("free"), TODOS("");

	private String listingType;

	private ListingType(String listingType) {
		this.listingType = listingType;
	}

	public String getListingType() {
		return listingType;
	}

	public static ListingType resolve(String listingType) {
		return Stream.of(values()).filter(value -> value.getListingType().equals(listingType)).findFirst()
				.orElse(TODOS);
	}

}
