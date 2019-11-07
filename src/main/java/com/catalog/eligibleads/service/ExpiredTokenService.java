package com.catalog.eligibleads.service;

import com.catalog.eligibleads.exception.ExpiredTokenNotFoundException;

public interface ExpiredTokenService {

	void updateExpiredToken(String meliId) throws ExpiredTokenNotFoundException;

}
