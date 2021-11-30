package com.nutrymaco.gateway.model;

import java.util.UUID;

/**
 * Payment
 */
public class Payment {
	
	private int id;
	private UUID paymentUid;
	private Status status;
	private int price;

	
	public Payment(int id, UUID paymentUid, Status status, int price) {
		this.id = id;
		this.paymentUid = paymentUid;
		this.status = status;
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
	public void setStatus(Status status) {
		this.status = status;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
	
}
