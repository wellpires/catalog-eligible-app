package com.catalog.eligibleads.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catalog.eligibleads.exception.ExpiredTokenNotFoundException;
import com.catalog.eligibleads.model.ExpiredToken;
import com.catalog.eligibleads.repository.ExpiredTokenRepository;
import com.catalog.eligibleads.service.ExpiredTokenService;

@Service
public class ExpiredTokenServiceImpl implements ExpiredTokenService {

	@Autowired
	private ExpiredTokenRepository expiredTokenRepository;

	@Override
	public void updateExpiredToken(String meliId) throws ExpiredTokenNotFoundException {
		ExpiredToken expiredToken = expiredTokenRepository.findById(meliId)
				.orElseThrow(ExpiredTokenNotFoundException::new);
		expiredToken.setDataExpiracao(new Date());
		expiredTokenRepository.flush();
	}

}
