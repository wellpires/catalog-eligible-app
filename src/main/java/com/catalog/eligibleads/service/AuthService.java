package com.catalog.eligibleads.service;

import com.catalog.eligibleads.dto.AuthTokenDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.exception.ClientAPIErrorException;
import com.catalog.eligibleads.exception.InvalidTokenException;

public interface AuthService {

	AuthTokenDTO findToken(MeliDTO meli) throws InvalidTokenException, ClientAPIErrorException;

}
