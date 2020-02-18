package com.catalog.eligibleads.service.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.catalog.eligibleads.exception.ExpiredTokenNotFoundException;
import com.catalog.eligibleads.model.ExpiredToken;
import com.catalog.eligibleads.repository.ExpiredTokenRepository;
import com.catalog.eligibleads.service.ExpiredTokenService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ExpiredTokenServiceImplTest {

	@Autowired
	private ExpiredTokenService expiredTokenService;

	@Autowired
	private ExpiredTokenRepository expiredTokenRepository;

	@Test
	public void shouldUpdateExpiredToken() throws ExpiredTokenNotFoundException {

		when(expiredTokenRepository.findById(anyString())).thenReturn(Optional.of(new ExpiredToken()));

		expiredTokenService.updateExpiredToken("mlb-teste");

		verify(expiredTokenRepository, times(1)).flush();

	}

	@Test(expected = ExpiredTokenNotFoundException.class)
	public void shouldNotUpdateExpiredTokenBecauseExpiredTokenNotFound() throws ExpiredTokenNotFoundException {

		when(expiredTokenRepository.findById(anyString())).thenReturn(Optional.empty());

		expiredTokenService.updateExpiredToken("mlb-teste");

	}

	@Configuration
	public static class Config {

		@Bean
		public ExpiredTokenService expiredTokenService() {
			return new ExpiredTokenServiceImpl();
		}

		@Bean
		public ExpiredTokenRepository expiredTokenRepository() {
			return mock(ExpiredTokenRepository.class);
		}

	}

}
