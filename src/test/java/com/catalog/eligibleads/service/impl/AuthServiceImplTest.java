package com.catalog.eligibleads.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.catalog.eligibleads.builder.AuthTokenDTOBuilder;
import com.catalog.eligibleads.builder.MeliDTOBuilder;
import com.catalog.eligibleads.dto.AuthTokenDTO;
import com.catalog.eligibleads.dto.MeliDTO;
import com.catalog.eligibleads.exception.ClientAPIErrorException;
import com.catalog.eligibleads.exception.InvalidTokenException;
import com.catalog.eligibleads.service.AuthService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestPropertySource(properties = { "api.mercadolivre.oauth.token=http://meli.teste" })
public class AuthServiceImplTest {

	@Autowired
	private RestTemplate client;

	@Autowired
	private AuthService authService;

	@Test
	public void shouldFindToken() throws InvalidTokenException, ClientAPIErrorException {

		AuthTokenDTO authTokenDTO = new AuthTokenDTOBuilder().accessToken("token-teste-teste")
				.refreshToken("refresh-token-teste").build();

		when(client.postForEntity(anyString(), any(HttpEntity.class), any()))
				.thenReturn(ResponseEntity.ok(authTokenDTO));

		MeliDTO meliDTOBuilt = new MeliDTOBuilder().accessToken("acess-token-teste").refreshToken("refresh-token-teste")
				.clientId(19l).build();
		AuthTokenDTO authTokenDTOBuilt = authService.findToken(meliDTOBuilt);

		assertThat("Should return new access-token", authTokenDTOBuilt.getAccessToken(), equalTo("token-teste-teste"));
		assertThat("Should return new token-teste", authTokenDTOBuilt.getRefreshToken(),
				equalTo("refresh-token-teste"));

	}

	@Test(expected = InvalidTokenException.class)
	public void shouldNotFindTokenBecauseTokenServiceReturnAunauthorizedHttpStatus()
			throws InvalidTokenException, ClientAPIErrorException {

		when(client.postForEntity(anyString(), any(HttpEntity.class), any()))
				.thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

		MeliDTO meliDTOBuilt = new MeliDTOBuilder().accessToken("acess-token-teste").refreshToken("refresh-token-teste")
				.clientId(19l).build();

		authService.findToken(meliDTOBuilt);

	}

	@Test
	public void shouldNotFindTokenBecauseTokenServiceIsNotAvailable() throws InvalidTokenException {

		String message_error = "internal server error";
		AuthTokenDTO authTokenDTO = new AuthTokenDTOBuilder().message(message_error).build();

		when(client.postForEntity(anyString(), any(HttpEntity.class), any()))
				.thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(authTokenDTO));

		MeliDTO meliDTOBuilt = new MeliDTOBuilder().accessToken("acess-token-teste").refreshToken("refresh-token-teste")
				.clientId(19l).build();

		try {
			authService.findToken(meliDTOBuilt);
		} catch (ClientAPIErrorException e) {
			assertThat("Should return message when error was thrown", e.getMessage(), equalTo(message_error));
		}

	}

	@Configuration
	public static class Config {

		@Bean
		public RestTemplate client() {
			return mock(RestTemplate.class);
		}

		@Bean
		public AuthService authService() {
			return new AuthServiceImpl();
		}

	}

}
