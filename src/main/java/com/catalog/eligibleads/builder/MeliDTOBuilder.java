package com.catalog.eligibleads.builder;

import org.apache.commons.lang3.StringUtils;

import com.catalog.eligibleads.dto.MeliDTO;

public class MeliDTOBuilder {

	private String id;
	private String accessToken;
	private String nomeConta;
	private String refreshToken;

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

	public MeliDTOBuilder refreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}

	public MeliDTO build() {

		MeliDTO meliDTO = new MeliDTO();
		meliDTO.setId(id);
		meliDTO.setAccessToken(accessToken);
		meliDTO.setNomeConta(nomeConta);
		meliDTO.setRefreshToken(refreshToken);
		return meliDTO;
	}

	public MeliDTO modify(MeliDTO meliDTO) {

		if (StringUtils.isNotBlank(id)) {
			meliDTO.setId(id);
		}
		if (StringUtils.isNotBlank(accessToken)) {
			meliDTO.setId(accessToken);
		}
		if (StringUtils.isNotBlank(nomeConta)) {
			meliDTO.setId(nomeConta);
		}
		if (StringUtils.isNotBlank(refreshToken)) {
			meliDTO.setId(refreshToken);
		}

		return meliDTO;
	}

}
