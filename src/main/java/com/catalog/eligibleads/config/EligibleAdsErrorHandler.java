package com.catalog.eligibleads.config;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class EligibleAdsErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return true;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
	}

}
