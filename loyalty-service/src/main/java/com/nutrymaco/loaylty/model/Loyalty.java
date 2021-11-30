package com.nutrymaco.loaylty.model;

/**
 * Loyalty
 */
public class Loyalty {
	
	private int id;
	private String username;
	private int reservationCount;
	private LoyaltyStatus status;
	private int discount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getReservationCount() {
		return reservationCount;
	}
	public void setReservationCount(int reservationCount) {
		this.reservationCount = reservationCount;
	}
	public LoyaltyStatus getStatus() {
		return status;
	}
	public void setStatus(LoyaltyStatus status) {
		this.status = status;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	
}
