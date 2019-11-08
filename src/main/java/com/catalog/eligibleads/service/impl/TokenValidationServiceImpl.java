package com.catalog.eligibleads.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catalog.eligibleads.builder.MeliDTOBuilder;
import com.catalog.eligibleads.dto.AuthTokenDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.exception.ClientAPIErrorException;
import com.catalog.eligibleads.exception.ExpiredTokenNotFoundException;
import com.catalog.eligibleads.exception.InvalidTokenException;
import com.catalog.eligibleads.exception.MeliNotFoundException;
import com.catalog.eligibleads.service.AuthService;
import com.catalog.eligibleads.service.ExpiredTokenService;
import com.catalog.eligibleads.service.MeliService;
import com.catalog.eligibleads.service.TokenValidationService;

@Service
public class TokenValidationServiceImpl implements TokenValidationService {

	private final Logger logger = LoggerFactory.getLogger(TokenValidationServiceImpl.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private ExpiredTokenService expiredTokenService;

	@Autowired
	private MeliService meliService;

	@Override
	public MeliDTO refreshToken(MeliDTO meli)
			throws ExpiredTokenNotFoundException, ClientAPIErrorException, MeliNotFoundException {
		
		if("1".equals("1")) {
			throw new ClientAPIErrorException("APAGAR");
		}

		try {
			AuthTokenDTO authTokenDTO = this.authService.findToken(meli);

			MeliDTO meliDTO = new MeliDTOBuilder()
					.refreshToken(Optional.ofNullable(authTokenDTO.getRefreshToken()).orElseGet(() -> null))
					.accessToken(
							Optional.ofNullable(authTokenDTO.getAccessToken()).orElseGet(() -> meli.getAccessToken()))
					.modify(meli);

			meliService.updateAccessTokenAndRefreshToken(meliDTO);
			return meliDTO;

		} catch (InvalidTokenException e) {
			logger.error(e.getMessage());
			expiredTokenService.updateExpiredToken(meli.getId());
		} catch (ClientAPIErrorException e) {
			throw e;
		}
		return meli;

	}

}
