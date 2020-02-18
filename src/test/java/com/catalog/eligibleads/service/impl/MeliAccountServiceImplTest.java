package com.catalog.eligibleads.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.redis.model.MeliAccount;
import com.catalog.eligibleads.redis.repository.MeliAccountRepository;
import com.catalog.eligibleads.service.MeliAccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MeliAccountServiceImplTest {

	@Autowired
	private MeliAccountService meliAccountService;

	@Autowired
	private MeliAccountRepository meliAccountRepository;

	@Test
	public void shouldSaveMeliAccount() {
		when(meliAccountRepository.save(any(MeliAccount.class))).thenReturn(new MeliAccount());

		meliAccountService.saveMeliAccount(new MeliDTO());
	}

	@Test
	public void shouldReturnMeliAccountExists() {

		when(meliAccountRepository.existsById(anyString())).thenReturn(Boolean.TRUE);

		meliAccountService.meliAccountProbablyHasNoAds(new MeliDTO());

	}

	@Test
	public void shouldReturnMeliAccountNotExists() {

		when(meliAccountRepository.existsById(anyString())).thenReturn(Boolean.FALSE);

		meliAccountService.meliAccountProbablyHasNoAds(new MeliDTO());

	}

	@Configuration
	public static class Config {

		@Bean
		public MeliAccountService meliAccountService() {
			return new MeliAccountServiceImpl();
		}

		@Bean
		public MeliAccountRepository meliAccountRepository() {
			return mock(MeliAccountRepository.class);
		}

	}

}
