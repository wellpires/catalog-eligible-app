package com.catalog.eligibleads.builder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import com.catalog.eligibleads.dto.MeliDTO;

public class MeliDTOBuilder {

	private String id;
	private String accessToken;
	private String nameAccount;
	private String refreshToken;
	private Long clientId;
	private String clientSecret;
	private int itemsAmount;
	private boolean allowNull;

	public MeliDTOBuilder id(String id) {
		this.id = id;
		return this;
	}

	public MeliDTOBuilder accessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	public MeliDTOBuilder nameAccount(String nameAccount) {
		this.nameAccount = nameAccount;
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

	public MeliDTOBuilder itemsAmount(int itemsAmount) {
		this.itemsAmount = itemsAmount;
		return this;
	}

	public MeliDTOBuilder refreshToken(String refreshToken, boolean allowNull) {
		this.refreshToken = refreshToken;
		this.allowNull = allowNull;
		return this;
	}

	public MeliDTO build() {

		MeliDTO meliDTO = new MeliDTO();
		meliDTO.setId(id);
		meliDTO.setAccessToken(accessToken);
		meliDTO.setNameAccount(nameAccount);
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
			meliDTO.setAccessToken(accessToken);
		}
		if (StringUtils.isNotBlank(nameAccount)) {
			meliDTO.setNameAccount(nameAccount);
		}
		if (StringUtils.isNotBlank(refreshToken) || allowNull) {
			meliDTO.setRefreshToken(refreshToken);
		}
		if (Objects.nonNull(clientId)) {
			meliDTO.setClientId(clientId);
		}
		if (StringUtils.isNotBlank(clientSecret)) {
			meliDTO.setClientSecret(clientSecret);
		}

		return meliDTO;
	}

	public List<MeliDTO> buildList() {
		return IntStream.range(0, itemsAmount).mapToObj(index -> {
			MeliDTO meliDTO = new MeliDTO();
			meliDTO.setId(id);
			meliDTO.setAccessToken(accessToken);
			meliDTO.setNameAccount(nameAccount);
			meliDTO.setRefreshToken(refreshToken);
			meliDTO.setClientId(clientId);
			meliDTO.setClientSecret(clientSecret);
			return meliDTO;
		}).collect(Collectors.toList());
	}

}
