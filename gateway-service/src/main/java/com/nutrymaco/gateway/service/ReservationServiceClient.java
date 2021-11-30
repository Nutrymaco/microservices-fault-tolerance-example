package com.nutrymaco.gateway.service;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nutrymaco.gateway.model.*;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * ReservationService - http client 
 */
@Path("/internal/api/v1")
@RegisterRestClient(configKey = "reservation-service")
@ApplicationScoped
public interface ReservationServiceClient {

	@GET
	@Path("/reservations")
	@Produces(MediaType.APPLICATION_JSON)
	List<Reservation> getReservations(@HeaderParam("X-User-Name") String username);

	@GET
	@Path("/reservations/{reservationUid}")
	Reservation getUserReservation(@PathParam("reservationUid") UUID reservationUid,
			@HeaderParam("X-User-Name") String username);

	@GET
	@Path("/hotels")
	@Produces(MediaType.APPLICATION_JSON)
	List<Hotel> getHotels();

	@POST
	@Path("/reservations")
	@Produces(MediaType.APPLICATION_JSON)
	Reservation createReservation(Reservation reservation);


	@PATCH
	@Path("/reservations/{reservationUid}/cancel")
	void cancelReservation(@PathParam("reservationUid") UUID reservationUid,
			@HeaderParam("X-User-Name") String username);
	
}
