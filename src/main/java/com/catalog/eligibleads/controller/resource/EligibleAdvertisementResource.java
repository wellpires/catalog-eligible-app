package com.catalog.eligibleads.controller.resource;

import org.springframework.http.ResponseEntity;

import com.catalog.eligibleads.response.EligibleAdvertisementsResponse;
import com.catalog.eligibleads.response.ErrorResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Service that find eligible ads")
public interface EligibleAdvertisementResource {

	@ApiOperation(value = "Find eligible advertisements stored in Redis database")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return all eligible advertisements by Meli ID and with pagination", response = EligibleAdvertisementsResponse.class),
			@ApiResponse(code = 400, message = "Required request data was not sent", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "General exception happened", response = ErrorResponse.class) })
	public ResponseEntity<EligibleAdvertisementsResponse> findEligibleAds(
			@ApiParam(name = "meliId", value = "Meli ID to find the eligible advertisements", required = true) String meliId,
			@ApiParam(name = "limit", value = "Limit of the page", required = true) Long limit,
			@ApiParam(name = "page", value = "Page number", required = true) Long page);

}
