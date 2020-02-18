package com.catalog.eligibleads.builder;

import com.catalog.eligibleads.dto.AuthTokenDTO;

public class AuthTokenDTOBuilder {

	private String accessToken;
	private String refreshToken;
	private String message;

	public AuthTokenDTOBuilder accessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	public AuthTokenDTOBuilder refreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}

	public AuthTokenDTOBuilder message(String message) {
		this.message = message;
		return this;
	}

	public AuthTokenDTO build() {
		AuthTokenDTO authTokenDTO = new AuthTokenDTO();
		authTokenDTO.setAccessToken(accessToken);
		authTokenDTO.setRefreshToken(refreshToken);
		authTokenDTO.setMessage(message);
		return authTokenDTO;
	}

}
