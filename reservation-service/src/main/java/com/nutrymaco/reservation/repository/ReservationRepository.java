package com.nutrymaco.reservation.repository;

import java.util.List;
import java.util.UUID;

import com.nutrymaco.reservation.model.Reservation;
import com.nutrymaco.reservation.model.Status;

/**
 * Repository for Reservtion entity
 */
public interface ReservationRepository {

	List<Reservation> getReservationsByUsername(String username);

	Reservation createReservation(Reservation reservation);

	void setReservationStatus(UUID reservationUid, Status status);
	
}
