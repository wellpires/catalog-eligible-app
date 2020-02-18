package com.catalog.eligibleads.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catalog.eligibleads.builder.MeliAccountBuilder;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.redis.repository.MeliAccountRepository;
import com.catalog.eligibleads.service.MeliAccountService;

@Service
public class MeliAccountServiceImpl implements MeliAccountService {

	@Autowired
	private MeliAccountRepository meliAccountRepository;

	@Override
	public void saveMeliAccount(MeliDTO meli) {
		meliAccountRepository
				.save(new MeliAccountBuilder().meliId(meli.getId()).nameAccount(meli.getNameAccount()).build());
	}

	@Override
	public boolean meliAccountProbablyHasNoAds(MeliDTO meli) {
		return !meliAccountRepository.existsById(meli.getId());
	}

}
