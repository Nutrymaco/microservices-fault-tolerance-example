package com.nutrymaco.gateway.wrapper.impl;

import com.nutrymaco.gateway.model.Hotel;
import com.nutrymaco.gateway.model.Reservation;
import com.nutrymaco.gateway.service.ReservationServiceClient;
import com.nutrymaco.gateway.service.annotation.ReservationService;
import com.nutrymaco.gateway.wrapper.CircuitBreaker;
import com.nutrymaco.gateway.wrapper.Status;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Qualifier;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ReservationServiceWrapper
 */
@ApplicationScoped
public class ReservationServiceWrapper {

	@Inject
	@RestClient
	ReservationServiceClient reservationService;

	@Inject
	@ReservationService
	CircuitBreaker circuitBreaker;
	
	public Optional<List<Reservation>> getReservations(String username) {
		return circuitBreaker.tryOrGetEmpty(
				() -> reservationService.getReservations(username)
		);
	}

	public Optional<Reservation> getUserReservation(UUID reservationUid, String username) {
		return circuitBreaker.tryOrGetEmpty(
				() -> reservationService.getUserReservation(reservationUid, username)
		);
	}

	public Optional<List<Hotel>> getHotels() {
		return circuitBreaker.tryOrGetEmpty(
				() -> reservationService.getHotels()
		);
	}

	public Optional<Reservation> createReservation(Reservation reservation) {
		return circuitBreaker.tryOrGetEmpty(
				() -> reservationService.createReservation(reservation)
		);
	}

	public Status cancelReservation(UUID reservationUid, String username) {
		return circuitBreaker.tryAndGetStatus(
				() -> reservationService.cancelReservation(reservationUid, username)
		);
	}

}
