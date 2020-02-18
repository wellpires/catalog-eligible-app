package com.catalog.eligibleads.builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.catalog.eligibleads.model.Meli;

public class MeliBuilder {

	private int itemsAmount;
	private String accessToken;
	private String refreshToken;

	public MeliBuilder itemsAmount(int itemsAmount) {
		this.itemsAmount = itemsAmount;
		return this;
	}

	public MeliBuilder accessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	public MeliBuilder refreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}

	public List<Meli> buildList() {

		return IntStream.range(0, itemsAmount).mapToObj(index -> {
			return new Meli();
		}).collect(Collectors.toList());

	}

	public Meli build() {
		Meli meli = new Meli();
		meli.setAccessToken(accessToken);
		meli.setRefreshToken(refreshToken);
		return meli;
	}

}
