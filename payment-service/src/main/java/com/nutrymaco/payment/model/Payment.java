package com.nutrymaco.payment.model;

import java.util.UUID;

/**
 * Payment
 */
public class Payment {
	
	private int id;
	private UUID paymentUid;
	private Status status;
	private int price;

	public Payment() {
		
	}
	
	public Payment(int id, UUID paymentUid, String status, int price) {
		this.id = id;
		this.paymentUid = paymentUid;
		this.status = Status.valueOf(status);
		this.price = price;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UUID getPaymentUid() {
		return paymentUid;
	}
	public void setPaymentUid(UUID paymentUid) {
		this.paymentUid = paymentUid;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = Status.valueOf(status);
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
	
}
