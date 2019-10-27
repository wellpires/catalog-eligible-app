package com.catalog.eligibleads.function;

import java.util.function.Function;

import com.catalog.eligibleads.builder.MeliDTOBuilder;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.model.Meli;

public class Meli2MeliDTOFunction implements Function<Meli, MeliDTO> {

	@Override
	public MeliDTO apply(Meli meli) {
		return new MeliDTOBuilder().id(meli.getId()).accessToken(meli.getAccessToken()).build();
	}

}
