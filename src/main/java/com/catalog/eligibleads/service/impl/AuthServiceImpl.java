package com.catalog.eligibleads.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.catalog.eligibleads.dto.AuthTokenDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.exception.ClientAPIErrorException;
import com.catalog.eligibleads.exception.InvalidTokenException;
import com.catalog.eligibleads.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private RestTemplate client;

	private String urlRefreshToken = "https://api.mercadolibre.com/oauth/token";

	@Override
	public AuthTokenDTO findToken(MeliDTO meli) throws InvalidTokenException, ClientAPIErrorException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("grant_type", "refresh_token");
		map.add("client_id", String.valueOf(meli.getClientId()));
		map.add("client_secret", meli.getClientSecret());
		map.add("refresh_token", meli.getRefreshToken());

		ResponseEntity<AuthTokenDTO> response = client.postForEntity(urlRefreshToken,
				new HttpEntity<MultiValueMap<String, String>>(map, headers), AuthTokenDTO.class);

		AuthTokenDTO authTokenDTO = response.getBody();
		if (HttpStatus.BAD_REQUEST.equals(response.getStatusCode())
				|| HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
			throw new InvalidTokenException("Token is invalid!");
		} else if (response.getStatusCode().series() == Series.CLIENT_ERROR
				|| response.getStatusCode().series() == Series.SERVER_ERROR) {
			throw new ClientAPIErrorException(authTokenDTO.getMessage());
		}

		return authTokenDTO;
	}

}
