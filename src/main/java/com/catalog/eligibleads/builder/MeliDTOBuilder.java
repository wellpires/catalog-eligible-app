package com.catalog.eligibleads.builder;

import com.catalog.eligibleads.dto.MeliDTO;

public class MeliDTOBuilder {

	private String id;
	private String accessToken;
	private String nomeConta;

	public MeliDTOBuilder id(String id) {
		this.id = id;
		return this;
	}

	public MeliDTOBuilder accessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	public MeliDTOBuilder nomeConta(String clienteApelido) {
		this.nomeConta = clienteApelido;
		return this;
	}

	public MeliDTO build() {
		MeliDTO meliDTO = new MeliDTO();
		meliDTO.setId(id);
		meliDTO.setAccessToken(accessToken);
		meliDTO.setNomeConta(nomeConta);
		return meliDTO;
	}

}
