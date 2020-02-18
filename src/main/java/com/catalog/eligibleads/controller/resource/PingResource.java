package com.catalog.eligibleads.controller.resource;

import org.springframework.http.ResponseEntity;

import com.catalog.eligibleads.response.ErrorResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Service that check application health")
public interface PingResource {

	@ApiOperation(value = "Check application health")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Application is healthy", response = String.class),
			@ApiResponse(code = 500, message = "Application is not healthy", response = ErrorResponse.class) })
	ResponseEntity<String> ping();

}
