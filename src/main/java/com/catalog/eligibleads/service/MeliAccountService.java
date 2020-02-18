package com.catalog.eligibleads.service;

import com.catalog.eligibleads.dto.MeliDTO;

public interface MeliAccountService {

	void saveMeliAccount(MeliDTO meli);

	boolean meliAccountProbablyHasNoAds(MeliDTO meli);

}
