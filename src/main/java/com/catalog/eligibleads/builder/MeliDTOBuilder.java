package com.catalog.eligibleads.builder;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.catalog.eligibleads.dto.MeliDTO;

public class MeliDTOBuilder {

	private String id;
	private String accessToken;
	private String nomeConta;
	private String refreshToken;
	private Long clientId;
	private String clientSecret;

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

	public MeliDTOBuilder clientId(Long clientId) {
		this.clientId = clientId;
		return this;
	}

	public MeliDTOBuilder clientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
		return this;
	}

	public MeliDTO build() {

		MeliDTO meliDTO = new MeliDTO();
		meliDTO.setId(id);
		meliDTO.setAccessToken(accessToken);
		meliDTO.setNameAccount(nomeConta);
		meliDTO.setRefreshToken(refreshToken);
		meliDTO.setClientId(clientId);
		meliDTO.setClientSecret(clientSecret);
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
		if (Objects.nonNull(clientId)) {
			meliDTO.setClientId(clientId);
		}
		if (StringUtils.isNotBlank(clientSecret)) {
			meliDTO.setClientSecret(clientSecret);
		}

		return meliDTO;
	}

}
