package com.nutrymaco.reservation.exception;

import java.util.UUID;

/**
 * ResevationNotFoundException
 */
public class ReservationNotFoundException extends RuntimeException {

	private final String username;
	private final UUID reservationUid;

	public ReservationNotFoundException(String username, UUID reservationUid) {
		this.username = username;
		this.reservationUid = reservationUid;
	}

	@Override
	public String getMessage() {
		var message = new StringBuffer();
		message.append("cant found reservation with:");
		if (username != null) {
			message.append("\n\tusername = " + username);
		}
		if (reservationUid != null) {
			message.append("\n\treservationuid = " + reservationUid);
		}
		return message.toString();
	}
	
	
   
}
