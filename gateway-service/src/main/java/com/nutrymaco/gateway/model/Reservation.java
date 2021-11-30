package com.nutrymaco.gateway.model;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Reservation
 */
public class Reservation {
	
	private int id;
	private UUID reservationUid;
	private String username;
	private UUID paymentUid;
	private int hotelId;
	private Status status;
	private long startDate;
	private long endDate;

	
	public Reservation(int id, UUID reservationUid, String username, UUID paymentUid, int hotelId, Status status,
			long startDate, long endDate) {
		this.id = id;
		this.reservationUid = reservationUid;
		this.username = username;
		this.paymentUid = paymentUid;
		this.hotelId = hotelId;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public UUID getReservationUid() {
		return reservationUid;
	}

	public void setReservationUid(UUID reservationUid) {
		this.reservationUid = reservationUid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UUID getPaymentUid() {
		return paymentUid;
	}

	public void setPaymentUid(UUID paymentUid) {
		this.paymentUid = paymentUid;
	}

	public int getHotelId() {
		return hotelId;
	}

	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}

	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	
}
