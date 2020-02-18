package com.catalog.eligibleads.service.impl;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.catalog.eligibleads.builder.MeliBuilder;
import com.catalog.eligibleads.builder.MeliDTOBuilder;
import com.catalog.eligibleads.exception.MeliNotFoundException;
import com.catalog.eligibleads.model.Meli;
import com.catalog.eligibleads.repository.MeliRepository;
import com.catalog.eligibleads.service.MeliService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MeliServiceImplTest {

	@Autowired
	private MeliService meliService;

	@Autowired
	private MeliRepository meliRepository;

	@Test
	public void shouldFindAllActivatedMeli() {

		when(meliRepository.findByActiveTrue()).thenReturn(new MeliBuilder().itemsAmount(20).buildList());

		assertThat("Should return all activated account", meliService.findAllActivatedMeli(), hasSize(20));

	}

	@Test
	public void shouldUpdateAccessTokenAndRefreshToken() throws MeliNotFoundException {

		when(meliRepository.findById(anyString())).thenReturn(Optional.ofNullable(
				new MeliBuilder().accessToken("old-access-token").refreshToken("old-refresh-token").build()));

		meliService.updateAccessTokenAndRefreshToken(new MeliDTOBuilder().id("123").accessToken("new-access-token")
				.refreshToken("new-refresh-token").build());

		verify(meliRepository, times(1)).saveAndFlush(Mockito.any(Meli.class));

	}

	@Test(expected = MeliNotFoundException.class)
	public void shouldNotUpdateAccessTokenAndRefreshTokenBecauseMeliAccountNotFound() throws MeliNotFoundException {

		when(meliRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

		meliService.updateAccessTokenAndRefreshToken(new MeliDTOBuilder().id("123").accessToken("new-access-token")
				.refreshToken("new-refresh-token").build());

		verify(meliRepository, never()).flush();

	}

	@Configuration
	public static class Config {

		@Bean
		public MeliService meliService() {
			return new MeliServiceImpl();
		}

		@Bean
		public MeliRepository meliRepository() {
			return mock(MeliRepository.class);
		}

	}

}
