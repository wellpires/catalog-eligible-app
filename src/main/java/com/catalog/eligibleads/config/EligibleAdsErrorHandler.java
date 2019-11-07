package com.catalog.eligibleads.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class EligibleAdsErrorHandler implements ResponseErrorHandler {

	private static final Logger logger = LoggerFactory.getLogger(EligibleAdsErrorHandler.class);

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return true;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		logger.error("{} - {}", response.getRawStatusCode(), response.getStatusText());
	}

}
