package com.catalog.eligibleads.enums;

import java.util.stream.Stream;

public enum Order {

	START_TIME_ASC("start_time_asc"), START_TIME_DESC("start_time_desc"), LAST_UPDATE_ASC("last_updated_asc"),
	LAST_UPDATE_DESC("last_updated_desc"), AVAILABLE_QUANTITY_ASC("available_quantity_asc"),
	AVAILABLE_QUANTITY_DESC("available_quantity_desc"), SOLD_QUANTITY_ASC("sold_quantity_asc"),
	SOLD_QUANTITY_DESC("sold_quantity_desc"), PRICE_ASC("price_asc"), PRICE_DESC("price_desc"), DEFAULT("");

	private String order;

	private Order(String order) {
		this.order = order;
	}

	public String getOrder() {
		return order;
	}

	public static Order resolve(String order) {
		return Stream.of(values()).filter(value -> value.getOrder().equals(order)).findFirst().orElse(DEFAULT);

	}

}
