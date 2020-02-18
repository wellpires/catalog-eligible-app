package com.catalog.eligibleads.builder;

import com.catalog.eligibleads.redis.model.MeliAccount;

public class MeliAccountBuilder {

	private String meliId;
	private String nameAccount;

	public MeliAccountBuilder meliId(String meliId) {
		this.meliId = meliId;
		return this;
	}

	public MeliAccountBuilder nameAccount(String nameAccount) {
		this.nameAccount = nameAccount;
		return this;
	}

	public MeliAccount build() {
		MeliAccount meliAccount = new MeliAccount();
		meliAccount.setId(meliId);
		meliAccount.setNameAccount(nameAccount);
		return meliAccount;
	}

}
