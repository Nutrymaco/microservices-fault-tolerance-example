package com.nutrymaco.gateway.dto;

import java.util.UUID;

import com.nutrymaco.gateway.model.Status;

/**
 * CreateReservationResponse
 */
public class CreateReservationResponse {
	
	private final UUID reservationUid;
	private final UUID hotelUid;
	private final String startDate;
	private final String endDate;
	private final int discount;
	private final Status status;
	private final PaymentDto payment;
	public CreateReservationResponse(UUID reservationUid, UUID hotelUid, String startDate, String endDate, int discount,
			Status status, PaymentDto payment) {
		this.reservationUid = reservationUid;
		this.hotelUid = hotelUid;
		this.startDate = startDate;
		this.endDate = endDate;
		this.discount = discount;
		this.status = status;
		this.payment = payment;
	}
	public UUID getReservationUid() {
		return reservationUid;
	}
	public UUID getHotelUid() {
		return hotelUid;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public int getDiscount() {
		return discount;
	}
	public Status getStatus() {
		return status;
	}
	public PaymentDto getPayment() {
		return payment;
	}

	
	
	
}
