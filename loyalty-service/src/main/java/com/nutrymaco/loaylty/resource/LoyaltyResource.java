package com.nutrymaco.loaylty.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nutrymaco.loaylty.model.Loyalty;
import com.nutrymaco.loaylty.repository.LoyaltyRepository;

@Path("/internal/api/v1/loyalties")
public class LoyaltyResource {

	@Inject
	LoyaltyRepository loyaltyRepository;


	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Loyalty getLoyalty(@PathParam("username") String username) {
		System.out.println("getLoyalty()");
		return loyaltyRepository.getLoyalty(username);
	}

	@PATCH
	@Path("/{username}/increase")
	public void increaseReservationCount(@PathParam("username") String username) {
		System.out.println("increaseReservationCount()");
		loyaltyRepository.increaseReservationCount(username);
	}

	@PATCH
	@Path("/{username}/decrease")
	public void decreaseReservationCount(@PathParam("username") String username) {
		System.out.println("decreaseReservationCount()");
		loyaltyRepository.decreaseReservationCount(username);
	}
	
}
