package com.catalog.eligibleads.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.catalog.eligibleads.builder.AuthTokenDTOBuilder;
import com.catalog.eligibleads.builder.MeliDTOBuilder;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.exception.ClientAPIErrorException;
import com.catalog.eligibleads.exception.ExpiredTokenNotFoundException;
import com.catalog.eligibleads.exception.InvalidTokenException;
import com.catalog.eligibleads.exception.MeliNotFoundException;
import com.catalog.eligibleads.service.AuthService;
import com.catalog.eligibleads.service.ExpiredTokenService;
import com.catalog.eligibleads.service.MeliService;
import com.catalog.eligibleads.service.TokenValidationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TokenValidationServiceImplTest {

	@Autowired
	private TokenValidationService tokenValidationService;

	@Autowired
	private AuthService authService;

	@Autowired
	private ExpiredTokenService expiredTokenService;

	@Autowired
	private MeliService meliService;

	@Test
	public void shouldRefreshToken() throws InvalidTokenException, ClientAPIErrorException, MeliNotFoundException,
			ExpiredTokenNotFoundException {

		String newAccessToken = "new-access-token";
		String newRefreshToken = "new-refresh-token";
		when(this.authService.findToken(any(MeliDTO.class))).thenReturn(
				new AuthTokenDTOBuilder().accessToken(newAccessToken).refreshToken(newRefreshToken).build());

		String sameID = "id";
		MeliDTO newMeliDTO = tokenValidationService.refreshToken(new MeliDTOBuilder().id(sameID)
				.accessToken("old-access-token").refreshToken("old-refresh-token").build());

		assertThat("Should return new access token", newMeliDTO.getAccessToken(), equalTo(newAccessToken));
		assertThat("Should return new refresh token", newMeliDTO.getRefreshToken(), equalTo(newRefreshToken));
		assertThat("Should return same id", newMeliDTO.getId(), equalTo(sameID));
		verify(meliService, times(1)).updateAccessTokenAndRefreshToken(any(MeliDTO.class));

	}

	@Test
	public void shouldNotRefreshTokenBecauseAccessTokenAndRefreshTokenIsNull() throws InvalidTokenException,
			ClientAPIErrorException, MeliNotFoundException, ExpiredTokenNotFoundException {

		when(this.authService.findToken(any(MeliDTO.class))).thenReturn(new AuthTokenDTOBuilder().build());

		String sameID = "id";
		String oldAccessToken = "old-access-token";
		MeliDTO newMeliDTO = tokenValidationService.refreshToken(
				new MeliDTOBuilder().id(sameID).accessToken(oldAccessToken).refreshToken("old-refresh-token").build());

		assertThat("Should return old access token", newMeliDTO.getAccessToken(), equalTo(oldAccessToken));
		assertNull("Should return refresh token as null", newMeliDTO.getRefreshToken());
		assertThat("Should return same id", newMeliDTO.getId(), equalTo(sameID));
		verify(meliService, times(1)).updateAccessTokenAndRefreshToken(any(MeliDTO.class));

	}

	@Test
	public void shouldNotRefreshTokenBecauseTokenWasNotFound() throws InvalidTokenException, ClientAPIErrorException,
			ExpiredTokenNotFoundException, MeliNotFoundException {

		when(this.authService.findToken(any(MeliDTO.class))).thenThrow(InvalidTokenException.class);

		MeliDTO meliDTO = tokenValidationService.refreshToken(new MeliDTOBuilder().id("id")
				.accessToken("old-access-token").refreshToken("old-refresh-token").build());

		assertThat("Should return old access token", meliDTO.getAccessToken(), equalTo("old-access-token"));
		assertThat("Should return old refresh token", meliDTO.getRefreshToken(), equalTo("old-refresh-token"));
		verify(expiredTokenService, times(1)).updateExpiredToken(anyString());

	}

	@Test(expected = ClientAPIErrorException.class)
	public void shouldNotRefreshTokenBecauseTokenServiceIsNotAvailable() throws InvalidTokenException,
			ClientAPIErrorException, ExpiredTokenNotFoundException, MeliNotFoundException {

		when(this.authService.findToken(any(MeliDTO.class))).thenThrow(ClientAPIErrorException.class);

		tokenValidationService.refreshToken(new MeliDTOBuilder().id("id").accessToken("old-access-token")
				.refreshToken("old-refresh-token").build());

	}

	@Before
	public void setup() {
		reset(meliService);
	}

	@Configuration
	public static class Config {

		@Bean
		public TokenValidationService tokenValidationService() {
			return new TokenValidationServiceImpl();
		}

		@Bean
		public AuthService authService() {
			return mock(AuthService.class);
		}

		@Bean
		public ExpiredTokenService expiredTokenService() {
			return mock(ExpiredTokenService.class);
		}

		@Bean
		public MeliService meliService() {
			return mock(MeliService.class);
		}

	}

}
