package com.nutrymaco.gateway.service;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.nutrymaco.gateway.model.Loyalty;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * LoyaltyService
 */
@Path("/internal/api/v1/loyalties")
@RegisterRestClient(configKey = "loyalty-service")
@ApplicationScoped
public interface LoyaltyServiceClient {
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	Loyalty getLoyalty(@PathParam("username") String username);

	@PATCH
	@Path("/{username}/increase")
	void increaseReservationCount(@PathParam("username") String username);

	@PATCH
	@Path("/{username}/decrease")
	void decreaseReservationCount(@PathParam("username") String username);

	@GET
	void healthCheck();

}
