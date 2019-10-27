package com.catalog.eligibleads.enums;

import java.util.stream.Stream;

public enum AdvertisementStatus {

	ACTIVE("active"), PAUSED("paused"), CLOSED("closed"), DEFAULT("");

	private String status;

	private AdvertisementStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public static AdvertisementStatus resolve(String status) {
		return Stream.of(values()).filter(value -> value.getStatus().equals(status)).findFirst().orElse(DEFAULT);
	}

}
