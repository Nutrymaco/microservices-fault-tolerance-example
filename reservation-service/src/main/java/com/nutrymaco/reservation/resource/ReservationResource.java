package com.nutrymaco.reservation.resource;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nutrymaco.reservation.model.Reservation;
import com.nutrymaco.reservation.model.Status;
import com.nutrymaco.reservation.repository.ReservationRepository;
import com.nutrymaco.reservation.exception.ReservationNotFoundException;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;


@Path("/internal/api/v1/reservations")
public class ReservationResource {

	@Inject
	ReservationRepository reservationRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Reservation>  getReservationsForUser(@HeaderParam("X-User-Name") String username) {
		System.out.println("getReservationForUser(" + username + ")");
		return reservationRepository.getReservationsByUsername(username);
	}

	@GET
	@Path("/{reservationUid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Reservation getUserReservation(@PathParam("reservationUid") UUID reservationUid,
										  @HeaderParam("X-User-Name") String username) {
		System.out.println("getUserReservation(" + reservationUid + ", " + username + ")");
		return reservationRepository.getReservationsByUsername(username)
			.stream()
			.filter(reservation -> reservationUid.equals(reservation.getReservationUid()))
			.findFirst()
			.orElseThrow(() -> new ReservationNotFoundException(username, reservationUid));
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Reservation createReservation(Reservation reservation) {
		System.out.println("createReservation()");
		return reservationRepository.createReservation(reservation);
	}

	@PATCH
	@Path("/{reservationUid}/cancel")
	public void cancelReservation(@PathParam("reservationUid") UUID reservationUid,
			@HeaderParam("username") String username) {
		System.out.println("cancelReservation()");
		reservationRepository.setReservationStatus(reservationUid, Status.CANCELED);
	}
}
