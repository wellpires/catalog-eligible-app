package com.catalog.eligibleads.enums;

import java.util.stream.Stream;

public enum BuyBoxStatus {

	READY_FOR_OPTIN, ALREADY_OPTED_IN, CLOSED, PRODUCT_INACTIVE, CATALOG_PRODUCT_ID_NULL, NOT_ELIGIBLE, COMPETING;

	public boolean isReadyForOptin() {
		return this == READY_FOR_OPTIN;
	}

	public static BuyBoxStatus valueOf(int index) {
		return Stream.of(BuyBoxStatus.values()).filter(buyBoxStatuValue -> (index == buyBoxStatuValue.ordinal()))
				.findFirst().orElseGet(null);
	}

}
